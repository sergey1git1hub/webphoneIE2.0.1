import org.openqa.selenium.WebDriver;
import org.sikuli.script.FindFailed;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by SChubuk on 04.10.2017.
 */
public class PDProgressiveReleasedAgentHangup {
    static IEData data;
    static WebDriver driver;
    static boolean debug = true;
    @Test
    public static void IELogin() throws InterruptedException, IOException {
        data = new IEData();
        data.group = "\\!test_group5_5220";
        driver = Methods.openWebphoneLoginPage(driver, data.browser, data.webphoneUrl);
        Methods.login(driver, data.method, data.username, data.group);
        Methods.checkStatus(driver, "Available", 10);
    }

    @Test(dependsOnMethods = "IELogin")
    public static void changeStatusToAUX() {
        Methods.changeStatus(driver, "AUX");
        Methods.checkStatus(driver, "AUX", 3);
    }

    @Test()
    public static void loginToPD(){

    }

    @Test
    public static void runPDCampaign(){

    }

    @Test(dependsOnMethods = "changeStatusToAUX")
    public static void runSQLQuery() throws SQLException, ClassNotFoundException {
        Methods.runSqlQuery("pd_5220copy", "94949");
    }

    @Test(dependsOnMethods = "runSQLQuery")
    public static void waitForCallOnClientSide(){
    }

    @Test(dependsOnMethods = "waitForCallOnClientSide")
    public static void noIncomingCallToAgent() throws InterruptedException {
        if(debug == true)
        Thread.sleep(5000);
        else Thread.sleep(20000);
    }

    @Test(dependsOnMethods = "noIncomingCallToAgent")
    public static void changeStatusToAvailable(){
        Methods.changeStatus(driver, "Available");
        Methods.checkStatus(driver, "Available", 3);
    }

    @Test(dependsOnMethods = "changeStatusToAvailable")
    public static void waitForCallOnClientSide2() throws FindFailed, InterruptedException {
        Methods.cxAnswer();
    }

    @Test(dependsOnMethods = "waitForCallOnClientSide2")
    public static void receiveIncomingCallToAgent(){

    }

    @Test(dependsOnMethods = "receiveIncomingCallToAgent")
    public static void agentHangup() throws InterruptedException, FindFailed {
        Methods.agentHangup(driver, 1);
    }

    @Test(dependsOnMethods = "agentHangup")
    public static void setResultCodeAndCheckAvailableStatus() throws InterruptedException {
        Methods.setWebphoneResultCode(driver);
        Methods.checkStatus(driver, "Available", 3);
    }

    @AfterClass
    public void teardown() {

    }
}
