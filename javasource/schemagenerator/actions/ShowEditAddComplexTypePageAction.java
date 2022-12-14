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
import com.mendix.webui.CustomJavaAction;
import com.mendix.webui.FeedbackHelper;
import com.mendix.systemwideinterfaces.core.IFeedback;
import schemagenerator.proxies.ContractNameSpaces;
import schemagenerator.proxies.OdataComplexType;
import com.mendix.systemwideinterfaces.core.IMendixObject;

public class ShowEditAddComplexTypePageAction extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __complexType;
	private schemagenerator.proxies.OdataComplexType complexType;

	public ShowEditAddComplexTypePageAction(IContext context, IMendixObject complexType)
	{
		super(context);
		this.__complexType = complexType;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.complexType = this.__complexType == null ? null : schemagenerator.proxies.OdataComplexType.initialize(getContext(), __complexType);

		// BEGIN USER CODE
		complexType.setComplexTypeName(complexType.getComplexTypeName().split("[.]")[1]);
		complexType.commit();
		FeedbackHelper.addOpenFormFeedback(
			getContext(),
			"SchemaGenerator/AddComplexTypePage",
			"Edit Custom Type",
			IFeedback.FormTarget.MODAL,
			complexType.getMendixObject().getId(),
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
		return "ShowEditAddComplexTypePageAction";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
