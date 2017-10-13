import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sikuli.script.FindFailed;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

import static data.Data.agentChrome;

/**
 * Created by SChubuk on 04.10.2017.
 */

public class TwoLinesAgentHangup {
    static WebDriver driver;
    static Data data;

    @Test
    public static void twoLinesAgentHangup() throws InterruptedException, IOException, FindFailed {
        CallOnTwoLines.callOnTwoLines();
        driver = CallOnTwoLines.driver;
        data = CallOnTwoLines.data;
        Thread.sleep(1000);
        WebElement button_Hold = driver.findElement(By.cssSelector("#btn_hold"));
        button_Hold.click();
        Thread.sleep(1000);
        Methods.agentHangup(driver, 1);
        Methods.agentHangup(driver, 2);
        CallOnTwoLines.setResultCodeAndCheckAvailableStatus();
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

