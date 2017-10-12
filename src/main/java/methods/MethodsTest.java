package methods;

import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;

import static methods.Methods.*;

/**
 * Created by SChubuk on 12.09.2017.
 */
public class MethodsTest {
    static String initialStatus;

    @Test
    public static void initializeVariablesChrome() {
        url = "http://172.21.7.239/gbwebphone/";
        browser = "chrome";
        method = "usual";
        username = "81016";
        password = "1";
        group = "\\!test_group5_5220";
        fast = false;
        initialStatus = "Available";
    }

    @Test(dependsOnMethods = "initializeVariables")
    public static void chromeLogin() throws InterruptedException {
        initializeVariablesChrome();
        loadPage();
        login();
        handlePluginWindow();
        checkStatus(initialStatus);
    }

    @Test
    public static void initializeVariablesIE() {
        url = "http://172.21.24.109/gbwebphone/";
        browser = "ie";
        method = "usual";
        username = "81016";
        password = "1";
        group = "\\!test_group5_5220";
        fast = false;
        initialStatus = "Available";
    }

    @Test(dependsOnMethods = "initializeVariables")
    public static void IELogin() throws InterruptedException {
        initializeVariablesIE();
        loadPage();
        login();
        handlePluginWindow();
        checkStatus(initialStatus);
    }

    @Test
    public static void initializeVariablesIEAD()  {
        url = "http://172.21.24.109/gbwebphone/";
        browser = "ie";
        method = "usual";
        username = "81016";
        password = "1";
        group = "pasha_G_5_copy_preview";
        fast = false;


        try {
            String myString = "Тренинг";
            byte bytes[] = new byte[0];
            String value = new String(bytes, "UTF-16");
            bytes = myString.getBytes("ISO-8859-1");
            initialStatus = value;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            initialStatus = "Тренинг";
        }


        /*initialStatus = "Тренинг";*/
    }

    @Test(dependsOnMethods = "initializeVariablesIEAD")
    public static void IELoginAD() throws InterruptedException {
        initializeVariablesIEAD();
        loadPage();
        login();
        handlePluginWindow();
        checkStatus(initialStatus);
    }

    }
