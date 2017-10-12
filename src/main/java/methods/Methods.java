package methods;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.App;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;
import org.testng.Assert;

import java.util.regex.Pattern;

import static helpMethods.HelpMethods.handleLogoutWindow;

/*loadPage
        login
        checkStatus

        data stored as global variables in test classes2copy
static
data

loadPage(String browser, String url)
login(String method, String username, String password)
checkStatus(String status)
driver is  accessible from Methods class
declare, but not initialize
initialize variables from test classes2copy before method calls
\!test_group5_5220

ability to provide default variables or override these variables in test classes2copy
*/

public class Methods {
    public static WebDriver driver;
    public static String url;
    public static String browser;
    public static String method;
    public static String username;
    public static String password;
    public static String group;
    public static boolean fast;
    public static App cxphone;
    public static Screen screen;
    public static org.sikuli.script.Pattern button_Discard;
    //static String status;

    public static void loadPage() throws InterruptedException {

        if (browser == "chrome") {
            System.setProperty("webdriver.chrome.driver", "C:/chromedriver/chromedriver.exe");
            driver = new ChromeDriver();
            driver.get(url);
            WebDriverWait waitForTitle = new WebDriverWait(driver, 10);
            waitForTitle.until(ExpectedConditions.titleIs("gbwebphone"));
            Assert.assertEquals(driver.getTitle(), "gbwebphone");
        } else {
            System.setProperty("webdriver.ie.driver", "C:/iedriver32/IEDriverServer.exe");
            DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

            /*ieCapabilities.setCapability("nativeEvents", false);
            ieCapabilities.setCapability("unexpectedAlertBehaviour", "accept");
            ieCapabilities.setCapability("ignoreProtectedModeSettings", true);
            ieCapabilities.setCapability("disable-popup-blocking", true);
            ieCapabilities.setCapability("enablePersistentHover", true);*/
            ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                    true);
            driver = new InternetExplorerDriver(ieCapabilities);

            Thread thread1 = new Thread() {
                public void run() {
                    driver.get(url);
                }
            };

            Thread thread2 = new Thread() {
                public void run() {
                    try {
                        screen = new Screen();
                        org.sikuli.script.Pattern checkbox_doNotAskAgain = new org.sikuli.script.Pattern("C:\\SikuliImages\\checkbox_doNotAskAgain.png");
                        screen.wait(checkbox_doNotAskAgain, 2);
                        screen.click(checkbox_doNotAskAgain);

                        org.sikuli.script.Pattern option_updateJavaLater = new org.sikuli.script.Pattern("C:\\SikuliImages\\option_updateJavaLater.png");
                        screen.wait(option_updateJavaLater, 2);
                        screen.click(option_updateJavaLater);
                    } catch (FindFailed findFailed) {
                        findFailed.printStackTrace();
                    }
                    try{
                        org.sikuli.script.Pattern checkbox_acceptTheRisk = new org.sikuli.script.Pattern("C:\\SikuliImages\\checkbox_acceptTheRisk.png");
                        screen.wait(checkbox_acceptTheRisk, 2);
                        screen.click(checkbox_acceptTheRisk);

                        org.sikuli.script.Pattern button_Run = new org.sikuli.script.Pattern("C:\\SikuliImages\\button_Run.png");
                        screen.wait(button_Run, 2);
                        screen.click(button_Run);
                    } catch (FindFailed findFailed) {
                        findFailed.printStackTrace();
                    }
                }
            };

// Start the downloads.
            thread1.start();
            thread2.start();

// Wait for them both to finish
            thread1.join();
            thread2.join();



            WebDriverWait waitForTitle = new WebDriverWait(driver, 10);
            waitForTitle.until(ExpectedConditions.titleIs("gbwebphone"));
            Assert.assertEquals(driver.getTitle(), "gbwebphone");
            WebElement language = driver.findElement(By.cssSelector("#lang_input_label"));
            language.click();
            WebElement language_en = driver.findElement(By.xpath("//li[text() = 'English']"));
            language_en.click();
        }
    }

    public static void login() throws InterruptedException {


        if (method == "sso") {
            WebElement button_SSO = driver.findElement(By.cssSelector("#ssoButton > span"));
            String winHandleBefore = driver.getWindowHandle();
            button_SSO.click();
            for (String winHandle : driver.getWindowHandles()) {
                driver.switchTo().window(winHandle);
            }
            WebElement ssoUsername = driver.findElement(By.cssSelector("#username"));
            ssoUsername.sendKeys(username);
            if (fast == false)
                Thread.sleep(1000);
            WebElement ssoPassword = driver.findElement(By.cssSelector("#password"));
            ssoPassword.sendKeys(password);
            if (fast == false)
                Thread.sleep(1000);
            WebElement ssoRememberMe = driver.findElement(By.cssSelector("#remember-me"));
            ssoRememberMe.click();
            if (fast == false)
                Thread.sleep(1000);
            WebElement button_sso_Login = driver.findElement(By.cssSelector("#login_button"));
            button_sso_Login.click();
            driver.switchTo().window(winHandleBefore);
            Thread.sleep(1000);
            driver = handleLogoutWindow(driver);
        } else {
            WebDriverWait waitForUsername = new WebDriverWait(driver, 5);
            waitForUsername.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[name=username_input]")));
            WebElement name = driver.findElement(By.cssSelector("[name=username_input]"));
            WebElement password = driver.findElement(By.cssSelector("[name=password_input]"));

            WebElement button_Connect = driver.findElement(By.cssSelector("[name='btn_connect']"));
            name.sendKeys("81016");
            if (fast == false)
                password.sendKeys("1");
            button_Connect.click();
            //if(fast ==false);
            Thread.sleep(1000);
            driver = handleLogoutWindow(driver);
        }
        WebDriverWait waitForButtonContinue = new WebDriverWait(driver, 10);
        waitForButtonContinue.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#btn_continue > span.ui-button-text.ui-c")));
        WebDriverWait waitForGroupList = new WebDriverWait(driver, 10);
        waitForGroupList.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#group_input_label")));
        WebElement groupList = driver.findElement(By.cssSelector("#group_input_label"));
        groupList.click();

        WebElement element = driver.findElement(By.cssSelector("[data-label=" + group + "]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);
        WebElement chatGroup = driver.findElement(By.cssSelector("[data-label=" + group + "]"));
        chatGroup.click();
        WebElement btnContinue = driver.findElement(By.cssSelector("#btn_continue > span.ui-button-text.ui-c"));
        btnContinue.click();

    }

    public static void handlePluginWindow() throws InterruptedException {
       /* try{
            WebDriverWait waitForButton_OK = new WebDriverWait(driver, 2);
            waitForButton_OK.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[id = 'btn_ef_positive']")));
            WebElement button_OK = driver.findElement(By.cssSelector("[id = 'btn_ef_positive']"));
            button_OK.click();
            WebDriverWait waitForButton_Cancel = new WebDriverWait(driver, 2);
            waitForButton_Cancel.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[id = 'btn_ef_positive']")));
            WebElement button_Cancel = driver.findElement(By.cssSelector("[id = 'btn_np_negative']"));
            button_Cancel.click();
        } catch(Exception e){}*/
        try {
            WebDriverWait waitForButton_OK = new WebDriverWait(driver, 2);
            waitForButton_OK.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[id = 'btn_ef_positive']")));
            WebElement button_OK = driver.findElement(By.cssSelector("[id = 'btn_ef_positive']"));
            button_OK.click();
     /*WebDriverWait waitForButton_Cancel = new WebDriverWait(driver, 10);
     waitForButton_Cancel.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[id = 'btn_ef_positive']")));*/
            Thread.sleep(3000);
            WebElement button_Cancel = driver.findElement(By.cssSelector("[id = 'btn_np_negative']"));
            button_Cancel.click();

            screen = new Screen();
            button_Discard = new org.sikuli.script.Pattern("C:\\SikuliImages\\button_Discard.png");
            screen.wait(button_Discard, 10);
            screen.click(button_Discard);

        } catch (Exception e) {
        }

        /*WebDriverWait waitForButton_OK = new WebDriverWait(driver, 2);
        waitForButton_OK.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[id = 'btn_ef_positive']")));
        WebElement button_OK = driver.findElement(By.cssSelector("[id = 'btn_ef_positive']"));
        button_OK.click();
           *//* WebDriverWait waitForButton_Cancel = new WebDriverWait(driver, 10);
            waitForButton_Cancel.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[id = 'btn_ef_positive']")));*//*
        Thread.sleep(3000);
        WebElement button_Cancel = driver.findElement(By.cssSelector("[id = 'btn_np_negative']"));
        button_Cancel.click();*/
    }

    public static void checkStatus(String status) throws InterruptedException {
        //Thread.sleep(3000);
        WebDriverWait waitForStatus = new WebDriverWait(driver, 50);
        waitForStatus.until(ExpectedConditions.textMatches(By.cssSelector(
                "#statusButton > span.ui-button-text.ui-c"), Pattern.compile(".*\\b" + status + "\\b.*")));
        WebElement currentStatus = driver.findElement(By.cssSelector(
                "#statusButton > span.ui-button-text.ui-c"));
        Assert.assertTrue(currentStatus.getText().contains(status));

    }
}
