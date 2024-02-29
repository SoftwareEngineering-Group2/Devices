import com.google.gson.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Serial connection
        SerialConnection serial = new SerialConnection();

        // Http connection
        HttpConnection http = new HttpConnection();
        http.HttpConnect();

        String responseString = http.response.toString();
        JsonObject jsonObject = JsonParser.parseString(responseString).getAsJsonObject();

        String value1 = jsonObject.get("hej2").getAsString();
        System.out.println("Value: " + value1);

        if (value1.equals("hej3")) {
            // Send byte to house
            serial.data = 13;
            serial.serialConnect();
        }
    }
}
