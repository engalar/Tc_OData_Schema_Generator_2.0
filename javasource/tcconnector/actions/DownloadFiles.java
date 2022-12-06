// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package tcconnector.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import com.teamcenter.fms.servercache.FSCException;
import com.teamcenter.fms.servercache.proxy.CommonsFSCWholeFileIOImpl;
import tcconnector.foundation.JModelObject;
import tcconnector.foundation.JServiceData;
import tcconnector.foundation.TcConnection;
import tcconnector.foundation.exceptions.NotLoadedExcpetion;
import tcconnector.internal.foundation.Constants;
import tcconnector.internal.foundation.Messages;
import tcconnector.proxies.FileDocument;
import tcconnector.proxies.TeamcenterConfiguration;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.thirdparty.org.json.JSONArray;
import com.mendix.thirdparty.org.json.JSONObject;

/**
 * SOA URL: 
 * Core-2006-03-DataManagement/getProperties
 * Core-2006-03-FileManagement/getFileReadTickets
 * 
 * Downloads all the files associated with the Dataset passed as an input parameter. 
 * 
 * Input -
 * - Dataset object having UID
 * 
 * Output -
 * - Action returns True or False in case of success and failure respectively.
 * - Input object's Dataset.Documents association will be updated to point to downloaded files if any.
 * 
 */
public class DownloadFiles extends CustomJavaAction<java.lang.Boolean>
{
	private IMendixObject __DatasetParameter;
	private tcconnector.proxies.Dataset DatasetParameter;
	private java.lang.String ConfigurationName;

	public DownloadFiles(IContext context, IMendixObject DatasetParameter, java.lang.String ConfigurationName)
	{
		super(context);
		this.__DatasetParameter = DatasetParameter;
		this.ConfigurationName = ConfigurationName;
	}

	@java.lang.Override
	public java.lang.Boolean executeAction() throws Exception
	{
		this.DatasetParameter = this.__DatasetParameter == null ? null : tcconnector.proxies.Dataset.initialize(getContext(), __DatasetParameter);

		// BEGIN USER CODE
		if(ConfigurationName == null || ConfigurationName.equals("") || ConfigurationName.isEmpty())
		{
			ConfigurationName = tcconnector.proxies.microflows.Microflows.retrieveConfigNameFromSingleActiveConfiguration(getContext());
			if(ConfigurationName.isEmpty())
				return null;
		}
		
		boolean areFilesDownloaded = Boolean.FALSE;
		try
		{
			JSONObject ref_list = retrieveDataset_ref_list();
			List<IMendixObject> files = retrieveFileListUIDs( ref_list );
	
			if( files.size() > 0)
			{
				JSONObject body = generateBody_getFileReadTickets(files);
				JSONObject getFileReadTicketsResponse = getFileReadTickets( body.toString(), ConfigurationName );
				
				ArrayList<FileDocument> documents = downloadFilesFromFMS(getFileReadTicketsResponse);
				DatasetParameter.setDocuments(getContext(), documents);
				areFilesDownloaded = true;
			}
			else
			{
				Constants.LOGGER.info( Messages.Dataset.NoFilesAvailableToDownload );
			}
		} catch (Exception e) {
			Constants.LOGGER.error( Messages.Dataset.DownloadFilesError + e.getMessage());
			areFilesDownloaded = false;
			throw e;
		}
		return areFilesDownloaded;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "DownloadFiles";
	}

	// BEGIN EXTRA CODE

	private String checkForNull(String input)
	{
		return input.length()>0 ? input : "";
	}

	private ArrayList<String> createSubstitutionsFor_getProperties() throws CoreException {
		ArrayList<String> Substitutions = new java.util.ArrayList<String>();
		Substitutions.add(checkForNull(DatasetParameter.getUID(getContext())));
		return Substitutions;
	}

