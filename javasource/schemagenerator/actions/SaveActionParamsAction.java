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
import schemagenerator.proxies.ActionParams;
import schemagenerator.proxies.ODataBasicTypes;
import com.mendix.systemwideinterfaces.core.IMendixObject;

public class SaveActionParamsAction extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __actionParams;
	private schemagenerator.proxies.ActionParams actionParams;

	public SaveActionParamsAction(IContext context, IMendixObject actionParams)
	{
		super(context);
		this.__actionParams = actionParams;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.actionParams = this.__actionParams == null ? null : schemagenerator.proxies.ActionParams.initialize(getContext(), __actionParams);

		// BEGIN USER CODE
		String baseType = null;
		if (!actionParams.getBaseType(getContext()).getCaption().equals(ODataBasicTypes._Object.getCaption())) {
			baseType = actionParams.getBaseType(getContext()).getCaption();
			actionParams.setParameterDataType(getContext(), baseType);
		}
		actionParams.commit(getContext());
		FeedbackHelper.addCloseCallerFeedback(getContext());
		FeedbackHelper.addRefreshClass(getContext(), ActionParams.getType());
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
		return "SaveActionParamsAction";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
