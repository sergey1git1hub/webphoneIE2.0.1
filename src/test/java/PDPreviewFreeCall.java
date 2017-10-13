import org.openqa.selenium.WebDriver;
import org.sikuli.script.FindFailed;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by SChubuk on 04.10.2017.
 */
public class PDPreviewFreeCall {
    static Data data;
    static WebDriver driver;

    @Test
    public static void pDPreviewFreeCall() throws InterruptedException, IOException, FindFailed, SQLException, ClassNotFoundException {
        PreviewFree.createData();
        PreviewFree.IELoginAD();
        PreviewFree.changeStatusToAvailable();
        Methods.switchToAdTab(PreviewFree.driver);
        Methods.runSqlQuery("pd_5009_3", "94949");
        PreviewFree.processCall();
    }

    @AfterClass
    public void teardown() throws IOException {
        boolean isIE = Methods.isIE(PreviewFree.driver);
        PreviewFree.driver.quit();

        if(isIE){
            Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
        }
    }
}
