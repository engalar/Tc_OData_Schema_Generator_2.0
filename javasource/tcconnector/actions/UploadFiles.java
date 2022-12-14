// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package tcconnector.actions;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;
import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.thirdparty.org.json.JSONArray;
import com.mendix.thirdparty.org.json.JSONObject;
import com.mendix.webui.CustomJavaAction;
import com.teamcenter.fms.servercache.FSCException;
import com.teamcenter.fms.servercache.proxy.CommonsFSCWholeFileIOImpl;
import tcconnector.foundation.TcConnection;
import tcconnector.internal.foundation.Constants;
import tcconnector.internal.foundation.FMSUtils;
import tcconnector.internal.foundation.Messages;

/**
 * SOA URL: 
 * Core-2010-04-DataManagement/createDatasets
 * Core-2006-03-FileManagement/commitDatasetFiles
 * 
 * Uploads one or more files** to Teamcenter using teamcenter FMS service. 
 * 
 * Input -
 * - Dataset.Documents association should hold files to be uploaded
 * 
 * Output -
 * - Action returns True or False in case of success and failure respectively. 
 * - In addtion to this UID property is updated on Dataset object which is passed as an input to action.
 * 
 * 
 * ** - Only one file upload is supported at the moment
 */
public class UploadFiles extends CustomJavaAction<java.lang.Boolean>
{
	private IMendixObject __DatasetParameter;
	private tcconnector.proxies.Dataset DatasetParameter;
	private java.lang.String NamedReference;
	private java.lang.String ConfigurationName;

	public UploadFiles(IContext context, IMendixObject DatasetParameter, java.lang.String NamedReference, java.lang.String ConfigurationName)
	{
		super(context);
		this.__DatasetParameter = DatasetParameter;
		this.NamedReference = NamedReference;
		this.ConfigurationName = ConfigurationName;
	}

	@java.lang.Override
	public java.lang.Boolean executeAction() throws Exception
	{
		this.DatasetParameter = this.__DatasetParameter == null ? null : tcconnector.proxies.Dataset.initialize(getContext(), __DatasetParameter);

		// BEGIN USER CODE
		boolean areFilesUploaded = Boolean.TRUE;
		try {

			if (DatasetParameter.getDocuments().size() > 0) {
				// Create Dataset
				JSONObject createDatasetResponse = createDatasets();
				if (isDatasetCreated(createDatasetResponse) == true) {
					// Dataset Created. Upload the file.
					// Method would throw exception in case of error. Hence no return value
					uploadFiletoFMS(createDatasetResponse);

					// Commit Dataset
					JSONObject commitDatasetFilesResponse = commitDatasetFiles(createDatasetResponse);
					if (isDatasetCommited(commitDatasetFilesResponse) == true) {
						// Update dataset with UID
						DatasetParameter.setUID(getContext(),
								((JSONObject) createDatasetResponse.getJSONArray("datasetOutput").get(0))
										.getJSONObject("dataset").getString("uid"));
						DatasetParameter.commit(getContext());
						areFilesUploaded = Boolean.TRUE;
					} else {
						areFilesUploaded = Boolean.FALSE;
					}
				}
			} else {
				Constants.LOGGER.info(Messages.Dataset.NoFilesAvailableToUpload);
				areFilesUploaded = Boolean.FALSE;
			}
		} catch (Exception e) {
			Constants.LOGGER.error(Messages.Dataset.UploadFilesError + e.getMessage());
			areFilesUploaded = Boolean.FALSE;
			throw e;
		}
		return areFilesUploaded;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "UploadFiles";
	}

	// BEGIN EXTRA CODE

	/*
	 * commitDatasetFiles service would return updated JSONArray in response. It
	 * should contain UID of the dataset passed as input.
	 */
	private boolean isDatasetCommited(JSONObject commitDatasetFilesResponse) {
		boolean sucess = Boolean.FALSE;
		if (commitDatasetFilesResponse.getJSONArray("updated").length() > 0) {
			JSONObject updated = (JSONObject) commitDatasetFilesResponse.getJSONArray("updated").get(0);
			int length = updated.getString("uid").length();
			if (length > 0) {
				sucess = Boolean.TRUE;
			}
		} else {
			sucess = Boolean.FALSE;
		}
		return sucess;
	}

	/*
	 * createDataset service would return Dataset UID in datasetOutput
	 */
	private boolean isDatasetCreated(JSONObject createDatasetResponse) {
		boolean success = Boolean.FALSE;
		if (createDatasetResponse.getJSONArray("datasetOutput").length() > 0) {
			JSONObject datasetOutput = (JSONObject) createDatasetResponse.getJSONArray("datasetOutput").get(0);
			JSONObject dataset = datasetOutput.getJSONObject("dataset");
			int length = dataset.getString("uid").length();
			if (length > 0) {
				success = Boolean.TRUE;
			}
		} else {
			success = Boolean.FALSE;
		}
		return success;
	}

