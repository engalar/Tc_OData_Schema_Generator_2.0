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
import schemagenerator.proxies.EnumMembers;
import schemagenerator.proxies.OdataEnum;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.systemwideinterfaces.core.UserException;
import com.mendix.systemwideinterfaces.core.UserException.ExceptionCategory;

public class SaveEnumMembersAction extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __enumMember;
	private schemagenerator.proxies.EnumMembers enumMember;

	public SaveEnumMembersAction(IContext context, IMendixObject enumMember)
	{
		super(context);
		this.__enumMember = enumMember;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.enumMember = this.__enumMember == null ? null : schemagenerator.proxies.EnumMembers.initialize(getContext(), __enumMember);

		// BEGIN USER CODE
		String name = enumMember.getName();
		if (name == null || name.isEmpty()) {
			throw new UserException(ExceptionCategory.Custom, "Name Must Not Be Empty");
		}
		name = name.trim();
		NamingRules namingRules = new NamingRules(getContext(), name);
		namingRules.executeAction();
		IMendixObject enumMxObj =  Core.retrieveByPath(getContext(), __enumMember, EnumMembers.MemberNames.EnumMembers_OdataEnum.toString()).get(0);
		List<IMendixObject> allMembers = Core.retrieveByPath(getContext(), enumMxObj, EnumMembers.MemberNames.EnumMembers_OdataEnum.toString());
		allMembers.removeIf(e -> e.getId().toLong() == __enumMember.getId().toLong());
		for (IMendixObject enumMemberMxObj : allMembers) {
			if (enumMemberMxObj.getValue(getContext(), EnumMembers.MemberNames.Name.toString()).toString().equals(name)) {
				throw new UserException(ExceptionCategory.Custom, "Name Already In Use");
			}
		}
		
		enumMember.setName(name);
		enumMember.commit(getContext());
		FeedbackHelper.addRefreshClass(getContext(), EnumMembers.getType());
		FeedbackHelper.addRefreshClass(getContext(), OdataEnum.getType());
		FeedbackHelper.addCloseCallerFeedback(getContext());
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
		return "SaveEnumMembersAction";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}