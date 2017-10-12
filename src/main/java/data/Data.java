package data;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

/**
 * Created by SChubuk on 22.08.2017.
 */
public class Data {
    public static String webphoneUrl = "http://172.21.7.239/gbwebphone/";
   // public static String webphoneUrl = "http://172.21.24.109/gbwebphone/";
    public static String PDUrl = "http://172.21.24.109:8087/gbpowerdialer/#/campaignList";
    public static String webchatUrl = "http://172.21.7.239:8080";
    public static String webchatServerUrl = "http://172.21.7.239:8080";
    public static String CRMUrl = "http://172.21.24.109:8085/agentdesk/cardDataForm?companyAlias=MLytvynovTest&phoneNumber=380930000000";
    public static String webchatClientUrl = "http://172.21.7.239:8080/api/external/workgroupProvider/direct/100";

    public static WebDriver agentChrome;
    public static WebDriver agentPD;
    public static WebDriver driver;
    public static WebDriver agentDriver;
    public static WebDriver agentCRM;
    public static WebDriver agentIE;
    public static WebDriver adminDriver;
    public static WebDriver clientDriver;



    /*
    AdminPage
    CheckSatusAfterLogin
    CheckStatusAfterLoginIE1_4_3
    CheckStatusAfterLoginWithLogs
    HelpMethods
    InteractWithCRMCard
    LoginToPD


    PDPreviewFreeAUX


    */

    /*
    * divide into packages
    * webchat
    * webphone
    * webchat&webphone
    * webphone&agentdesktop
    * powerdialer
    * webphone&powerdialer
    * webphone&agentdesktop&powerdialer
    *agentdesktop
    *
    *
    * */

    @Test
    public void test(){
        String str1 = "sometext";
        System.out.println(str1);
        str1 = "othertext";
        System.out.println(str1);
    }
}
