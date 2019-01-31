/*
 * Copyright 2012 SURFnet bv, The Netherlands
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package oauth;

import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 *
 *
 */
public abstract class SeleniumSupport {

  private static WebDriver driver;

  private AuthorizationCodeRequestHandler authorizationCodeRequestHandler ;


  @BeforeClass
  public static void initializeOnce() {
    if (driver != null) {
      driver.close();
      driver.quit();
      driver = null;
    }
    if ("firefox".equals(System.getProperty("selenium.webdriver", "firefox"))) {
      initFirefoxDriver();
    } else {
      initHtmlUnitDriver();
    }

  }



  protected AuthorizationCodeRequestHandler getAuthorizationCodeRequestHandler() {
    return authorizationCodeRequestHandler;
  }

  private static void initHtmlUnitDriver() {
    initDriver(new HtmlUnitDriver());
  }

  private static void initFirefoxDriver() {
    initDriver(new FirefoxDriver());
  }

  private static void initDriver(WebDriver remoteWebDriver) {
    SeleniumSupport.driver = remoteWebDriver;
    SeleniumSupport.driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        if (driver != null) {
          driver.quit();
        }
      }
    });
  }

  /**
   * @return the webDriver
   */
  protected WebDriver getWebDriver() {
    return driver;
  }

  public void restartBrowserSession() {
    initializeOnce();
  }


  protected void login(WebDriver webdriver, boolean consent) throws InterruptedException {
    // Login end user.
    webdriver.findElement(By.id("username")).sendKeys("test11");
    WebElement myDynamicElement = (new WebDriverWait(driver, 10))
            .until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
    myDynamicElement.sendKeys("Hello111");
    WebElement submitElement = (new WebDriverWait(driver, 10))
            .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@type='submit']")));
    submitElement.click();
    Thread.sleep(10000);
    if (consent) {
      consent(webdriver);
    }
  }

  private void consent(WebDriver webdriver) {
    // consent form
    assertThat(webdriver.getPageSource(), containsString("Grant permission"));
    webdriver.findElement(By.id("user_oauth_approval")).click();
  }

}
