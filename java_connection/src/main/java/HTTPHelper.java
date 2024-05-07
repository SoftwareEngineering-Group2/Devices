import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class HTTPHelper {

    final static int NUM_CONNECTIONS = 5;
    public int responseCode;
    public StringBuffer response;
    //HttpURLConnection connection;
    HttpURLConnection[] connectionList = new HttpURLConnection[7];
    public void HttpConnect() {
        try {

            String[] sensorList = {"gasSensor", "lightSensor", "steamSensor", "moistureSensor", "motionSensor", "leftButton", "rightButton"};
            //String[] sensorList = {"rightButton", "leftButton", "motionSensor", "moistureSensor", "steamSensor", "lightSensor", "gasSensor" };

            String pwdkey = System.getenv("PWDKEY");
            System.out.println(pwdkey);

            for (int i = 0; i < NUM_CONNECTIONS; i++) {

                String baseURL = "https://server-o8if.onrender.com/device/" + sensorList[i] + "/" + pwdkey;
                URL url = new URL(baseURL);

                // Open connection
                connectionList[i] = (HttpURLConnection) url.openConnection();

                connectionList[i].setRequestMethod("POST");
                connectionList[i].setRequestProperty("Content-Type", "application/json");
                connectionList[i].setDoOutput(true);

            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void httpDisconnect() {

        for (int i = 0; i < NUM_CONNECTIONS; i++) {

            connectionList[i].disconnect();

        }

    }

    public void getRequest(HttpURLConnection connection) {
        try {
            // Request method
            connection.setRequestMethod("GET");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postRequest(HttpURLConnection connection) {
        try {
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json");

            connection.setDoOutput(true);
            String requestBody = "{\"hej2\": \"hej3\"}";
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                byte[] data = requestBody.getBytes(StandardCharsets.UTF_8);
                wr.write(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postSensorRequest(HttpURLConnection connection, boolean state) {
        try {

            // Create JSON request body based on the state boolean
            String requestBody = "{\"newState\": \"" + (state ? "true" : "false") + "\"}";
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                byte[] data = requestBody.getBytes(StandardCharsets.UTF_8);
                wr.write(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putRequest(HttpURLConnection connection) {
        try {
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json");

            connection.setDoOutput(true);
            String requestBody = "{\"tester\": \"testus\"}";
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                byte[] data = requestBody.getBytes(StandardCharsets.UTF_8);
                wr.write(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject convertArrayToJSONandSend(int[] result) {

        JSONObject obj;

        for (int i = 0; i < result.length; i++) {

            if(result[7] == 1)    // gasSensor true
                postSensorRequest(connectionList[0], true);
            else
                postSensorRequest(connectionList[0], false);
            if(result[6] == 1)    // lightSensor true
                postSensorRequest(connectionList[1], true);
            else
                postSensorRequest(connectionList[1], false);
            if(result[5] == 1)    // steamSensor true
                postSensorRequest(connectionList[2], true);
            else
                postSensorRequest(connectionList[2], false);
            if(result[4] == 1)    // moistureSensor true
                postSensorRequest(connectionList[3], true);
            else
                postSensorRequest(connectionList[3], false);
            if(result[3] == 1)    // motionSensor true
                postSensorRequest(connectionList[4], true);
            else
                postSensorRequest(connectionList[4], false);

        }

        for (int i = 0; i < NUM_CONNECTIONS; i++) {
            printResponse(connectionList[i]);
        }

        httpDisconnect();
        HttpConnect();

        return null;
    }

    public void printResponse(HttpURLConnection connection) {

        try {

            responseCode = connection.getResponseCode();
            System.out.println("Response code: " + responseCode);

            // Read response
            BufferedReader reader = null;

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            response = new StringBuffer();
            while (true) {
                if (!((inputLine = reader.readLine()) != null)) break;
                response.append(inputLine);
            }

            reader.close();
            System.out.println("Response: " + response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
