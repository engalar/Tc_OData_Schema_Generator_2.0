// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package schemagenerator.actions;

import java.util.Iterator;
import java.util.List;
import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import com.mendix.webui.FeedbackHelper;
import schemagenerator.proxies.ContractNameSpaces;
import schemagenerator.proxies.OdataActions;
import schemagenerator.proxies.OdataObject;
import schemagenerator.proxies.OperationMappingFile;
import schemagenerator.proxies.ReturnType;
import schemagenerator.proxies.View;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.thirdparty.org.json.JSONArray;
import com.mendix.thirdparty.org.json.JSONException;
import com.mendix.thirdparty.org.json.JSONObject;

public class SaveNewActionAction__ extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __odataAction;
	private schemagenerator.proxies.OdataActions odataAction;

	public SaveNewActionAction__(IContext context, IMendixObject odataAction)
	{
		super(context);
		this.__odataAction = odataAction;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.odataAction = this.__odataAction == null ? null : schemagenerator.proxies.OdataActions.initialize(getContext(), __odataAction);

		// BEGIN USER CODE
		List<IMendixObject> cnsList = Core.retrieveByPath(getContext(), __odataAction, OdataActions.MemberNames.OdataActions_ContractNameSpaces.toString());
		IMendixObject cns = null;
		if (cnsList.isEmpty()) {
			IMendixObject odataObject = Core.retrieveByPath(getContext(), __odataAction, OdataActions.MemberNames.OdataActions_OdataObject.toString()).get(0);
			cns = Core.retrieveByPath(getContext(), odataObject, OdataObject.MemberNames.OdataObject_ContractNameSpaces.toString()).get(0);
		}
		else {
			cns = cnsList.get(0);
		}
		String contractName = cns.getValue(getContext(), ContractNameSpaces.MemberNames.ContractNameSpace.toString()).toString();
		odataAction.setName(getContext(), contractName + "." + odataAction.getName(getContext()));
		if (odataAction.getOdataActions_OdataObject(getContext()) != null) {
			odataAction.setOdataActions_ContractNameSpaces(null);
			odataAction.setIsBound(getContext(), true);
		}
		else {
			odataAction.setIsBound(getContext(), false);
        }
        
        ReturnType returnType = odataAction.getReturnType_OdataActions(getContext());
        if (returnType.getBaseType().equalsIgnoreCase("Void")) {
            odataAction.setReturnType_OdataActions(null);
        }
		
		//Mapping file processing for action parameteres
        OperationMappingFile opMappingFile = odataAction.getOperationMappingFile_OdataActions();
        View viewRequest = opMappingFile.getOperationMappingFile_View_Request();
        View viewResponse = opMappingFile.getOperationMappingFile_View_Response();
        JSONObject mainJson = new JSONObject();
        mainJson.put("OperationInput", TcODataSchemaHelper.getJsonFromTree(getContext(), viewRequest));
        mainJson.put("OperationOutput", TcODataSchemaHelper.getJsonFromTree(getContext(), viewResponse));
        
        mainJson.put("ServiceOperation", opMappingFile.getOperationName());
        opMappingFile.setJSONModified(mainJson.toString());
        opMappingFile.commit();
		
		odataAction.commit();
		FeedbackHelper.addCloseCallerFeedback(getContext());
		FeedbackHelper.addRefreshClass(getContext(), OdataActions.getType());
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
		return "SaveNewActionAction__";
	}

	// BEGIN EXTRA CODE
   
	// END EXTRA CODE
}
