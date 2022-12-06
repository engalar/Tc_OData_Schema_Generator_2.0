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
import com.mendix.thirdparty.org.json.JSONObject;
import com.mendix.webui.CustomJavaAction;
import tcconnector.foundation.TcConnection;
import tcconnector.internal.foundation.Constants;
import tcconnector.internal.foundation.CookieManager;
import tcconnector.proxies.TcSession;

/**
 * SOA URL: 
 * Core-2006-03-Session/logout
 * 
 * Tc Version:
 * TcEng 2005 SR1
 * 
 * Description:
 * Retrieves the Teamcenter Session for the user and attempts to log them out of Teamcenter.
 * 
 * Once logged out the cookies associated with the session will be deleted and the Teamcenter Host Address within the session will be set to an empty string.
 */
public class Logout extends CustomJavaAction<java.lang.Boolean>
{
	private java.lang.String ConfigurationName;

	public Logout(IContext context, java.lang.String ConfigurationName)
	{
		super(context);
		this.ConfigurationName = ConfigurationName;
	}

	@java.lang.Override
	public java.lang.Boolean executeAction() throws Exception
	{
		// BEGIN USER CODE
		JSONObject logoutArgs = new JSONObject();
		TcSession tcSession = tcconnector.proxies.microflows.Microflows.retrieveTcSessionBasedOnConfigName(getContext(),ConfigurationName);
		TcConnection.callTeamcenterService(getContext(),  Constants.OPERATION_LOGOUT, logoutArgs, null, ConfigurationName);
		CookieManager.deleteCookies(getContext(), tcSession);
		tcSession.delete(getContext());
		return true;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "Logout";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
