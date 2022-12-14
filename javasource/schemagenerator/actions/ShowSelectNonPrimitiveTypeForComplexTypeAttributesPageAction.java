// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package schemagenerator.actions;

import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IFeedback;
import com.mendix.webui.CustomJavaAction;
import com.mendix.webui.FeedbackHelper;
import schemagenerator.proxies.ComplexTypeAttribute;
import schemagenerator.proxies.ContractNameSpaces;
import schemagenerator.proxies.OdataComplexType;
import schemagenerator.proxies.View;
import com.mendix.systemwideinterfaces.core.IMendixObject;

public class ShowSelectNonPrimitiveTypeForComplexTypeAttributesPageAction extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __complexTypeAttribute;
	private schemagenerator.proxies.ComplexTypeAttribute complexTypeAttribute;

	public ShowSelectNonPrimitiveTypeForComplexTypeAttributesPageAction(IContext context, IMendixObject complexTypeAttribute)
	{
		super(context);
		this.__complexTypeAttribute = complexTypeAttribute;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.complexTypeAttribute = this.__complexTypeAttribute == null ? null : schemagenerator.proxies.ComplexTypeAttribute.initialize(getContext(), __complexTypeAttribute);

		// BEGIN USER CODE
		IMendixObject complexType = Core.retrieveByPath(getContext(), __complexTypeAttribute, ComplexTypeAttribute.MemberNames.ComplexTypeAttribute_OdataComplexType.toString()).get(0);
		IMendixObject cns = Core.retrieveByPath(getContext(), complexType, OdataComplexType.MemberNames.OdataComplexType_ContractNameSpaces.toString()).get(0);
		IMendixObject schema = Core.retrieveByPath(getContext(), cns, ContractNameSpaces.MemberNames.ContractNameSpaces_ODataSchema.toString()).get(0);
		View view = new View(getContext());
		Core.commit(getContext(), TcODataSchemaHelper.getObjectSelectionTreeForSchemaGenreation(getContext(), schema, true, true, false, __complexTypeAttribute.getId(), view));
		view.setViewForObjectType(getContext(), complexTypeAttribute.entityName);
		complexTypeAttribute.setComplexTypeAttribute_View(getContext(), view);
		view.commit(getContext());
		FeedbackHelper.addOpenFormFeedback(
			getContext(),
			"SchemaGenerator/SelectNonPrimitiveTypeForComplexTypeAttributes",
			IFeedback.FormTarget.MODAL,
			view.getMendixObject().getId(),
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
		return "ShowSelectNonPrimitiveTypeForComplexTypeAttributesPageAction";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
