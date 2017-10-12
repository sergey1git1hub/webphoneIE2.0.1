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
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

import static data.Data.agentChrome;
import static helpMethods.HelpMethods.handleLogoutWindow;
import static methods.Methods.driver;
import static methods.Methods.fast;
import static methods.Methods.password;

/**
 * Created by SChubuk on 04.10.2017.
 */

public class Methods {
    public static String browser;
    public static boolean onJenkins;
    static boolean killProcess = true;

    public static WebDriver openWebphoneLoginPage(WebDriver driver, String browser, final String webphoneUrl) throws InterruptedException, IOException {
        if (browser == "chrome") {
            System.setProperty("webdriver.chrome.driver", "C:/chromedriver/chromedriver.exe");
            driver = new ChromeDriver();
            driver.get(webphoneUrl);
            WebDriverWait waitForTitle = new WebDriverWait(driver, 10);
            waitForTitle.until(ExpectedConditions.titleIs("gbwebphone"));
            Assert.assertEquals(driver.getTitle(), "gbwebphone");
        } else {
            if(killProcess == true){
                Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
            killProcess = false;
            }
            Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
            System.setProperty("webdriver.ie.driver", "C:/iedriver32/IEDriverServer.exe");
            DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
            ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                    true);
            driver = new InternetExplorerDriver(ieCapabilities);
            driver.manage().window().maximize();

           /* final WebDriver finalDriver = driver;
            final String finalwebphoneUrl = webphoneUrl;*/
            Thread thread1 = new LoaderThread(driver, webphoneUrl);
            Thread thread2 = new Thread() {
                public void run() {
                    Screen screen;
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
                    try {
                        screen = new Screen();
                        ;
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
        return driver;
    }

    public static WebDriver login(WebDriver driver, String method, String username, String group) throws InterruptedException {
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
            name.sendKeys(username);
            if (fast == false)
                password.sendKeys("1");
            button_Connect.click();
            //if(fast ==false);

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

        return driver;
    }

    public static WebDriver checkStatus(WebDriver driver, String status, int waitTime) {
        WebDriverWait waitForStatus = new WebDriverWait(driver, waitTime);
        waitForStatus.until(ExpectedConditions.textMatches(By.cssSelector(
                "#statusButton > span.ui-button-text.ui-c"), Pattern.compile(".*\\b" + status + "\\b.*")));
        WebElement currentStatus = driver.findElement(By.cssSelector(
                "#statusButton > span.ui-button-text.ui-c"));
        Assert.assertTrue(currentStatus.getText().contains(status));
        return driver;
    }

    public static WebDriver changeStatus(WebDriver driver, String status) {
        if (browser == "chrome") {
            WebElement currentStatus = driver.findElement(By.cssSelector(
                    "#statusButton > span.ui-button-text.ui-c"));
            currentStatus.click();
            WebElement desirableStatus;
            if (!status.equals("AUX")) {
                desirableStatus = driver.findElement(By.xpath(
                        "/*//*[contains(text(),'" + status + "')]"));
            } else {
                desirableStatus = driver.findElement(By.xpath(
                        "//*[contains(text(),'AUX') and not(contains(text(),'Доступен'))]"));
            }
            desirableStatus.click();
            checkStatus(driver, status, 2);
        } else {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            WebElement currentStatus = driver.findElement(By.cssSelector(
                    "#statusButton > span.ui-button-text.ui-c"));
            executor.executeScript("arguments[0].click();", currentStatus);
            WebElement desirableStatus;
            if (!status.equals("AUX")) {
                desirableStatus = driver.findElement(By.xpath(
                        "/*//*[contains(text(),'" + status + "')]"));
            } else {
                desirableStatus = driver.findElement(By.xpath(
                        "//*[contains(text(),'AUX') and not(contains(text(),'Доступен'))]"));
            }
            executor.executeScript("arguments[0].click();", desirableStatus);
            checkStatus(driver, status, 2);
        }
        return driver;
    }

    public static WebDriver switchLine(WebDriver driver, int line) throws FindFailed {
        if (browser == "chrome") {
            WebElement lineElement = driver.findElement(By.cssSelector("[id = 'btn_line_" + line + "_span']"));
            lineElement.click();
        } else {
            try {
                if (driver instanceof JavascriptExecutor) {
                    ((JavascriptExecutor) driver)
                            .executeScript("wp_common.wp_ChangeLine(" + line + "); log(event);");
                }
            } catch (Exception e) {
            }
        }
        return driver;
    }

    public static WebDriver call(WebDriver driver, int line, String number) throws FindFailed, InterruptedException {
        switchLine(driver, line);
        Thread.sleep(500);
        WebElement phoneNumberField = driver.findElement(By.cssSelector("#PhoneNumber"));
        phoneNumberField.sendKeys(number);
        WebElement button_Call = driver.findElement(By.cssSelector("#btn_call"));
        button_Call.click();
        return driver;
    }

    public static void cxAnswer() throws FindFailed, InterruptedException {
        App cxphone = App.open("C:\\Program Files (x86)\\3CXPhone\\3CXPhone.exe");
        Screen screen = new Screen();
        org.sikuli.script.Pattern button_3CXAcceptCall = new org.sikuli.script.Pattern("C:\\SikuliImages\\button_3CXAcceptCall.png");
        screen.wait(button_3CXAcceptCall, 10);
        screen.click(button_3CXAcceptCall);
        if(fast = false)
            Thread.sleep(1000);
        org.sikuli.script.Pattern closePhoneWindow = new org.sikuli.script.Pattern("C:\\SikuliImages\\closePhoneWindow.png");
        screen.wait(closePhoneWindow, 10);
        screen.click(closePhoneWindow);

    }

    public static WebDriver agentHangup(WebDriver driver, int line) throws FindFailed, InterruptedException {
        switchLine(driver, line);
        Thread.sleep(500);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        WebElement button_Hangup = driver.findElement(By.cssSelector("#btn_hangup"));
        if(browser == "chrome")
        button_Hangup.click();
        else executor.executeScript("arguments[0].click();", button_Hangup);
        return driver;
    }

    public static void clientHangup(WebDriver driver, int line) throws FindFailed {
        App cxphone = App.open("C:\\Program Files (x86)\\3CXPhone\\3CXPhone.exe");
        Screen screen = new Screen();
        org.sikuli.script.Pattern line_3CXLine1 = new org.sikuli.script.Pattern("C:\\SikuliImages\\line_3CXLine1.png");
        org.sikuli.script.Pattern line_3CXLine2 = new org.sikuli.script.Pattern("C:\\SikuliImages\\line_3CXLine2.png");
        org.sikuli.script.Pattern button_3CXHangupCall = new org.sikuli.script.Pattern("C:\\SikuliImages\\button_3CXHangupCall.png");
        org.sikuli.script.Pattern closePhoneWindow = new org.sikuli.script.Pattern("C:\\SikuliImages\\closePhoneWindow.png");
        if (line == 2) {
            screen.wait(line_3CXLine1, 10);
            screen.click(line_3CXLine1);
        }
        /*if(line ==2){
            screen.wait(line_3CXLine2, 10);
            screen.click(line_3CXLine2);
        }*/
        screen.click(button_3CXHangupCall);
        screen.wait(closePhoneWindow, 10);
        screen.click(closePhoneWindow);
    }

    public static WebDriver setWebphoneResultCode(WebDriver driver) throws InterruptedException {
        if (browser == "chrome") {
            WebDriverWait waitForResultCode = new WebDriverWait(driver, 5);
            waitForResultCode.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='Удачно']")));

            WebElement resultCode = driver.findElement(By.xpath("//td[text()='Удачно']"));
            resultCode.click();
            Thread.sleep(1000);
            WebElement button_Save = driver.findElement(By.cssSelector("#btn_rslt > span.ui-button-text.ui-c"));
            button_Save.click();
        } else {
            WebDriverWait waitForResultCode = new WebDriverWait(driver, 5);
            waitForResultCode.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='Удачно']")));

            WebElement resultCode = driver.findElement(By.xpath("//td[text()='Удачно']"));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", resultCode);
            Thread.sleep(1000);
            WebElement button_Save = driver.findElement(By.cssSelector("#btn_rslt > span.ui-button-text.ui-c"));
            executor.executeScript("arguments[0].click();", button_Save);
        }
        return driver;


    }

    public static WebDriver switchToAdTab(WebDriver driver) {
        WebElement adTab = driver.findElement(By.xpath("//a[@href = '#tabView:tab123']"));
        adTab.click();
        return driver;
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        String userName = "GBWebPhoneTest";
        String password = "yt~k$tCW8%Gj";
        String url = "jdbc:sqlserver://172.21.7.225\\\\corporate;DatabaseName=GBWebPhoneTest;portNumber=1438";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection conn = DriverManager.getConnection(url, userName, password);
        return conn;
    }

    public static void updateRecord(Connection con, String dbTable, String dbPhoneNumber) throws SQLException {
        String query;
        query = "INSERT INTO GBWebPhoneTest.dbo." + dbTable + "(phone_number_1)"
                + " VALUES ('" + dbPhoneNumber + "');";
        Statement stmt = con.createStatement();
        stmt.execute(query);
    }

    public static void runSqlQuery(String dbTable, String dbPhoneNumber) throws SQLException, ClassNotFoundException {
        updateRecord(getConnection(), dbTable, dbPhoneNumber);
    }

   /* @Test
    public static void test() throws SQLException, ClassNotFoundException {
        runSqlQuery("pd_5009_3", "94949");
    }*/

    public static WebDriver agentAcceptCall(WebDriver driver) {
        WebDriverWait waitForButtonAccept = new WebDriverWait(driver, 20);
        waitForButtonAccept.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#btn_preview_accept")));
        WebElement button_Accept = driver.findElement(By.cssSelector("#btn_preview_accept"));
        button_Accept.click();
        return driver;
    }

    public static WebDriver saveCRMCard(WebDriver driver) throws FindFailed {
        WebDriverWait waitForIncallStatus = new WebDriverWait(driver, 5);
        waitForIncallStatus.until(ExpectedConditions.textMatches(By.cssSelector(
                "#statusButton > span.ui-button-text.ui-c"), Pattern.compile(".*\\bIncall\\b.*")));

        Screen screen = new Screen();
        org.sikuli.script.Pattern mltest = new org.sikuli.script.Pattern("C:\\SikuliImages\\mltest.png");
        screen.wait(mltest, 30);
        screen.click(mltest);

        org.sikuli.script.Pattern button_OK = new org.sikuli.script.Pattern("C:\\SikuliImages\\button_OK.png");
        screen.wait(button_OK, 10);
        screen.click(button_OK);
        org.sikuli.script.Pattern button_nextForm = new org.sikuli.script.Pattern("C:\\SikuliImages\\button_nextForm.png");
        screen.wait(button_nextForm, 10);
        screen.click(button_nextForm);

        org.sikuli.script.Pattern button_save = new org.sikuli.script.Pattern("C:\\SikuliImages\\button_save.png");
        screen.wait(button_save, 10);
        screen.click(button_save);

        org.sikuli.script.Pattern selectResultCode = new org.sikuli.script.Pattern("C:\\SikuliImages\\selectResultCode.png");
        screen.wait(selectResultCode, 10);
        screen.click(selectResultCode);

        org.sikuli.script.Pattern callLater = new org.sikuli.script.Pattern("C:\\SikuliImages\\callLater.png");
        screen.wait(callLater, 10);
        screen.click(callLater);

        screen.wait(button_save, 10);
        screen.click(button_save);
        return driver;
    }

}
