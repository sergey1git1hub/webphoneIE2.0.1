import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.sikuli.script.FindFailed;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Created by SChubuk on 05.10.2017.
 */

public class CallOnTwoLines {
    static IEData data;
    static WebDriver driver;


    public static void createData() {
        data = new IEData();
        data.group = "\\!test_group5_5220";
        Methods.browser = "ie";
        Methods.onJenkins = false;
    }


    public static void IELogin() throws InterruptedException, IOException {
        driver = Methods.openWebphoneLoginPage(driver, data.browser, data.webphoneUrl);
        Methods.login(driver, data.method, data.username, data.group);
        Methods.checkStatus(driver, "Available", 30);
    }


    public static WebDriver callOnFirstLine() throws FindFailed, InterruptedException {
        Methods.openCXphone(2000);
        Methods.call(driver, 1, "94949");
        Methods.cxAnswer();
        return driver;
    }


    public static WebDriver callOnSecondLine() throws FindFailed, InterruptedException {
        Methods.call(driver, 2, "94948");
        Methods.cxAnswer();
        return driver;
    }

    public static void callOnTwoLines() throws InterruptedException, IOException, FindFailed {
        createData();
        IELogin();
        callOnFirstLine();
        callOnSecondLine();
    }

    public static void setResultCodeAndCheckAvailableStatus() throws InterruptedException, FindFailed, UnknownHostException {
        Methods.setWebphoneResultCode(driver);
        Methods.checkStatus(driver, "Available", 3);

    }

}
