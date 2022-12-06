// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package schemagenerator.actions;

import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IFeedback;
import com.mendix.webui.FeedbackHelper;
import schemagenerator.proxies.ComplexTypeAttribute;
import schemagenerator.proxies.ContractNameSpaces;
import schemagenerator.proxies.OdataComplexType;
import schemagenerator.proxies.View;

public class ShowSelectNonPrimitiveTypeForComplexTypePageAction extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __complexType;
	private schemagenerator.proxies.OdataComplexType complexType;

	public ShowSelectNonPrimitiveTypeForComplexTypePageAction(IContext context, IMendixObject complexType)
	{
		super(context);
		this.__complexType = complexType;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.complexType = this.__complexType == null ? null : schemagenerator.proxies.OdataComplexType.initialize(getContext(), __complexType);

		// BEGIN USER CODE
		IMendixObject cns = Core.retrieveByPath(getContext(), __complexType, OdataComplexType.MemberNames.OdataComplexType_ContractNameSpaces.toString()).get(0);
		IMendixObject schema = Core.retrieveByPath(getContext(), cns, ContractNameSpaces.MemberNames.ContractNameSpaces_ODataSchema.toString()).get(0);
		View view = new View(getContext());
		Core.commit(getContext(), TcODataSchemaHelper.getObjectSelectionTreeForSchemaGenreation(getContext(), schema, true, false, false, __complexType.getId(), view));
		view.setViewForObjectType(getContext(), complexType.entityName);
		complexType.setOdataComplexType_View(getContext(), view);
		view.commit(getContext());
		FeedbackHelper.addOpenFormFeedback(
			getContext(),
			"SchemaGenerator/SelectNonPrimitiveTypeForComplexTypes",
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
		return "ShowSelectNonPrimitiveTypeForComplexTypePageAction";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
