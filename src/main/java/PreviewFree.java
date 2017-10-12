import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.sikuli.script.FindFailed;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by SChubuk on 04.10.2017.
 */
public class PreviewFree {

    static IEData data;
    static WebDriver driver;

    public static void createData() {
        data = new IEData();
        data.group = "pasha_G_5_copy_preview";
        Methods.browser = "ie";
        Methods.onJenkins = false;
    }

    public static void IELoginAD() throws InterruptedException, IOException {
        driver = Methods.openWebphoneLoginPage(driver, data.browser, data.webphoneUrl);
        Methods.login(driver, data.method, data.username, data.group);
        Methods.checkStatus(driver, "Тренинг", 10);
    }


    public static void changeStatusToAvailable(){
        Methods.changeStatus(driver, "Available");
        Methods.checkStatus(driver, "Available", 3);

    }

    public static void changeStatusToAUX(){
        Methods.changeStatus(driver, "AUX");
        Methods.checkStatus(driver, "AUX", 3);
    }


    public static void processCall() throws InterruptedException, FindFailed {
        Methods.agentAcceptCall(driver);
        Methods.cxAnswer();
        Methods.saveCRMCard(driver);
        Methods.checkStatus(driver, "Relax", 3);
        Methods.checkStatus(driver, "Available", 6);
    }

}
