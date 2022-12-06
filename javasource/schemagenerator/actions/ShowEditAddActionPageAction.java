// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package schemagenerator.actions;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IFeedback;
import com.mendix.thirdparty.org.json.JSONObject;
import com.mendix.webui.CustomJavaAction;
import com.mendix.webui.FeedbackHelper;
import schemagenerator.proxies.*;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ShowEditAddActionPageAction extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __odataAction;
	private schemagenerator.proxies.OdataActions odataAction;

	public ShowEditAddActionPageAction(IContext context, IMendixObject odataAction)
	{
		super(context);
		this.__odataAction = odataAction;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.odataAction = this.__odataAction == null ? null : schemagenerator.proxies.OdataActions.initialize(getContext(), __odataAction);

		// BEGIN USER CODE
		if (odataAction.getName().contains(".")) {
			odataAction.setName(odataAction.getName().split("[.]")[1]);
			odataAction.commit();
		}

		// Get the Operation Mapping File from Disk
		OperationMappingFile opMappingFile = odataAction.getOperationMappingFile_OdataActions();
		String operationName = opMappingFile.getOperationName();
		String[] operationNameKeyValue = operationName.split("[/]");
		String[] operationNameKeyParts = operationNameKeyValue[0].split("[-]");
		JSONObject rootObj = null;
		if(operationNameKeyValue.length == 2 && operationNameKeyParts.length == 4) {
			String path = "ActionMapping" + "/" + operationNameKeyParts[0] + "/" + operationNameKeyParts[1] + "-" + operationNameKeyParts[2] + "/" + operationNameKeyParts[3] + "/" + operationNameKeyValue[1] + ".json";
			File serviceListFile = new File(Core.getConfiguration().getResourcesPath(), path);
			rootObj = new JSONObject(FileUtils.readFileToString(serviceListFile));
		}
		else {
			throw new Exception("Operation Name Malformed");
		}

		View viewForRequest = new View(getContext());
		View viewForResponse = new View(getContext());

		SelectionTreeNode requestNode = new SelectionTreeNode(getContext());
		requestNode.setRoot(true);
		requestNode.setName("Request");
		requestNode.setSelectionTreeNode_View(viewForRequest);
		requestNode.setSelectionTreeNode_Children(
				TcODataSchemaHelper.getRecursiveTreeEdit(
						getContext(),
						viewForRequest,
						rootObj.getJSONObject("OperationInput").toString(),
						opMappingFile.getJSONModified()
				)
		);
		requestNode.commit();

		SelectionTreeNode responseNode = new SelectionTreeNode(getContext());
		responseNode.setRoot(true);
		responseNode.setName("Response");
		responseNode.setSelectionTreeNode_View(viewForResponse);
		JSONObject operationResponseJsonObject = rootObj.optJSONObject("OperationResponse");
		String operationResponseString = "";
		if(operationResponseJsonObject != null)
		{
			operationResponseString = operationResponseJsonObject.toString();
			if(operationResponseString.equalsIgnoreCase("{}"))
			{
				operationResponseJsonObject = new JSONObject();
				operationResponseJsonObject.put("output", "void");
				operationResponseString = operationResponseJsonObject.toString();
			}
		}
		else
		{
			operationResponseString = rootObj.optString("OperationResponse");
			operationResponseJsonObject = new JSONObject();
			operationResponseJsonObject.put("output", operationResponseString);
			operationResponseString = operationResponseJsonObject.toString();
		}
		responseNode.setSelectionTreeNode_Children(
				TcODataSchemaHelper.getRecursiveTreeEdit(
						getContext(),
						viewForResponse,
						operationResponseString,
						opMappingFile.getJSONModified()
				)
		);
		responseNode.commit();
		viewForRequest.commit();
		viewForResponse.commit();
		opMappingFile.setOperationMappingFile_View_Request(viewForRequest);
		opMappingFile.setOperationMappingFile_View_Response(viewForResponse);
		opMappingFile.commit();
		odataAction.commit();

		FeedbackHelper.addRefreshClass(getContext(), OdataActions.getType());
		FeedbackHelper.addOpenFormFeedback(
			getContext(),
			"SchemaGenerator/AddAction__",
			"Edit Action",
			IFeedback.FormTarget.CONTENT,
			odataAction.getMendixObject().getId(),
			null
		);

		return null;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "ShowEditAddActionPageAction";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