	/*
	 * Retrieve ImanFile UID & File name
	 */
	private List<IMendixObject> retrieveFileListUIDs(JSONObject file_listResponse) {
		List<IMendixObject> iMendixObjectList = new ArrayList<IMendixObject>();

		JServiceData object = (JServiceData)file_listResponse;
		List<JModelObject> plainObjects = object.getPlainObjects();
		
		for ( int i = 0 ; i < plainObjects.size() ; i ++ )
		{
			JModelObject dataSet = plainObjects.get(i);
			if( dataSet != null && dataSet.getUID().equals( DatasetParameter.getUID(getContext()) ))
			{
				try {
					List<String> uiValues = dataSet.getPropertyValues("ref_list");
					List<JModelObject> dbValues = dataSet.getPropertyValueAsModelObjects("ref_list");
					for( int j = 0 ; j < dbValues.size() ; j ++ )
					{
						IMendixObject datasetType = Core.instantiate(getContext(), tcconnector.proxies.Pair.entityName );
						datasetType.setValue(getContext(), "Name", uiValues.get(j));
						datasetType.setValue(getContext(), "Value", dbValues.get(j).getUID());
						iMendixObjectList.add(datasetType);
					}
				} catch (NotLoadedExcpetion e) {
					/*
					 * In catch means there is empty ref_list. This is valid scenario and ignore this exception 
					 */
				}
			}
		}
		
		return iMendixObjectList;
	}

	private ArrayList<FileDocument> downloadFilesFromFMS(JSONObject getFileReadTicketsResponse) 
			throws FSCException, IOException {
		ArrayList<FileDocument> documents = new java.util.ArrayList<FileDocument>();

		/*
		 * Initialize FMS
		 */
		String[] fmsURLs = retrieveFMSURLs();
		CommonsFSCWholeFileIOImpl fscFileIOImpl = initializeFMS( fmsURLs );
		/*
		 * Parse getFileTicketsResponse and get ticket(s) information from that.
		 */

		JSONArray tickets = getFileReadTicketsResponse.getJSONArray("tickets");
		/*
		 *  getFileReadTickets returns response having 2 arrays under 'tickets'.
		 *  First array would contain ImanFile objects
		 *  Second array contains list of corresponding tickets
		 *  --------------------------------------------------------------------
		 * 	{
		 *		tickets:
		 *		[
		 *			["ImanFileObjects"],
		 *			["Tickets"]
		 *		],
		 *		serviceData: "IServiceData"
		 *	}
		 *	--------------------------------------------------------------------
		 */
		List<List<String>> imanFileObjectList = new ArrayList<List<String>>();
		for( int i = 0 ; i < tickets.getJSONArray(1).length() ; i++ )
		{
			List<String> fileTciketAndNameList = new ArrayList<String>();
			JModelObject ImanFileObject = (JModelObject)tickets.getJSONArray(0).getJSONObject( i );
			String fileTicket = tickets.getJSONArray(1).getString( i );
			String original_file_name = ImanFileObject.getPropertyValueAsString("original_file_name");
			fileTciketAndNameList.add(fileTicket);
			fileTciketAndNameList.add(original_file_name);
			imanFileObjectList.add(fileTciketAndNameList);
	    }
		
		if(imanFileObjectList.size()>0)	
		{

			imanFileObjectList.parallelStream().forEach(s -> { 
				try 
				{
					documents.add(downloadFiles(s,fmsURLs,fscFileIOImpl));
				} 
				catch (Exception e)
				{
					Constants.LOGGER.error( Messages.Dataset.DownloadFilesError + e.getMessage());
				}
	        });
		}	
		return documents;
	}
	 
	private FileDocument downloadFiles(List<String> fileTciketAndNameList,String[]  fmsURLs,CommonsFSCWholeFileIOImpl fscFileIOImpl) throws Exception
	{
		String fileTicket = fileTciketAndNameList.get(0);
		String original_file_name = fileTciketAndNameList.get(1);
		
		/*
		 * Create FileDocument object and stream to get file from FMS
		 */
		IMendixObject documentObject = Core.instantiate(getContext(), tcconnector.proxies.FileDocument.entityName);
		FileDocument document = FileDocument.initialize(getContext(), documentObject);
		document.setName(original_file_name);
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		/*
		 * get stream from FMS
		 */
		fscFileIOImpl.download("TCM", fmsURLs, fileTicket, os);
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		Core.storeFileDocumentContent(getContext(), documentObject, is);
		os.close();
		is.close();
		return document;
	}

