package oauth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Integration test (using Selenium) for the Implicit Grant flow.
 */
public class ImplicitGrantTestIT extends SeleniumSupport {

    private static String generateNonce() throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException
    {
        String dateTimeString = Long.toString(new Date().getTime());
        byte[] nonceByte = dateTimeString.getBytes();
        return Base64.encodeBase64String(nonceByte);
    }

    @Test
    public void implicitGrant() throws NoSuchAlgorithmException, NoSuchProviderException, IOException, InterruptedException {
//        performImplicitGrant(true);
        /*
         * The second time no consent is required (as we have already an access token for the client/ principal name
         */
//        restartBrowserSession();
        performImplicitGrant(false);
    }

    private void performImplicitGrant(boolean needConsent) throws NoSuchAlgorithmException, NoSuchProviderException, IOException, InterruptedException {

        WebDriver webdriver = getWebDriver();

        String responseType = "token+id_token";
        String clientId = "testapp";
        String scopes = "openid";
        String redirectUri = "https://sdev11.myid-nonprod.telstra.com/testapplication/";
        String ctfrproceed = "true";
        String url = String.format(
                "%s/identity/as/authorization.oauth2?response_type=%s&client_id=%s&redirect_uri=%s&scopes=%s&nonce=%s&ctfr-proceed=%s",
                "https://sdev11.myid-nonprod.telstra.com/", responseType, clientId, redirectUri, scopes, generateNonce(), ctfrproceed);
        System.out.println(url);
        webdriver.get(url);

        login(webdriver, needConsent);
        URI responseURI = URI.create(webdriver.getCurrentUrl());
        assertThat(responseURI.getPath(), equalTo("/testapplication/"));
        assertThat(responseURI.getHost(), equalTo("sdev11.myid-nonprod.telstra.com"));
    }
}