	/*
	 * Sequence of Substitution objects added to ArrayList is important. It needs to
	 * be in sequence as mentioned in JSON Template for commitDatasetFiles
	 */
	private ArrayList<String> createSubstitutionsFor_commitDatasetFiles(JSONObject createDatasetResponse)
			throws CoreException {
		ArrayList<String> Substitutions = new java.util.ArrayList<String>();
		JSONObject datasetOutput = (JSONObject) createDatasetResponse.getJSONArray("datasetOutput").get(0);
		JSONObject dataset = datasetOutput.getJSONObject("dataset");

		// uid
		Substitutions.add(checkForNull(dataset.getString("uid")));

		// DatasetType;
		Substitutions.add(checkForNull(DatasetParameter.getdataset_type(getContext())));

		// fileName
		Substitutions.add(checkForNull(this.DatasetParameter.getDocuments().get(0).getName()));

		// namedReferenceName
		Substitutions.add(checkForNull(NamedReference));

		// ticket
		Substitutions.add(checkForNull(getTicket(datasetOutput)));
		return Substitutions;
	}

	private String checkForNull(String input) {
		return input != null ? input : "";
	}

	/*
	 * retrieve ticket from createDataset response
	 */
	private String getTicket(JSONObject datasetOutput) {
		// ticket information
		JSONObject commitInfo = datasetOutput.getJSONArray("commitInfo").getJSONObject(0);
		JSONObject datasetFileTicketInfos = commitInfo.getJSONArray("datasetFileTicketInfos").getJSONObject(0);
		String ticket = datasetFileTicketInfos.getString("ticket");
		return ticket;
	}

	private ArrayList<String> createSubstitutionsFor_createDatasets() throws CoreException {
		ArrayList<String> Substitutions = new java.util.ArrayList<String>();

		// object_name
		Substitutions.add(checkForNull(DatasetParameter.getobject_name(getContext())));

		// DatasetType;
		Substitutions.add(checkForNull(DatasetParameter.getdataset_type(getContext())));

		// object_desc
		Substitutions.add(checkForNull(DatasetParameter.getobject_desc(getContext())));

		// namedReferenceName
		Substitutions.add(checkForNull(NamedReference));

		// fileName
		Substitutions.add(checkForNull(this.DatasetParameter.getDocuments().get(0).getName()));
		return Substitutions;
	}

	/*
	 * Upload File to FMS.
	 */
	private void uploadFiletoFMS(JSONObject createDatasetResponse)
			throws UnknownHostException, FSCException, CoreException {
		JSONObject datasetOutput = (JSONObject) createDatasetResponse.getJSONArray("datasetOutput").get(0);
		// get ticket
		String ticket = getTicket(datasetOutput);
		CommonsFSCWholeFileIOImpl fscFileIOImpl = FMSUtils.initializeFMS(getContext(), ConfigurationName);

		// open stream to upload file to FMS
		InputStream is = Core.getFileDocumentContent(getContext(),
				this.DatasetParameter.getDocuments().get(0).getMendixObject());

		// This API throws exception if file upload is unsuccessful.
		fscFileIOImpl.upload("TCM", null, ticket, is, this.DatasetParameter.getDocuments().get(0).getSize());
	}

	private static String createServiceInput(String jsonTemplate, ArrayList<String> substitutions) {
		for (int i = 0; i < substitutions.size(); i++) {
			String replacement = substitutions.get(i);
			String target = "{" + (i + 1) + "}";
			jsonTemplate = jsonTemplate.replace(target, replacement);
		}
		return jsonTemplate;
	}

	private JSONObject createDatasets() throws Exception {
		isTextFileType();
		// Create Dataset JSON Template
		String CreateDatasetJT = "\r\n" + "{\r\n" + "    \"input\": [\r\n" + "        {\r\n"
				+ "            \"clientId\": \"TCM\",\r\n" + "            \"container\": \"\",\r\n"
				+ "            \"datasetFileInfos\": [\r\n" + "                {\r\n"
				+ "                    \"fileName\": \"{5}\",\r\n"
				+ "                    \"namedReferenceName\": \"{4}\",\r\n" + "				     \"isText\":"
				+ isTextFileType + ",\r\n" + "                    \"clientId\": \"\",\r\n"
				+ "                    \"allowReplace\": false\r\n" + "                }\r\n" + "            ],\r\n"
				+ "            \"relationType\": \"\",\r\n" + "            \"name\": \"{1}\",\r\n"
				+ "            \"type\": \"{2}\",\r\n" + "            \"description\": \"{3}\",\r\n"
				+ "            \"datasetId\": \"\",\r\n" + "            \"datasetRev\": \"\",\r\n"
				+ "            \"toolUsed\": \"\",\r\n" + "            \"attrs\": [],\r\n"
				+ "            \"nrObjectInfos\": []\r\n" + "        }\r\n" + "    ]\r\n" + "}\r\n" + "\r\n" + "\r\n"
				+ "";

		// substitutions for createDatasets
		ArrayList<String> Substitutions = createSubstitutionsFor_createDatasets();
		CreateDatasetJT = createServiceInput(CreateDatasetJT, Substitutions);

		return TcConnection.callTeamcenterService(getContext(), Constants.OPERATION_CREATEDATASETS, CreateDatasetJT,
				new JSONObject(), ConfigurationName);
	}

