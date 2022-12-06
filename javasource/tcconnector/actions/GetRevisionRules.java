// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package tcconnector.actions;

import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import tcconnector.foundation.TcConnection;
import tcconnector.internal.foundation.Constants;
import tcconnector.proxies.GetRevisionRulesResponse;
import com.mendix.systemwideinterfaces.core.IMendixObject;

/**
 * SOA URL:
 * Cad-2007-01-StructureManagement/getRevisionRules
 * 
 * Description: 
 * The GetRevisionRules service gets all the persistent revision rules in the database.
 * 
 * Returns:
 * GetRevisionRulesResponse which contains RevisionRuleInfo. RevisionRuleInfo has the revision rule. Partial errors can be retrieved using association TcConnector.ResponseData/TcConnector.PartialErrors.
 */
public class GetRevisionRules extends CustomJavaAction<IMendixObject>
{
	private java.lang.String BusinessObjectMappings;
	private java.lang.String ConfigurationName;

	public GetRevisionRules(IContext context, java.lang.String BusinessObjectMappings, java.lang.String ConfigurationName)
	{
		super(context);
		this.BusinessObjectMappings = BusinessObjectMappings;
		this.ConfigurationName = ConfigurationName;
	}

	@java.lang.Override
	public IMendixObject executeAction() throws Exception
	{
		// BEGIN USER CODE
		IMendixObject jsonInputObj = null;
		GetRevisionRulesResponse response = new GetRevisionRulesResponse(getContext());
		response = (GetRevisionRulesResponse) TcConnection.callTeamcenterService(getContext(),
				Constants.OPERATION_GETREVISIONRULES, jsonInputObj, response, SERVICE_OPERATION_MAP,
				BusinessObjectMappings, ConfigurationName);
		return response.getMendixObject();
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "GetRevisionRules";
	}

	// BEGIN EXTRA CODE
	private static final String SERVICE_OPERATION_MAP = "OperationMapping/Cad/2007-01/StructureManagement/GetRevisionRules.json";
	// END EXTRA CODE
}
