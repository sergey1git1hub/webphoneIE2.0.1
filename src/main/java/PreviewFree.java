import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.sikuli.script.FindFailed;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

/**
 * Created by SChubuk on 04.10.2017.
 */
public class PreviewFree {

    static IEData data;
    static WebDriver driver;

    public static void createData() {
        data = new IEData();
        data.group = "pasha_G_5_copy_preview";
        data.webphoneUrl = "http://172.21.7.239/gbwebphone/";
        Methods.browser = "ie";
        Methods.onJenkins = false;
    }

    public static void IELoginAD() throws InterruptedException, IOException {
        driver = Methods.openWebphoneLoginPage(driver, data.browser, data.webphoneUrl);
        Methods.login(driver, data.method, data.username, data.group);
        Methods.checkStatus(driver, "Тренинг", 10);
    }


    public static void changeStatusToAvailable() throws InterruptedException, FindFailed, UnknownHostException, UnsupportedEncodingException {
        Methods.changeStatus(driver, "Available");
        Methods.checkStatus(driver, "Available", 3);

    }

    public static void changeStatusToAUX() throws InterruptedException, FindFailed, UnknownHostException, UnsupportedEncodingException {
        Methods.changeStatus(driver, "AUX");
        Methods.checkStatus(driver, "AUX", 3);
    }


    public static void processCall() throws InterruptedException, FindFailed, UnsupportedEncodingException, UnknownHostException {
        try{
            System.out.println("try{");
            Methods.openCXphone(5000);
        Methods.agentAcceptCall(driver, 30);
        System.out.println("Methods.agentAcceptCall(driver, 30);");
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("} catch(Exception e){");
            WebDriver driverTemp = Methods.loginToPD();
            System.out.println("WebDriver driverTemp = Methods.loginToPD();");
            Methods.runPDCampaign(driverTemp, 252);
            System.out.println("Methods.runPDCampaign(driverTemp, 252);");
            Methods.agentAcceptCall(driver, 30);
            System.out.println("Methods.agentAcceptCall(driver, 30);");
        }
        Methods.cxAnswer();
        System.out.println("Methods.cxAnswer();");
        Methods.saveCRMCard(driver);
        Methods.checkStatus(driver, "Relax", 3);
        Methods.checkStatus(driver, "Available", 6);
    }

}