	private JSONObject commitDatasetFiles(JSONObject createDatasetResponse) throws Exception {
		// commitDatasetFiles JSON Template
		String CommitDatasetFilesJT = "\r\n" + "{\r\n" + "	\"commitInput\": [{\r\n" + "			\"dataset\": {\r\n"
				+ "				\"uid\": \"{1}\",\r\n" + "				\"type\": \"{2}\"\r\n" + "			},\r\n"
				+ "			\"createNewVersion\": true,\r\n" + "			\"datasetFileTicketInfos\": [{\r\n"
				+ "					\"datasetFileInfo\": {\r\n" + "						\"clientId\": \"\",\r\n"
				+ "						\"fileName\": \"{3}\",\r\n"
				+ "						\"namedReferencedName\": \"{4}\",\r\n" + "						\"isText\":"
				+ isTextFileType + ",\r\n" + "						\"allowReplace\": false\r\n"
				+ "					},\r\n" + "					\"ticket\": \"{5}\"\r\n" + "				}\r\n"
				+ "			]\r\n" + "		}\r\n" + "	]\r\n" + "}\r\n" + "\r\n" + "\r\n" + "";

		// substitutions for commitDatasetFiles
		ArrayList<String> Substitutions = createSubstitutionsFor_commitDatasetFiles(createDatasetResponse);
		CommitDatasetFilesJT = createServiceInput(CommitDatasetFilesJT, Substitutions);
		return TcConnection.callTeamcenterService(getContext(), Constants.OPERATION_COMMITDATASETFILES,
				CommitDatasetFilesJT, new JSONObject(), ConfigurationName);
	}

	private Boolean isTextFileType = false;

	/**
	 * 
	 * @return true in case fileFormat is Text false in case of Binary
	 * @throws Exception
	 */
	private void isTextFileType() throws Exception {
		JSONObject getDatasetTypeInfoResponse = getDatasetTypeInfo();
		JSONArray infosJA = getDatasetTypeInfoResponse.getJSONArray("infos");
		JSONArray refInfosJA = infosJA.getJSONObject(0).getJSONArray("refInfos");
		String fileExtension = "*." + FilenameUtils.getExtension(this.DatasetParameter.getDocuments().get(0).getName());

		Boolean foundNamedReferenceAndFileExtension = false;
		for (int i = 0; i < refInfosJA.length(); i++) {
			JSONObject refInfoJO = refInfosJA.getJSONObject(i);
			if (refInfoJO.getString("referenceName").compareToIgnoreCase(NamedReference) == 0
					&& (refInfoJO.getString("fileExtension").compareToIgnoreCase(fileExtension) == 0
							|| refInfoJO.getString("fileExtension").compareTo("*") == 0)) {
				isTextFileType = (refInfoJO.getString("fileFormat").compareToIgnoreCase("TEXT") == 0 ? true : false);
				foundNamedReferenceAndFileExtension = true;
				break;
			}
		}
		if (!foundNamedReferenceAndFileExtension) {
			throw new Exception(createServiceInput(Messages.Dataset.InvalidNamedReferenceFileExtensionCombination,
					createSubstitutionsFor_NamedReferenceFileExtensionMismatch(fileExtension)));
		}
	}

	private ArrayList<String> createSubstitutionsFor_NamedReferenceFileExtensionMismatch(String fileExtension) {
		String datasetType = DatasetParameter.getdataset_type(getContext());
		ArrayList<String> Substitutions = new java.util.ArrayList<String>();
		Substitutions.add(datasetType);
		Substitutions.add(NamedReference);
		Substitutions.add(fileExtension);
		return Substitutions;
	}

	private ArrayList<String> createSubstitutionsFor_getDatasetTypeInfo() {
		String datasetType = DatasetParameter.getdataset_type(getContext());
		ArrayList<String> Substitutions = new java.util.ArrayList<String>();
		Substitutions.add(datasetType);
		return Substitutions;
	}

	private JSONObject getDatasetTypeInfo() throws Exception {
		// getAvailableTypesWithDisplayNames JSON Template
		String getDatasetTypeInfoJT = "{\r\n" + "        \"datasetTypeNames\": [\r\n" + "             \"{1}\"\r\n"
				+ "             ]\r\n" + "            }\r\n" + "    }";
		// substitutions for createDatasets
		ArrayList<String> Substitutions = createSubstitutionsFor_getDatasetTypeInfo();
		getDatasetTypeInfoJT = createServiceInput(getDatasetTypeInfoJT, Substitutions);

		return TcConnection.callTeamcenterService(getContext(), Constants.OPERATION_GETDATASETTYPEINFO,
				getDatasetTypeInfoJT, new JSONObject(), ConfigurationName);
	}
	// END EXTRA CODE
}
