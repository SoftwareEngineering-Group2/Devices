import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Serial connection
        /*SerialConnection serial = new SerialConnection();
        serial.serialConnect();*/

        // Http connection
        HttpConnection http = new HttpConnection();
        http.HttpConnect();
    }
}
