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
import com.mendix.systemwideinterfaces.core.IFeedback;
import com.mendix.webui.CustomJavaAction;
import com.mendix.webui.FeedbackHelper;
import schemagenerator.proxies.ComplexTypeAttribute;
import com.mendix.systemwideinterfaces.core.IMendixObject;

public class ShowComplexTypeAttributePageAction extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __complexType;
	private schemagenerator.proxies.OdataComplexType complexType;

	public ShowComplexTypeAttributePageAction(IContext context, IMendixObject complexType)
	{
		super(context);
		this.__complexType = complexType;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.complexType = this.__complexType == null ? null : schemagenerator.proxies.OdataComplexType.initialize(getContext(), __complexType);

		// BEGIN USER CODE
		ComplexTypeAttribute complexTypeAttribute = new ComplexTypeAttribute(getContext());
		complexTypeAttribute.setComplexTypeAttribute_OdataComplexType(getContext(), complexType);
		FeedbackHelper.addOpenFormFeedback(
			getContext(),
			"SchemaGenerator/AddNewComplexTypeAttribute",
			IFeedback.FormTarget.MODAL,
			complexTypeAttribute.getMendixObject().getId(),
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
		return "ShowComplexTypeAttributePageAction";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
