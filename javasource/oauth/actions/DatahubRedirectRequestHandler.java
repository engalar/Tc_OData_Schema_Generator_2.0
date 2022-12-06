package oauth.actions;

import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;

import com.mendix.core.Core;
import com.mendix.externalinterface.connector.RequestHandler;
import com.mendix.http.Http;
import com.mendix.http.HttpHeader;
import com.mendix.http.HttpMethod;
import com.mendix.http.HttpResponse;
import com.mendix.integration.ActionWhenNoObjectFound;
import com.mendix.integration.Integration;
import com.mendix.m2ee.api.IMxRuntimeRequest;
import com.mendix.m2ee.api.IMxRuntimeResponse;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.systemwideinterfaces.core.ISession;

import org.apache.commons.io.IOUtils;

import oauth.proxies.Hash;
import oauth.proxies.ResponceAccessToken;
import oauth.proxies.constants.Constants;
import system.proxies.Session;

public class DatahubRedirectRequestHandler extends RequestHandler {

    private static final String ENCODING            = "UTF-8";
    private String contextPath;

    private static final String HOME_PAGE           = "./index.html";
    private static final String ENVIRONMENTS_PAGE   = "/p/registerEnv";
    
    public DatahubRedirectRequestHandler(String contextPath) {
        this.contextPath = contextPath;
	}

	@Override
    protected void processRequest(IMxRuntimeRequest req, IMxRuntimeResponse resp, String path) throws Exception {

        if (!path.equals("callback")) {
            redirectToUrl(req, resp, HOME_PAGE);
            return;
        }
        String code = req.getParameter("code");
        String state = req.getParameter("state");

        ISession session = this.getSessionFromRequest(req, false);
        if (session == null) {
            redirectToUrl(req, resp, HOME_PAGE);
            return;
        }

		IContext context = session.createContext();
        int depth = 1;
        IMendixObject mxObjHash = Core.retrieveXPathQuery(context, "//OAuth.Hash" + "[hash = '" + state + "']", depth).get(0);
        Hash hash = Hash.initialize(context, mxObjHash);
        if (hash == null) {
            redirectToUrl(req, resp, HOME_PAGE);
        }

        IMendixObject responseAccessToken = Core.retrieveByPath(context, hash.getMendixObject(), "OAuth.Hash_ResponceAccessToken").get(0);

        Http httpHandler = Core.http();
        HttpHeader contentType = new HttpHeader("Content-Type", "application/x-www-form-urlencoded");
        String content = "grant_type=authorization_code&code=" + URLEncoder.encode(code, ENCODING) +
                            "&client_id="+Constants.getClientID() + "&client_secret="+
                            URLEncoder.encode(Constants.getClientSecret(), ENCODING) + "&redirect_uri="+
                            URLEncoder.encode(Constants.getRedirectEndpoint(), ENCODING);
        InputStream contentStream = IOUtils.toInputStream(content, ENCODING);
        HttpResponse responseOAuthToken = httpHandler.executeHttpRequest(
            new URI(Constants.getAuthenticationServer() + "/oauth/token"), 
            HttpMethod.POST,
            new HttpHeader[] {contentType},
            contentStream
        );
        contentStream.close();
        Integration integrationHandler = Core.integration();
        IMendixObject mxObjOAuthToken = integrationHandler.importStream(context, responseOAuthToken.getContent(), "OAuth.JwtTokenResponse", ActionWhenNoObjectFound.CREATE, null, -1, false).get(0);
        ResponceAccessToken oauthToken = ResponceAccessToken.initialize(context, mxObjOAuthToken);
        oauthToken.setHash_ResponceAccessToken(context, hash);
        IMendixObject mxObjSessionObject = Core.retrieveByPath(context, responseAccessToken, "OAuth.ResponceAccessToken_Session").get(0);
        Session sessionObject = Session.initialize(context, mxObjSessionObject);
        oauthToken.setResponceAccessToken_Session(context, sessionObject);
        Core.commit(context, oauthToken.getMendixObject());
        Core.delete(context, responseAccessToken);

        if (hash.getorignatorUrl(context).equals(ENVIRONMENTS_PAGE)) {
            IMendixObject datahubEnvandApp = Core.retrieveByPath(context, hash.getMendixObject(), "OAuth.Hash_DatahubAppAndEnv").get(0);
            String formId = String.valueOf( datahubEnvandApp.getId().toLong());
            redirectToUrl(req, resp, ENVIRONMENTS_PAGE + "/" + formId);
        }
        else if (hash.getorignatorUrl(context).equals(HOME_PAGE)) {
            redirectToUrl(req, resp, HOME_PAGE);
        }
    }

    private static void redirectToUrl(IMxRuntimeRequest req, IMxRuntimeResponse resp, String url) {
        resp.addHeader("Cookie", req.getHeader("Cookie"));
        resp.setStatus(IMxRuntimeResponse.SEE_OTHER);
        resp.addHeader("Location", url);
    }

}
