import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.App;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

import static data.Data.PDUrl;
import static data.Data.agentChrome;
import static data.Data.agentPD;
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
        System.out.println("openWebphoneLoginPage");
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
                        System.out.println("openWebphoneLoginPage.updateJavaLater");
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
                        System.out.println("openWebphoneLoginPage.DoYouWantToRunThisApplication");
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
        System.out.println("login");
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
        System.out.println("checkStatus");
        WebDriverWait waitForStatus = new WebDriverWait(driver, waitTime);
        waitForStatus.until(ExpectedConditions.textMatches(By.cssSelector(
                "#statusButton > span.ui-button-text.ui-c"), Pattern.compile(".*\\b" + status + "\\b.*")));
        WebElement currentStatus = driver.findElement(By.cssSelector(
                "#statusButton > span.ui-button-text.ui-c"));
        Assert.assertTrue(currentStatus.getText().contains(status));
        return driver;
    }

    public static WebDriver changeStatus(WebDriver driver, String status) throws UnknownHostException, FindFailed, InterruptedException {
        System.out.println("changeStatus");
        String hostName = InetAddress.getLocalHost().getHostName();
        if(hostName.equalsIgnoreCase("kv1-it-pc-jtest")){
            if(status.equalsIgnoreCase("Available")){
            Screen screen = new Screen();
            org.sikuli.script.Pattern currentStatus = new org.sikuli.script.Pattern("C:\\SikuliImages\\currentStatus.png");
            screen.wait(currentStatus, 10);
            Thread.sleep(1000);
            screen.click(currentStatus);
            Thread.sleep(1000);
            org.sikuli.script.Pattern availableStatus = new org.sikuli.script.Pattern("C:\\SikuliImages\\availableStatus.png");
            screen.wait(availableStatus, 10);
            screen.click(availableStatus);
            }
            if(status.equalsIgnoreCase("AUX")){
                Screen screen = new Screen();
                org.sikuli.script.Pattern currentStatus = new org.sikuli.script.Pattern("C:\\SikuliImages\\currentStatus.png");
                screen.wait(currentStatus, 10);
                //Thread.sleep(2000);
                screen.click(currentStatus);
                Thread.sleep(1000);
                org.sikuli.script.Pattern auxStatus = new org.sikuli.script.Pattern("C:\\SikuliImages\\auxStatus.png");
                screen.wait(auxStatus, 10);
                screen.click(auxStatus);
            }
            checkStatus(driver, status, 2);
        } else
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
        System.out.println("switchLine");
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
                e.printStackTrace();
            }
        }
        return driver;
    }

    public static WebDriver call(WebDriver driver, int line, String number) throws FindFailed, InterruptedException, UnknownHostException {
        String hostName = InetAddress.getLocalHost().getHostName();

        System.out.println("call");
        switchLine(driver, line);
        Thread.sleep(500);
        WebElement phoneNumberField = driver.findElement(By.cssSelector("#PhoneNumber"));
        phoneNumberField.sendKeys(number);
        WebElement button_Call = driver.findElement(By.cssSelector("#btn_call"));
        if(hostName.equalsIgnoreCase("kv1-it-pc-jtest")){

            /*Screen screen = new Screen();
            screen.wait();
            screen.click();*/
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", button_Call);
        } else{
        button_Call.click();
    }
        return driver;

    }

    public static void openCXphone(int waitTime) throws FindFailed, InterruptedException {
        System.out.println("openCXphone");
        App cxphone = App.open("C:\\Program Files (x86)\\3CXPhone\\3CXPhone.exe");
        Thread.sleep(waitTime);
        Screen screen = new Screen();

        org.sikuli.script.Pattern closePhoneWindow = new org.sikuli.script.Pattern("C:\\SikuliImages\\closePhoneWindow.png");
        screen.wait(closePhoneWindow, 10);
        screen.click(closePhoneWindow);
    }

    public static void cxAnswer() throws FindFailed, InterruptedException {
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

        System.out.println("agentHangup");
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

    public static WebDriver setWebphoneResultCode(WebDriver driver) throws InterruptedException, UnknownHostException, FindFailed {
        System.out.println("setWebphoneResultCode");
        String hostName = InetAddress.getLocalHost().getHostName();
        if(hostName.equalsIgnoreCase("kv1-it-pc-jtest")){
            Screen screen = new Screen();
            org.sikuli.script.Pattern resultCodeUdachno = new org.sikuli.script.Pattern("C:\\SikuliImages\\resultCodeUdachno.png");
            screen.wait(resultCodeUdachno, 10);
            screen.click(resultCodeUdachno);
            Thread.sleep(1000); //necessary
            WebElement button_Save = driver.findElement(By.cssSelector("#btn_rslt > span.ui-button-text.ui-c"));
            button_Save.click();
        } else
        if (isIE(driver) == false) {
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
        System.out.println("switchToAdTab");
        WebElement adTab = driver.findElement(By.xpath("//a[@href = '#tabView:tab123']"));
        adTab.click();
        return driver;
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        System.out.println("getConnection");
        String userName = "GBWebPhoneTest";
        String password = "yt~k$tCW8%Gj";
        String url = "jdbc:sqlserver://172.21.7.225\\\\corporate;DatabaseName=GBWebPhoneTest;portNumber=1438";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection conn = DriverManager.getConnection(url, userName, password);
        return conn;
    }

    public static void updateRecord(Connection con, String dbTable, String dbPhoneNumber) throws SQLException {
        System.out.println("updateRecord");
        String query;
        query = "INSERT INTO GBWebPhoneTest.dbo." + dbTable + "(phone_number_1)"
                + " VALUES ('" + dbPhoneNumber + "');";
        Statement stmt = con.createStatement();
        stmt.execute(query);
    }

    public static void runSqlQuery(String dbTable, String dbPhoneNumber) throws SQLException, ClassNotFoundException {
        System.out.println("runSqlQuery");
        updateRecord(getConnection(), dbTable, dbPhoneNumber);
    }

   /* @Test
    public static void test() throws SQLException, ClassNotFoundException {
        runSqlQuery("pd_5009_3", "94949");
    }*/

    public static WebDriver agentAcceptCall(WebDriver driver, int waitTime) throws InterruptedException {
        System.out.println("agentAcceptCall");
        WebDriverWait waitForButtonAccept = new WebDriverWait(driver, waitTime);
        System.out.println("WebDriverWait waitForButtonAccept = new WebDriverWait(driver, waitTime);");
        waitForButtonAccept.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#btn_preview_accept")));
        System.out.println("waitForButtonAccept.until(ExpectedConditions.elementToBeClickable(By.cssSelector(\"#btn_preview_accept\")));");
        WebElement button_Accept = driver.findElement(By.cssSelector("#btn_preview_accept"));
        System.out.println("WebElement button_Accept = driver.findElement(By.cssSelector(\"#btn_preview_accept\"));");
       //if not wait, CRM card not opened
        Thread.sleep(500);
        if(isIE(driver) == true){
            System.out.println("if(isIE(driver) == true){" + isIE(driver));
            clickIEelement(driver, button_Accept);
            System.out.println("clickIEelement(button_Accept);");
        } else{
            System.out.println("} else{");
        button_Accept.click();
            System.out.println("button_Accept.click();");
        }
        return driver;
    }

    public static WebDriver saveCRMCard(WebDriver driver) throws FindFailed, InterruptedException {
        System.out.println("saveCRMCard");
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
        Thread.sleep(2000);
        screen.wait(button_save, 10);
        screen.click(button_save);
        return driver;
    }

    public static WebDriver loginToPD() throws InterruptedException {
        System.out.println("loginToPD");
        System.setProperty("webdriver.chrome.driver", "C:/chromedriver/chromedriver.exe");
        WebDriver agentPD = new ChromeDriver();
        agentPD.manage().window().maximize();
        agentPD.get(PDUrl);

        System.out.println(agentPD.getTitle());
 /*       if(agentPD.getTitle() == "Доступ запрещён!"){
            System.out.println("Inside if");
            agentPD.navigate().refresh();
        }*/
        Thread.sleep(3000);
        agentPD.navigate().refresh();
        Assert.assertEquals(agentPD.getTitle(), "gbpowerdialer");

        Thread.sleep(1000);
        WebElement username = agentPD.findElement(By.cssSelector("#username"));
        username.sendKeys("81016");
        WebElement password = agentPD.findElement(By.cssSelector("#password"));
        password.sendKeys("1");
        WebElement button_SignIn = agentPD.findElement(By.cssSelector("#loginModal > div > div > form > div.modal-footer > button"));
        button_SignIn.click();
        return agentPD;
    }
    public static void runPDCampaign(WebDriver agentPD, int campaignId) throws InterruptedException {
        System.out.println("runPDCampaign");
        Thread.sleep(2000);

/*        WebDriverWait waitForId = new WebDriverWait(agentPD, 2);
        waitForId.until(ExpectedConditions.elementToBeClickable(By.xpath("/*//*[text() = '257']")));*/
        WebElement columns = agentPD.findElement(By.xpath("//*[@id=\"campaignGrid\"]/div/div[1]"));
        columns.click();
        Thread.sleep(1000);

        WebElement id = agentPD.findElement(By.xpath("//*[@id=\"menuitem-13\"]/button"));
        id.click();
        Thread.sleep(1000);
        WebElement running = agentPD.findElement(By.xpath("//*[text() = '" + campaignId + "']//parent::div//following-sibling::div[3]//div"));


        System.out.println(running.getText());
        if (!running.getText().equals("Running")) {
            WebElement currentCampaign = agentPD.findElement(By.xpath("//*[text() = '" + campaignId + "']"));
            currentCampaign.click();
            WebElement icon_Enable = agentPD.findElement(By.cssSelector("#navbarCompainList > div > div:nth-child(7) > button"));
            icon_Enable.click();
            WebElement button_Enable = agentPD.findElement(By.cssSelector("#navbarCompainList > div > div.btn-group.open > ul > li:nth-child(1) > a"));
            button_Enable.click();
            Thread.sleep(1000);
            WebElement button_Start = agentPD.findElement(By.cssSelector("#navbarCompainList > div > button.btn.btn-success.btn-sm.navbar-btn"));
            button_Start.click();
        }
        agentPD.quit();
    }

    public static boolean isIE(WebDriver driver){
        System.out.println("isIE");
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = cap.getBrowserName().toLowerCase();
        System.out.println(browserName);
        if(browserName.equals("internet explorer"))
        return true; else return false;
    }

     public static void clickIEelement(WebDriver driver, WebElement element)   {
         System.out.println("clickIEelement");
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
     }

}
