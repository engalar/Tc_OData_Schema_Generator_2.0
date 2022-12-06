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
import schemagenerator.proxies.ContractNameSpaces;
import schemagenerator.proxies.externalReferences;
import com.mendix.systemwideinterfaces.core.IMendixObject;

public class ValidateRefNamespaceToDelete extends CustomJavaAction<java.lang.Void>
{
	private java.util.List<IMendixObject> __ContractNameSpacesList;
	private java.util.List<schemagenerator.proxies.ContractNameSpaces> ContractNameSpacesList;
	private java.util.List<IMendixObject> __externalReferenceList;
	private java.util.List<schemagenerator.proxies.externalReferences> externalReferenceList;

	public ValidateRefNamespaceToDelete(IContext context, java.util.List<IMendixObject> ContractNameSpacesList, java.util.List<IMendixObject> externalReferenceList)
	{
		super(context);
		this.__ContractNameSpacesList = ContractNameSpacesList;
		this.__externalReferenceList = externalReferenceList;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.ContractNameSpacesList = java.util.Optional.ofNullable(this.__ContractNameSpacesList)
			.orElse(java.util.Collections.emptyList())
			.stream()
			.map(__ContractNameSpacesListElement -> schemagenerator.proxies.ContractNameSpaces.initialize(getContext(), __ContractNameSpacesListElement))
			.collect(java.util.stream.Collectors.toList());

		this.externalReferenceList = java.util.Optional.ofNullable(this.__externalReferenceList)
			.orElse(java.util.Collections.emptyList())
			.stream()
			.map(__externalReferenceListElement -> schemagenerator.proxies.externalReferences.initialize(getContext(), __externalReferenceListElement))
			.collect(java.util.stream.Collectors.toList());

		// BEGIN USER CODE
		List<String> AllBaseTypes = new ArrayList<String>();
		List<String> namespaceList = new ArrayList<String>();
		for(ContractNameSpaces contractNS: ContractNameSpacesList)
		{
			if(!contractNS.getInheritedObject())
			{
				List<IMendixObject> objList = Core.retrieveByPath(getContext(), contractNS.getMendixObject(), "SchemaGenerator.OdataObject_ContractNameSpaces");
				for(IMendixObject obj: objList)
				{
					Object baseType = obj.getMember(getContext(), "BaseType").getValue(getContext());
					if(baseType != null)
						AllBaseTypes.add(baseType.toString());
				}
			}
			namespaceList.add(contractNS.getContractNameSpace());
		}
		
		for(externalReferences externalReference: externalReferenceList)
		{
			List<IMendixObject> namespaces = Core.retrieveByPath(getContext(), externalReference.getMendixObject(), "SchemaGenerator.NameSpaces_externalReferences");
			for(IMendixObject namespace: namespaces)
			{
				int index = namespaceList.indexOf(namespace.getMember(getContext(), "name").getValue(getContext()).toString());
				if(index == -1)
					continue;
				ContractNameSpaces NS_contractNS = ContractNameSpacesList.get(index);
				List<IMendixObject> objList = Core.retrieveByPath(getContext(), NS_contractNS.getMendixObject(), "SchemaGenerator.OdataObject_ContractNameSpaces");
				for(IMendixObject obj: objList)
				{
					String odataName = obj.getMember(getContext(), "ODataName").getValue(getContext()).toString();
					if(AllBaseTypes.contains(odataName))
						throw new Exception("Reference cannot be deleted because the object "+ odataName + " is refered in current service.");
				}
			}
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
		return "ValidateRefNamespaceToDelete";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
