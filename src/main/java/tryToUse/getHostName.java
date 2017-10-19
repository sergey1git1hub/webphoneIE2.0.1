package tryToUse;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by SChubuk on 13.10.2017.
 */
public class getHostName {
    public static void main(String[] args) throws UnknownHostException {
        String hostName = InetAddress.getLocalHost().getHostName();
        System.out.println(hostName);

    }
}
