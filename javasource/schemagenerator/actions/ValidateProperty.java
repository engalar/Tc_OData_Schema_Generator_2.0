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
import com.mendix.webui.CustomJavaAction;
import schemagenerator.proxies.OData_Properties;
import com.mendix.systemwideinterfaces.core.IMendixObject;

public class ValidateProperty extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __Property;
	private schemagenerator.proxies.Properties Property;

	public ValidateProperty(IContext context, IMendixObject Property)
	{
		super(context);
		this.__Property = Property;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.Property = this.__Property == null ? null : schemagenerator.proxies.Properties.initialize(getContext(), __Property);

		// BEGIN USER CODE
		try {
			
			String propName = Property.getODataName();
			NamingRules validName = new NamingRules(getContext(), propName);
			validName.executeAction();
			
			if(Property.getODataTypeValue().getCaption().equals(OData_Properties.Reference.getCaption()) 
					|| Property.getODataTypeValue().getCaption().equals(OData_Properties.Relation.getCaption()))
			{
				String referenceValue = Property.getreferenceType();
				if(referenceValue == null || referenceValue.isEmpty())
					throw new Exception("Please select valid reference object for property " + Property.getTcName());
			}
			
			IMendixObject odataObj = Core.retrieveByPath(getContext(), Property.getMendixObject(), "SchemaGenerator.Properties_OdataObject").get(0);
			
			List<IMendixObject> propList = Core.retrieveByPath(getContext(), odataObj, "SchemaGenerator.Properties_OdataObject");
			for(IMendixObject prop : propList)
			{
				if(!Property.getTcName().equals(prop.getValue(getContext(), "TcName")))
				{
					String name = prop.getValue(getContext(), "ODataName");
					if(name.equalsIgnoreCase(propName))
						throw new Exception("Duplicate property name " + propName + " for the Object " + odataObj.getValue(getContext(), "ODataName"));
				}
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
		return "ValidateProperty";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
