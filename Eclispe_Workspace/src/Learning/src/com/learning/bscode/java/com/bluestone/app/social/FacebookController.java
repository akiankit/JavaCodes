package com.bluestone.app.social;

import java.lang.reflect.Type;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.account.CustomerService;
import com.bluestone.app.account.NewAccountRegistrationServiceV2;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.account.model.User;
import com.bluestone.app.checkout.cart.service.CartService;
import com.bluestone.app.core.HttpRequestParser;
import com.bluestone.app.core.util.SessionUtils;
import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


@Controller
@RequestMapping("/auth*")
public class FacebookController {
	
	private static final Logger log = LoggerFactory.getLogger(FacebookController.class);
	
	public static final String ATTR_OAUTH_REQUEST_TOKEN = "oauthRequestToken";
	public static final String ATTR_OAUTH_ACCESS_TOKEN = "oauthAccessToken";
	
	@Autowired
	@Qualifier("facebookServiceProvider")
	private OAuthServiceProvider facebookServiceProvider;
	
	@Autowired
	private NewAccountRegistrationServiceV2 newAccountRegistrationServiceV2;
	
	@Autowired
	private FacebookLoginService facebookLoginService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
    private CartService cartService;
	
	private static final Token EMPTY_REQUEST_TOKEN = null;

    @ResponseBody
    @RequestMapping(value = {"/facebook-login"}, method = RequestMethod.GET)
    public String login(HttpServletRequest request) {
        log.debug("FacebookController.login(): ClientIP=[{}]", HttpRequestParser.getClientIp(request));
        User authenticatedUser = SessionUtils.getAuthenticatedUser();
        if (authenticatedUser == null) {
            OAuthService service = facebookServiceProvider.getService();
            // redirect to facebook auth page
            return service.getAuthorizationUrl(EMPTY_REQUEST_TOKEN) + "&display=popup";
        } else {
            log.error("FacebookController.login(): Facebook Login Error from ClientIP=[{}]: {} is already logged in.",
                      HttpRequestParser.dumpHeadersForLogging(request), authenticatedUser);
        }
        //Handling url "/showLoginPage" - disables action on clicking "Login with facebook"
        return "";
    }
	
	@RequestMapping(value={"/facebook-callback*"}, method = RequestMethod.GET)
	public ModelAndView callback(@RequestParam(value="code", required=false) String oauthVerifier, HttpServletRequest request) {		
		log.debug("FacebookController.callback(): oauthVerifier={}", oauthVerifier);
		String error = request.getParameter("error");
		ModelAndView modelAndView = new ModelAndView("facebooklogin");
		modelAndView.addObject("hasError", false);		
		if(StringUtils.isNotBlank(error)){
			modelAndView.addObject("hasError", true);
			if(error.equalsIgnoreCase("access_denied")){
				error = "Permission Denied. User Cancelled";
			}			
			String errorReason = request.getParameter("error_reason");
			log.error("FacebookController.callback(): Facebook Error Response, Error={}, Error Reason={}",error, errorReason);
			modelAndView.addObject("error", error);
			return modelAndView;
		}
        //log.debug("FacebookController.callback(): FaceBookLogin Successful for {}", oauthVerifier);
		try{
			
			OAuthService oAuthService = facebookServiceProvider.getService();			
			Verifier verifier = new Verifier(oauthVerifier);
			Token accessToken = oAuthService.getAccessToken(EMPTY_REQUEST_TOKEN, verifier);			
			
			// getting user profile
			OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://graph.facebook.com/me");
			oAuthService.signRequest(accessToken, oauthRequest);
			Response oauthResponse = oauthRequest.send();
			String responseBody = oauthResponse.getBody();
			if(responseBody==null){
				throw new Exception("Facebook Response is Empty");				
			}
			parseResponseAndDoLogin(responseBody);
			
			if (SecurityUtils.getSubject().isAuthenticated()) {
	            Customer authenticatedCustomer = SessionUtils.getAuthenticatedCustomer();
	            // if login user is a customer , then only proceed with setting cart
	            if (authenticatedCustomer != null) {
	                try {
	                    cartService.setCustomerCartInSession(authenticatedCustomer, request.getSession());
	                } catch (Exception e) {
	                    log.error("Error while setting customer=[{}] cart in session ", authenticatedCustomer, e);
	                }
	            }
	        }
		}catch(Exception e){			
			log.error("Error at FacebookController.callback(): Reason={}", Throwables.getRootCause(e).toString());
			modelAndView.addObject("hasError", true);
			modelAndView.addObject("error", "Something went wrong.. please try again");
		}		
		return modelAndView;
	}

	private void parseResponseAndDoLogin(String responseBody) throws Exception {	
		log.debug("FacebookController.parseResponseAndDoLogin(): responsebody={}", responseBody);	
		responseBody = StringEscapeUtils.unescapeHtml(responseBody);
		JsonElement parse = new JsonParser().parse(responseBody);	    	    
		Type type = new TypeToken<Map<String, Object>>(){}.getType();
		Map<String,Object> responseMap = new Gson().fromJson(parse, type);
		String email = (String) responseMap.get("email");
		String name = (String) responseMap.get("name");
		String facebookId = (String) responseMap.get("id");
		String gender = (String) responseMap.get("gender");
		String birthday = (String) responseMap.get("birthday");				
		Customer customer = newAccountRegistrationServiceV2.createAccountForFacebookUser(email,name, facebookId,gender,birthday);
		String loginError = facebookLoginService.facebookLogin(customer.getEmail(), facebookId);
		if(StringUtils.isNotEmpty(loginError)){
			throw new Exception(loginError);
		}		
	}	

}