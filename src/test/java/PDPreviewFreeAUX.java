import org.openqa.selenium.WebDriver;
import org.sikuli.script.FindFailed;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by SChubuk on 04.10.2017.
 */
public class PDPreviewFreeAUX {
    static Data data;
    static WebDriver driver;

    @Test
    public static void pDPreviewFreeAUX() throws InterruptedException, IOException, FindFailed, SQLException, ClassNotFoundException {
        PreviewFree.createData();
        PreviewFree.IELoginAD();
        Methods.switchToAdTab(PreviewFree.driver);
        PreviewFree.changeStatusToAUX();
        Methods.runSqlQuery("pd_5009_3", "94949");
        Thread.sleep(20000);
        PreviewFree.changeStatusToAvailable();
        //no incoming call
        PreviewFree.processCall();
    }

    @AfterClass
    public void teardown(){
        PreviewFree.driver.quit();
    }
}
