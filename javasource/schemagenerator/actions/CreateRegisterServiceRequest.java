// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package schemagenerator.actions;

import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import com.mendix.core.Core;
import com.mendix.http.Http;
import com.mendix.http.HttpHeader;
import com.mendix.http.HttpMethod;
import com.mendix.http.HttpResponse;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import org.apache.http.HttpStatus;
import schemagenerator.proxies.constants.Constants;
import tcconnector.proxies.Cookie;
import tcconnector.proxies.TcSession;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.thirdparty.org.json.JSONArray;
import com.mendix.thirdparty.org.json.JSONObject;

public class CreateRegisterServiceRequest extends CustomJavaAction<java.lang.String>
{
	private IMendixObject __SchemaDocument;
	private schemagenerator.proxies.OdataSchemaDocument SchemaDocument;

	public CreateRegisterServiceRequest(IContext context, IMendixObject SchemaDocument)
	{
		super(context);
		this.__SchemaDocument = SchemaDocument;
	}

	@java.lang.Override
	public java.lang.String executeAction() throws Exception
	{
		this.SchemaDocument = this.__SchemaDocument == null ? null : schemagenerator.proxies.OdataSchemaDocument.initialize(getContext(), __SchemaDocument);

		// BEGIN USER CODE
		// Retrive Cookie for the current Teamcenter session
		TcSession tcSession = tcconnector.proxies.microflows.Microflows.retrieveTcSessionBasedOnConfigName(getContext(),null);
		IMendixObject mendixCookie = Core.retrieveByPath(getContext(), tcSession.getMendixObject(), "TcConnector.Cookies").get(0);
		Cookie cookie = Cookie.initialize(getContext(), mendixCookie);
		HttpHeader cookieHeader = new HttpHeader("Cookie", cookie.getName() + '=' + cookie.getValue());

		Http httpHandler = Core.http();
		URI getCsdlUrl = new URI(
			tcSession.getHostAddress(getContext()) + "/" + 
			Constants.getAppendMicroServiceURL() + "/" +
			GET_DEPLOYED_SERVICE_CSDL_SCHEMA_CONTENT_WITH_REFERENCES + "?" +
			"schemaVersion=" + SchemaDocument.getmajorVersion(getContext()) +
			"&schemaName=" + SchemaDocument.getName(getContext())
		);
		HttpResponse resp = httpHandler.executeHttpRequest(
			getCsdlUrl,
			HttpMethod.GET, 
			new HttpHeader[]{cookieHeader},
			null
		);
		if (resp.getStatusCode() == HttpStatus.SC_OK) {
			InputStream respBody = resp.getContent();
			String respBodyStr = TcODataSchemaHelper.convertStreamToString(respBody);
			JSONObject respBodyjObj = new JSONObject(respBodyStr);
			HashMap<String,String> allServiceMap = getAllDependentSchemas(respBodyjObj);
			allServiceMap.remove(respBodyjObj.getString("name") + "/" + respBodyjObj.getString("majorVersion"));
			
			// Generating the JSON POST Request Body for the datahub
			JSONArray includes = new JSONArray();
			Collection<String> serviceList = allServiceMap.values();
			for (String csdl : serviceList) {
				JSONObject value = new JSONObject();
				value.put("Value", csdl);
				includes.put(value);
			}
			JSONObject contract = new JSONObject();
			contract.put("Type", "Metadata");
			contract.put("Value", respBodyjObj.getString("CSDLSchema"));
			contract.put("Includes", includes);
			
			JSONArray contracts = new JSONArray();
			contracts.put(contract);
			
			JSONObject version = new JSONObject();
			version.put("Contracts", contracts);
			version.put("VersionText", respBodyjObj.getString("majorVersion") + "." + String.valueOf(respBodyjObj.getInt("minorVersion")));
			version.put("Location", "/" + Constants.getAppendMicroServiceURL() + "/" + respBodyjObj.getString("name") + ".svc" + "/" + respBodyjObj.getString("majorVersion"));
			JSONArray versions = new JSONArray();
			versions.put(version);

			JSONObject publishedService = new JSONObject();
			publishedService.put("Name", respBodyjObj.getString("name"));
			publishedService.put("ContractType", "OData_4_0_Xml");
			publishedService.put("Versions", versions);
			JSONArray publishedServices = new JSONArray();
			publishedServices.put(publishedService);

			JSONObject datahubResp = new JSONObject();
			datahubResp.put("PublishedServices", publishedServices);
			datahubResp.put("ConsumedServices", new JSONArray());
			respBody.close();
			resp.close();
			return datahubResp.toString();
		}
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
		return "CreateRegisterServiceRequest";
	}

	// BEGIN EXTRA CODE
	public static final String GET_DEPLOYED_SERVICE_CSDL_SCHEMA_CONTENT_WITH_REFERENCES = "getDeployedServiceCSDLSchemaContentWithRefs";
	
	public static HashMap<String,String> getAllDependentSchemas(JSONObject teamcenterResp) {
		
		HashMap<String,String> dependsMap = new HashMap<String,String>();
		JSONArray dependsArray = teamcenterResp.getJSONArray("depends");
		for (JSONObject dependentService : dependsArray.toJSONObjectCollection()) {
			dependsMap.putAll(getAllDependentSchemas(dependentService));
		}
		String serviceName = teamcenterResp.getString("name");
		String serviceVersion = teamcenterResp.getString("majorVersion");
		dependsMap.put(serviceName + "/" + serviceVersion, teamcenterResp.getString("CSDLSchema"));
		return dependsMap;
	}
	// END EXTRA CODE
}
