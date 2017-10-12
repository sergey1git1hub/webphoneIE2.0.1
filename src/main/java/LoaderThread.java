import org.openqa.selenium.WebDriver;

/**
 * Created by SChubuk on 10.10.2017.
 */
public class LoaderThread extends Thread {

    private WebDriver driver;
    private String webphoneUrl;

    public LoaderThread(WebDriver driver, String webphoneUrl){
        this.driver = driver;
        this.webphoneUrl = webphoneUrl;
    }

    public void run() {
        driver.get(webphoneUrl);
    }

}