	private CommonsFSCWholeFileIOImpl initializeFMS(String[] fmsURLs) throws UnknownHostException, FSCException {
		CommonsFSCWholeFileIOImpl fscFileIOImpl;
		fscFileIOImpl = new CommonsFSCWholeFileIOImpl();
		final InetAddress clientIP = InetAddress.getLocalHost();
		fscFileIOImpl.init(clientIP.getHostAddress(), fmsURLs, fmsURLs);		
		return fscFileIOImpl;
	}

	/*
	 * retrieve FMS URL from active TC configuration
	 */
	private String[] retrieveFMSURLs() {
		TeamcenterConfiguration config = tcconnector.proxies.microflows.Microflows.retrieveTeamcenterConifgurationByName((IContext)getContext(), ConfigurationName); 
		String FMSURL = config.getFMSURL(getContext());
		String[] bootstrapURLs = FMSURL.split(",");
		return bootstrapURLs;
	}

	/*
	 * get ImanFile UIDs from dataset response
	 */
	private JSONObject generateBody_getFileReadTickets(List<IMendixObject> ref_list) {
		JSONObject body = new JSONObject();
		JSONArray filesVec = new JSONArray();
		for(int cnt=0; cnt < ref_list.size(); ++cnt)
		{
			JSONObject file = new JSONObject();
			IMendixObject ImanFileObject = ref_list.get(cnt);
			file.put("uid",ImanFileObject.getMember(getContext(), "Value").getValue(getContext()));
			filesVec.put(file);
		}
		body.put("files",filesVec);
		return body;
	}
	
	private static String createServiceInput(String jsonTemplate, ArrayList<String> substitutions)
	{
		for(int i=0; i<substitutions.size(); i++)
		{
			String replacement 	= substitutions.get(i);
			String target       = "{"+(i+1)+"}";
			jsonTemplate 		= jsonTemplate.replace(target, replacement);
		}
		return jsonTemplate;
	}
	
	/*
	 * Call TC Service and get ref_list for dataset.
	 */
	private JSONObject retrieveDataset_ref_list() throws Exception {
		// Create getProperties ref_list JSON template
		String getPropertiesJT = "\r\n" + 
				"{\r\n" + 
				"    \"objects\": [\r\n" + 
				"        \"{1}\"\r\n" + 
				"    ],\r\n" + 
				"    \"attributes\": [\r\n" + 
				"        \"ref_list\"\r\n" + 
				"    ]\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"";
		
		// substitutions for createDatasets
		ArrayList<String> Substitutions = createSubstitutionsFor_getProperties();
		getPropertiesJT = createServiceInput( getPropertiesJT, Substitutions );

		return TcConnection.callTeamcenterService(getContext(), Constants.OPERATION_GETPROPERTIES , getPropertiesJT, new JSONObject(), ConfigurationName);
	}
	
	/*
	 * Call TC Service and get tickets of ImanFile(s)
	 */
	private JSONObject getFileReadTickets(String getFileReadTicketsJT, String configurationName) throws Exception {
		JSONObject emptyPolicy = new JSONObject("{\r\n" + 
				"    \"types\": [\r\n" + 
				"        {\r\n" + 
				"            \"name\": \"ImanFile\",\r\n" + 
				"            \"properties\": [\r\n" + 
				"                {\r\n" + 
				"                    \"name\": \"original_file_name\"\r\n" + 
				"                }\r\n" + 
				"            ]\r\n" + 
				"        }\r\n" + 
				"    ]\r\n" + 
				"}");
		return TcConnection.callTeamcenterService(getContext(), Constants.OPERATION_GETFILEREADTICKETS , getFileReadTicketsJT, emptyPolicy, configurationName);
	}
	// END EXTRA CODE
}
