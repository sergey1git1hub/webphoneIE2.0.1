package helpMethods;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileFilter;

import static data.Data.webchatServerUrl;
import static org.testng.Assert.assertEquals;

/**
 * Created by SChubuk on 01.08.2017.
 */
public class HelpMethods {
    //place for dataprovider
    //public static WebDriver LoginToWebphnoneViaSSO(WebDriver driver, String[] parameters){
    public static WebDriver LoginToWebphnoneViaSSO(WebDriver driver, String username, String password, String group, String initialStatus){
        return driver;
    }

    public static WebDriver login(WebDriver driver) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:/chromedriver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(webchatServerUrl);
        assertEquals(driver.getTitle(), "gbwebchat");

        WebElement name = driver.findElement(By.cssSelector("[name=username]"));
        WebElement password = driver.findElement(By.cssSelector("[name=password]"));

        WebElement button_login = driver.findElement(By.cssSelector("body > app-root > " +
                "md-sidenav-container > div.mat-sidenav-content > md-card > app-login-detail > " +
                "div > form > div:nth-child(3) > button"));


        name.sendKeys("81016");
        password.sendKeys("1");
        button_login.click();
        Thread.sleep(1000);
        return driver;
    }

    public static WebDriver handleLogoutWindow(WebDriver driver) {
        try {
            WebDriverWait waitForLogoutWindow = new WebDriverWait(driver, 2);
            waitForLogoutWindow.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("#userLogoutForm\\3a btn_userlogout_yes > span.ui-button-text.ui-c")));
            WebElement button_Yes = driver.findElement(By.cssSelector("#userLogoutForm\\3a btn_userlogout_yes > span.ui-button-text.ui-c"));
            button_Yes.click();
        } catch (ElementNotVisibleException e) {
            System.out.println(e);

        }catch (TimeoutException e){
        }
        return driver;
    }

    public static WebDriver handleSecurityWarning(WebDriver driver) {
        String parentHandle = driver.getWindowHandle(); // get the current window handle

        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle); // switch focus of WebDriver to the next found window handle (that's your newly opened window)
        }

        String openDevTools = Keys.chord(Keys.ENTER);
        driver.findElement(new By.ByTagName("body")).sendKeys(openDevTools);


        driver.close(); // close newly opened window when done with it
        driver.switchTo().window(parentHandle); // switch back to the original window
        return driver;
    }
    public void clickEditButton(WebDriver adminDriver) {
        WebElement button_Edit = adminDriver.findElement(By.xpath("body > app-root > md-sidenav-container > div.mat-sidenav-content > md-card > app-workgroup-list > div > div:nth-child(1) > div:nth-child(2) > div > div:nth-child(2) > button:nth-child(1) >" +
                " div.mat-button-ripple.mat-ripple"));
        button_Edit.click();
    }

    public static File lastFileModified(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (File file : files) {
            if (file.lastModified() > lastMod) {
                choice = file;
                lastMod = file.lastModified();
            }
        }
        return choice;
    }

}
