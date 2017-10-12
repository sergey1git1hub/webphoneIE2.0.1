import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Screen;
import org.testng.Assert;

/**
 * Created by SChubuk on 04.10.2017.
 */

public class MethodsTest {

    public static WebDriver openWebphoneLoginPage(WebDriver driver, String browser, final String webphoneUrl) throws InterruptedException {

            Thread thread1 = new LoaderThread(driver,webphoneUrl);
            thread1.start();
        return driver;
    }

}