package testing.internal.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.thirdparty.org.json.JSONObject;

import tcconnector.proxies.Credentials;
import tcconnector.proxies.TeamcenterConfiguration;
import tcconnector.proxies.microflows.Microflows;

public class TestUtils 
{

	public static TeamcenterConfiguration tcConfig;
	private static Credentials crediantials;
	
	public static void teamcenterLogin(IContext context) throws FileNotFoundException, CoreException
	{
		File myObj = new File(Core.getConfiguration().getResourcesPath(), "TestTcConfigInfo.json");
		Scanner myReader = new Scanner(myObj);
		String data = null;
		while (myReader.hasNextLine())
		{
			if(data == null)
				data = myReader.nextLine();
			else
				data = data + myReader.nextLine();
		}
		
		JSONObject jsonObj = new JSONObject(data);
		
		String TcUrl = jsonObj.getString("TC_URL");
		JSONObject Credentials = jsonObj.getJSONObject("Credentials");
		String user = Credentials.getString("UserName");
		String password = Credentials.getString("Password");
		
		crediantials = new Credentials(context);
		crediantials.setuser(user);
		crediantials.setpassword(password);
		
		List<IMendixObject> tcConfigList = Core.retrieveXPathQuery(context, "//TcConnector.TeamcenterConfiguration" + "[TCURL = '" + TcUrl + "']");
		
		if(tcConfigList.isEmpty())
		{
			List<IMendixObject> tcConfig_TestConfigList = Core.retrieveXPathQuery(context, "//TcConnector.TeamcenterConfiguration" + "[ConfigName = '" + "TestConfig" + "']");
			if(!tcConfig_TestConfigList.isEmpty())
				Core.delete(context, tcConfig_TestConfigList);
			
			tcConfig = new TeamcenterConfiguration(context);
			tcConfig.setConfigName("TestConfig");
			tcConfig.setActive(true);
			tcConfig.setTCURL(TcUrl);
		}
		else
		{
			tcConfig = TeamcenterConfiguration.load(context, tcConfigList.get(0).getId());
			tcConfig.setActive(true);
		}
		
		Microflows.saveTeamcenterConfiguration(context, tcConfig);
		tcconnector.proxies.microflows.Microflows.executeLogin(context, crediantials, tcConfig);
	}
	
	public static void teamcenterLogout(IContext context) 
	{
		tcconnector.proxies.microflows.Microflows.executeLogout(context, tcConfig);
	}
}
