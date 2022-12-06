// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package tcconnector.actions;

import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.thirdparty.org.json.JSONObject;
import com.mendix.webui.CustomJavaAction;
import tcconnector.foundation.TcConnection;
import tcconnector.foundation.exceptions.NotLoadedExcpetion;
import tcconnector.internal.foundation.Constants;
import tcconnector.proxies.SessionUser;
import tcconnector.proxies.TcServerInfo;
import tcconnector.foundation.BusinessObjectMappings;
import tcconnector.foundation.JModelObject;
import tcconnector.foundation.JServiceData;
import tcconnector.foundation.JPolicy;

/**
 * (Deprecated)
 * 
 * SOA URL: 
 * Core-2007-01-Session/getTCSessionInfo
 * 
 * Description:
 * Retrieves information about the Teamcenter Server session.
 */
public class GetTcSessionInfo extends CustomJavaAction<IMendixObject>
{
	private java.lang.String ConfigurationName;

	public GetTcSessionInfo(IContext context, java.lang.String ConfigurationName)
	{
		super(context);
		this.ConfigurationName = ConfigurationName;
	}

	@java.lang.Override
	public IMendixObject executeAction() throws Exception
	{
		// BEGIN USER CODE
		IContext context = getContext();
		JSONObject inputArgs = new JSONObject();

		BusinessObjectMappings mappings = new BusinessObjectMappings("User=TcConnector.User;Group=TcConnector.Group",
				ConfigurationName);
		JSONObject policy = new JPolicy(mappings);
		JSONObject response = TcConnection.callTeamcenterService(context, "Core-2007-01-Session/getTCSessionInfo",
				inputArgs, policy, ConfigurationName);

		TcServerInfo info = new TcServerInfo(context);
		JSONObject extraInfo = response.getJSONObject("extraInfo");
		info.setVersion(extraInfo.getString("DisplayVersion"));
		info.setLocale(extraInfo.getString("TCServerLocale"));
		info.setSyslog(extraInfo.getString("syslogFile"));
		info.setServerID(extraInfo.getString("TcServerID"));

		JModelObject user = (JModelObject) response.getJSONObject("user");
		JModelObject group = (JModelObject) response.getJSONObject("group");
		JServiceData sd = (JServiceData) response.getJSONObject("ServiceData");
		sd.getPlainObjects().size();
		try {
			SessionUser userEntity = new SessionUser(context);
			user.initializeEntity(context, userEntity.getMendixObject(), null, ConfigurationName);
			info.set_user(userEntity);

			info.setGroup(group.getPropertyValue("name"));
		} catch (NotLoadedExcpetion e) {
			String message = e.getMessage()
					+ "\n\nThis is demonstrating error handling for a programming error in CallGetTcSessionInfo Java Action "
					+ "('group.getPropertyValue(\"nameXX\")' should be 'group.getPropertyValue(\"name\")'";
			Constants.LOGGER.info(message);
			// Uncomment the showError call, to show the error to the user (pop-up message)
			// Since this is a RuntimeExcpetion, it does not need to be caught here, if
			// passed to the Mendix framework
			// a generic message is displayed to the user.
			// In either case, the error is logged in the JModelObject class.

			// tcconnector.proxies.microflows.Microflows.showError(getContext(), message );
		}

		return info.getMendixObject();
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "GetTcSessionInfo";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
