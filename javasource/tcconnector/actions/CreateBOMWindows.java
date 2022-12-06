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
import tcconnector.proxies.CreateBomWindowResponse;
import com.mendix.systemwideinterfaces.core.IMendixObject;

/**
 * SOA URL:
 * Cad-2007-01-StructureManagement/createBOMWindows
 * 
 * Description:
 * This actions Creates a BOMWindow and sets the input Item Revision as the top line
 * 
 * Returns:
 * An entity of type CreateBomWindowResponse. Top line  can be retrieved using association TcConnector.bomLine__BOMWindow. Partial errors can be retrieved using association TcConnector.ResponseData/TcConnector.PartialErrors.
 */
public class CreateBOMWindows extends CustomJavaAction<IMendixObject>
{
	private IMendixObject __InputData;
	private tcconnector.proxies.CreateBomWindowInput InputData;
	private java.lang.String BusinessObjectMappings;
	private java.lang.String ConfigurationName;

	public CreateBOMWindows(IContext context, IMendixObject InputData, java.lang.String BusinessObjectMappings, java.lang.String ConfigurationName)
	{
		super(context);
		this.__InputData = InputData;
		this.BusinessObjectMappings = BusinessObjectMappings;
		this.ConfigurationName = ConfigurationName;
	}

	@java.lang.Override
	public IMendixObject executeAction() throws Exception
	{
		this.InputData = this.__InputData == null ? null : tcconnector.proxies.CreateBomWindowInput.initialize(getContext(), __InputData);

		// BEGIN USER CODE
		CreateBomWindowResponse response = new CreateBomWindowResponse(getContext());
		response = (CreateBomWindowResponse)TcConnection.callTeamcenterService(  getContext(), 
													Constants.OPERATION_CREATEBOMWINDOWS2, InputData.getMendixObject(), 
													response, SERVICE_OPERATION_MAP, BusinessObjectMappings,ConfigurationName);
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
		return "CreateBOMWindows";
	}

	// BEGIN EXTRA CODE
	private static final String SERVICE_OPERATION_MAP  = "OperationMapping/Cad/2007-01/StructureManagement/createBoMWindows.json";
	// END EXTRA CODE
}
