package com.saucelabs.example;

import com.saucelabs.example.util.ResultReporter;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.openqa.selenium.OutputType;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;


public class TestSetup {

    private ResultReporter reporter;
    private ThreadLocal<IOSDriver> driver = new ThreadLocal<IOSDriver>();
  
  /**
   * DataProvider that explicitly sets the browser combinations to be used.
   *
   * @param testMethod
   * @return
   */
  @DataProvider(name = "devices", parallel = true)
  public static Object[][] sauceBrowserDataProvider(Method testMethod) {
      return new Object[][]{
    		  //Verify that your account has access to the devices below
             new Object[]{"iOS", "iPhone 7", "10"},
             new Object[]{"iOS", "iPhone SE", ""}
      };
  }  
  
  private IOSDriver createDriver(String platformName, String platformVersion, String deviceName, String methodName) throws MalformedURLException {
  	
      DesiredCapabilities capabilities = new DesiredCapabilities();
      capabilities.setCapability("testobject_api_key", System.getenv("TESTOBJECT_API_KEY"));
      capabilities.setCapability("deviceName", deviceName);
      capabilities.setCapability("platformVersion", platformVersion);
      capabilities.setCapability("platformName", platformName);
      capabilities.setCapability("name",  methodName);
    //  capabilities.setCapability("appiumVersion", "1.13.0");
    //  driver.getCapabilities().getCapability("testobject_test_report_url");
       capabilities.setCapability("testobject_suite_name", "Ian_appium_IoS_Test_SUite");
      capabilities.setCapability("recordDeviceVitals", true);
      capabilities.setCapability("cacheId", "testDemo");
      capabilities.setCapability("noReset", true);
      
      driver.set(new IOSDriver<WebElement>(
              new URL(System.getenv("APPIUM_URL")),
              capabilities));
      return driver.get();
  }

    /* A simple addition, it expects the correct result to appear in the result field. */
    @Test(dataProvider = "devices")
    public void IanFtwoPlusThreeOperation(String platformName, String deviceName, String platformVersion, Method method) throws MalformedURLException {

    	IOSDriver driver = createDriver(platformName, platformVersion, deviceName, method.getName());

        /* Get the elements. */
        MobileElement buttonTwo = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("2")));
        MobileElement buttonThree = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("3")));
        MobileElement buttonPlus = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("+")));
        MobileElement buttonEquals = (MobileElement)(driver.findElement(MobileBy.AccessibilityId("=")));
        MobileElement resultField = (MobileElement)(driver.findElement(By.xpath("//XCUIElementTypeStaticText|//UIAApplication[1]/UIAWindow[1]/UIAStaticText[1]")));

        /* Add two and two. */
        buttonTwo.click();
        buttonPlus.click();
        buttonThree.click();
        driver.getScreenshotAs(OutputType.FILE);
        buttonEquals.click();
        driver.getScreenshotAs(OutputType.FILE);
        
        /* Check if within given time the correct result appears in the designated field. */
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.textToBePresentInElement(resultField, "5"));
        
        buttonTwo.click();
        buttonPlus.click();
        buttonThree.click();
        driver.getScreenshotAs(OutputType.FILE);
        buttonEquals.click();
        driver.getScreenshotAs(OutputType.FILE);
        
        /* Check if within given time the correct result appears in the designated field. */
        (new WebDriverWait(driver, 30)).until(ExpectedConditions.textToBePresentInElement(resultField, "5"));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
    	IOSDriver driver = getWebDriver();
    	
    	reporter = new ResultReporter();   	
        boolean success = result.isSuccess();
        String sessionId = driver.getSessionId().toString();

        reporter.saveTestStatus(sessionId, success);
        driver.quit();
        
    	
    	/*
    	((JavascriptExecutor) webDriver.get()).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
    	  webDriver.get().quit();
    	  **/
    }
    
    /*
    protected void annotate(String text) {
        ((JavascriptExecutor) webDriver.get()).executeScript("sauce:context=" + text);
    }
    **/
    
    /**
     * @return the {@link WebDriver} for the current thread
     */
    public IOSDriver getWebDriver() {
        return driver.get();
    }
}
