import org.openqa.selenium.WebDriver;
import org.sikuli.script.FindFailed;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by SChubuk on 04.10.2017.
 */
public class TwoLinesClientHangup {
    static Data data;
    static WebDriver driver;
    static boolean fast = false;
    static int delay = 2;

    @Test
    public static void twoLinesClientHangup() throws InterruptedException, IOException, FindFailed {
        CallOnTwoLines.callOnTwoLines();
        driver = CallOnTwoLines.driver;
        data = CallOnTwoLines.data;
        if(fast == false){
        Methods.clientHangup(driver, 1);
        Thread.sleep(delay);
        Methods.clientHangup(driver, 2);
        Thread.sleep(delay);
        CallOnTwoLines.setResultCodeAndCheckAvailableStatus();
        } else{
            Methods.clientHangup(driver, 1);
            Methods.clientHangup(driver, 2);
            CallOnTwoLines.setResultCodeAndCheckAvailableStatus();
        }

    }

    @AfterClass
    public void teardown() throws IOException {
        boolean isIE = Methods.isIE(driver);
        driver.quit();

        if(isIE){
            Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
        }
    }
    }


