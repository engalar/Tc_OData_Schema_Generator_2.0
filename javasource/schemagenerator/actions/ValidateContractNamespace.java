// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package schemagenerator.actions;

import java.util.List;
import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixIdentifier;
import com.mendix.webui.CustomJavaAction;
import com.mendix.systemwideinterfaces.core.IMendixObject;

public class ValidateContractNamespace extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __ContractNamespace;
	private schemagenerator.proxies.ContractNameSpaces ContractNamespace;

	public ValidateContractNamespace(IContext context, IMendixObject ContractNamespace)
	{
		super(context);
		this.__ContractNamespace = ContractNamespace;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.ContractNamespace = this.__ContractNamespace == null ? null : schemagenerator.proxies.ContractNameSpaces.initialize(getContext(), __ContractNamespace);

		// BEGIN USER CODE
		try
		{
			String contractNSName =  __ContractNamespace.getMember(getContext(), "ContractNameSpace").getValue(getContext()).toString();
			NamingRules namingRules = new NamingRules(getContext(), contractNSName);
			namingRules.executeAction();
			
			IMendixIdentifier identifier = (IMendixIdentifier) __ContractNamespace.getMember(getContext(), "SchemaGenerator.ContractNameSpaces_ODataSchema").getValue(getContext());
			IMendixObject OdataSchema = Core.retrieveId(getContext(), identifier);
						
			List<IMendixObject> contractNSList = Core.retrieveByPath(getContext(), OdataSchema, "SchemaGenerator.ContractNameSpaces_ODataSchema");
			
			for(IMendixObject contractNS : contractNSList)
			{
				
				if(contractNS.getId().equals(__ContractNamespace.getId()))
					continue;
				
				String Name = contractNS.getMember(getContext(), "ContractNameSpace").getValue(getContext()).toString();
				if(contractNSName.equals(Name))
					throw new Exception("Namespace cannot be created as " + contractNSName + " namespace already exists in the service.");
			}
		}
		catch(Exception e)
		{
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
		return "ValidateContractNamespace";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}