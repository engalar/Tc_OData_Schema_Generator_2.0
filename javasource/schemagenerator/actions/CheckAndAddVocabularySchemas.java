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
import schemagenerator.proxies.NameSpaces;
import schemagenerator.proxies.externalReferences;
import com.mendix.systemwideinterfaces.core.IMendixObject;

public class CheckAndAddVocabularySchemas extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __Schema;
	private schemagenerator.proxies.ODataSchema Schema;

	public CheckAndAddVocabularySchemas(IContext context, IMendixObject Schema)
	{
		super(context);
		this.__Schema = Schema;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.Schema = this.__Schema == null ? null : schemagenerator.proxies.ODataSchema.initialize(getContext(), __Schema);

		// BEGIN USER CODE
		List<IMendixObject> externalReferenceList = Core.retrieveByPath(getContext(), Schema.getMendixObject(), "SchemaGenerator.externalReferences_ODataSchema");
		boolean capabilitiesRefenceExsists = false;
		for (IMendixObject refrence : externalReferenceList) {
			externalReferences refrencObject = externalReferences.initialize(getContext(), refrence);
			if (refrencObject.geturi(getContext()).equals(CAPABLITIES_URI)) {
				capabilitiesRefenceExsists = true;
			}
		}
		if(!capabilitiesRefenceExsists) {
			
			externalReferences newReference = externalReferences.initialize(
				getContext(), 
				Core.instantiate(
					getContext(),
					externalReferences.getType()
				)
			);

			NameSpaces newNameSpace = NameSpaces.initialize(
				getContext(),
				Core.instantiate(
					getContext(),
					NameSpaces.getType()
				)
			);

			newReference.seturi(getContext(), CAPABLITIES_URI);
			newReference.setexternalReferences_ODataSchema(getContext(), Schema);
			newNameSpace.setname(getContext(), CAPABLITIES_NAME);
			newNameSpace.setalias(getContext(), CAPABLITIES_ALIAS);
			newNameSpace.setNameSpaces_externalReferences(getContext(), newReference);
			Core.commit(getContext(), newReference.getMendixObject());
			Core.commit(getContext(), newNameSpace.getMendixObject());

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
		return "CheckAndAddVocabularySchemas";
	}

	// BEGIN EXTRA CODE

	public static final String CAPABLITIES_URI = "http://localhost/Org.OData.Capabilities.V1/v4.0/Org.OData.Capabilities.V1.xml";
	public static final String CAPABLITIES_NAME = "Org.OData.Capabilities.V1";
	public static final String CAPABLITIES_ALIAS = "Capabilities";
	
	// END EXTRA CODE
}
