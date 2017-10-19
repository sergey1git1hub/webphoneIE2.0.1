package tryToUse;

import java.io.IOException;

/**
 * Created by schubuk on 19.10.2017.
 */
public class runAppViaJava {
    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().exec("C:\\Program Files (x86)\\3CXPhone\\3CXPhone.exe");
    }
}
