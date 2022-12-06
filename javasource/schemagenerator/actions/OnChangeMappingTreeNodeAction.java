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
import com.mendix.webui.FeedbackHelper;
import schemagenerator.proxies.SelectionTreeNodeHelper;
import schemagenerator.proxies.View;
import com.mendix.systemwideinterfaces.core.IMendixObject;

public class OnChangeMappingTreeNodeAction extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __selectionHelper;
	private schemagenerator.proxies.SelectionTreeNodeHelper selectionHelper;

	public OnChangeMappingTreeNodeAction(IContext context, IMendixObject selectionHelper)
	{
		super(context);
		this.__selectionHelper = selectionHelper;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.selectionHelper = this.__selectionHelper == null ? null : schemagenerator.proxies.SelectionTreeNodeHelper.initialize(getContext(), __selectionHelper);

		// BEGIN USER CODE
		List<IMendixObject> nodeList = Core.retrieveByPath(getContext(), __selectionHelper, SelectionTreeNodeHelper.MemberNames.SelectionTreeNodeHelper_SelectionTreeNode.toString());
		View view = View.initialize(getContext(), Core.retrieveByPath(getContext(), __selectionHelper, SelectionTreeNodeHelper.MemberNames.SelectionTreeNodeHelper_View.toString()).get(0));
		if (nodeList.isEmpty()) {
//			view.setSelectedNone(false);
			view.commit();
			FeedbackHelper.addRefreshClass(getContext(), View.getType());
			FeedbackHelper.addRefreshObjectFeedback(getContext(), view.getMendixObject().getId());
			return null;
		}
//		view.setSelectedNone(true);
		view.commit();
		FeedbackHelper.addRefreshClass(getContext(), View.getType());
		FeedbackHelper.addRefreshObjectFeedback(getContext(), view.getMendixObject().getId());
		
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
		return "OnChangeMappingTreeNodeAction";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
