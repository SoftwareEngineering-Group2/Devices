import java.io.BufferedReader;
import java.io.DataOutputStream;
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

            String pwdkey = System.getenv("PWDKEY");
            System.out.println(pwdkey);

            for (int i = 0; i < NUM_CONNECTIONS; i++) {

                String baseURL = "https://server-o8if.onrender.com/device/" + sensorList[i] + pwdkey;
                URL url = new URL(baseURL);
                // Open connection
                //connection = (HttpURLConnection) url.openConnection();

                connectionList[i] = (HttpURLConnection) url.openConnection();


            }


            // CALL FUNCTION FOR THE CONNECTION HERE
            //getRequest(connection);
            //postRequest(connection);
            //putRequest(connection);

            // Get response code
            /*
            responseCode = connection.getResponseCode();
            System.out.println("Response code: " + responseCode);

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();
*/
            //System.out.println("Response: " + response);



            // Close connection
            //connection.disconnect();
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
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json");

            connection.setDoOutput(true);
            //String requestBody = "{\"newState\": \"hej3\"}";
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

    public JSONObject convertArrayToJSON(int[] result) {

        JSONObject obj;

        for (int i = 0; i < result.length; i++) {

            switch (i) {

                case 0 : // gas
                    if(result[i] == 0) {
                        // add gasSensor false
                        postSensorRequest(connectionList[i], false);
                    }
                    else {
                        // gasSensor true
                    }
                    break;
                case 1 : // light
                    if(result[i] == 0) {
                        // add gasSensor false
                    }
                    else {
                        // gasSensor true
                    }
                    break;
                case 2 : // steam
                    if(result[i] == 0) {
                        // add gasSensor false
                    }
                    else {
                        // gasSensor true
                    }
                    break;
                case 3 : // moisture
                    if(result[i] == 0) {
                        // add gasSensor false
                    }
                    else {
                        // gasSensor true
                    }
                    break;
                case 4 : // motion
                    if(result[i] == 0) {
                        // add gasSensor false
                    }
                    else {
                        // gasSensor true
                    }
                    break;

            }


        }

        return null;
    }

    public void sendSensorDataHTTP(JSONObject obj) {

        for (int i = 0; i < NUM_CONNECTIONS; i++) {

            postRequest(connectionList[i]);

        }

    }

}
