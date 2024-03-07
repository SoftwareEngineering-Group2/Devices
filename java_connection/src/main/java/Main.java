import com.google.gson.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Serial connection
        SerialConnection serial = new SerialConnection();
        /*for (int i = 0; i < 5; i++) {
            serial.data = 255;
            serial.serialConnect();
            Thread.sleep(2000);
        }*/

        serial.data = 255;
        serial.serialConnect();

        // Http connection
        /*HttpConnection http = new HttpConnection();
        http.HttpConnect();

        String responseString = http.response.toString();
        JsonObject jsonObject = JsonParser.parseString(responseString).getAsJsonObject();

        System.out.println(jsonObject);

        String deviceMessage = jsonObject.get("message").getAsString();
        System.out.println("Value: " + deviceMessage);
        String deviceStatus = jsonObject.get("state").getAsString();
        System.out.println("State: " + deviceStatus);

        if (deviceStatus.equals("false")) {
            // Send byte to house
            System.out.println("HEJ");
            serial.data = 66;
            serial.serialConnect();
        }*/
    }
}
