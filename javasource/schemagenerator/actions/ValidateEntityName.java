// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package schemagenerator.actions;

import java.util.ArrayList;
import java.util.List;
import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import com.mendix.systemwideinterfaces.core.IMendixObject;

public class ValidateEntityName extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __ContractNameSpaces;
	private schemagenerator.proxies.ContractNameSpaces ContractNameSpaces;

	public ValidateEntityName(IContext context, IMendixObject ContractNameSpaces)
	{
		super(context);
		this.__ContractNameSpaces = ContractNameSpaces;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.ContractNameSpaces = this.__ContractNameSpaces == null ? null : schemagenerator.proxies.ContractNameSpaces.initialize(getContext(), __ContractNameSpaces);

		// BEGIN USER CODE
		try {
			List<IMendixObject> odataObjList = Core.retrieveByPath(getContext(), __ContractNameSpaces, "SchemaGenerator.OdataObject_ContractNameSpaces");
			
			for(IMendixObject odataObj : odataObjList)
			{
				String entityNameNS = odataObj.getMember(getContext(), "ODataName").getValue(getContext()).toString();
				String entityName = entityNameNS.split("[.]")[1];
				
				List<IMendixObject> odataObjListTemp = new ArrayList<IMendixObject>();
				odataObjListTemp.addAll(odataObjList);
				odataObjListTemp.remove(odataObj);
				for(IMendixObject odataObjTemp : odataObjListTemp)
				{
					String entityNameNSTemp = odataObjTemp.getMember(getContext(), "ODataName").getValue(getContext()).toString();
					String entityNameTemp = entityNameNSTemp.split("[.]")[1];
					if(entityName.toLowerCase().equals(entityNameTemp.toLowerCase()))
						throw new Exception("Same Odata Name for the objects " + entityNameNS + " and "  + entityNameNSTemp + ".\r\n" + "Please give a unique Odata Name.");
				}
				 NamingRules namingRules = new NamingRules(getContext(), entityName);
				 namingRules.executeAction();
			}
		} catch (Exception e) {
			throw e;
		}
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
		return "ValidateEntityName";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}