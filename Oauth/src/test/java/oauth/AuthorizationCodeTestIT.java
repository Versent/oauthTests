
package oauth;


import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.openqa.selenium.WebDriver;


import java.net.URI;
import java.net.URLEncoder;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * Integration test (using Selenium) for the Authorization Code flow.
 */
public class AuthorizationCodeTestIT extends SeleniumSupport {
  
  private String clientId = "testapp";
  private String secret = "somesecret";


  @Test
  public void authCode() throws Exception {
    String accessTokenRedirectUri = "https://sdev11.myid-nonprod.telstra.com/testapplication/";

    WebDriver webdriver = getWebDriver();
    String responseType = "code";
    String scopes = "openid profile";
    String url = String.format(
            "%s/identity/as/authorization.oauth2?response_type=%s&scope=%s&client_id=%s&redirect_uri=%s",
            "https://sdev11.myid-nonprod.telstra.com", responseType, scopes, clientId, accessTokenRedirectUri);
    System.out.println(url);
    webdriver.get(url);

    login(webdriver, false);
    URI responseURI = URI.create(webdriver.getCurrentUrl());

    assertThat(responseURI.getPath(), equalTo("/testapplication/"));
    assertThat(responseURI.getHost(), equalTo("sdev11.myid-nonprod.telstra.com"));
  }
    

}