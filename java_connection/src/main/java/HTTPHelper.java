import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class HTTPHelper {

    final static int NUM_CONNECTIONS = 5;
    public int responseCode;
    public StringBuffer response;

    HttpURLConnection[] connectionList = new HttpURLConnection[7];
    HttpURLConnection connection2, connectionOutput;
    String pwdkey = System.getenv("PWDKEY");

    public void HttpConnect() {
        try {

            String[] sensorList = {"gasSensor", "lightSensor", "steamSensor", "moistureSensor", "motionSensor", "leftButton", "rightButton"};


            System.out.println(pwdkey);

            for (int i = 0; i < NUM_CONNECTIONS; i++) {

                String baseURL = "https://server-o8if.onrender.com/static/device/" + sensorList[i] + "/" + pwdkey;

                URL url = new URL(baseURL);

                // Open connection
                connectionList[i] = (HttpURLConnection) url.openConnection();

                connectionList[i].setRequestMethod("POST");
                connectionList[i].setRequestProperty("Content-Type", "application/json");
                connectionList[i].setDoOutput(true);


            }

            String baseURL2 = "https://server-o8if.onrender.com/sensor/" + pwdkey;
            URL url2 = new URL(baseURL2);

            connection2 = (HttpURLConnection) url2.openConnection();

            connection2.setRequestMethod("POST");
            connection2.setRequestProperty("Content-Type", "application/json");
            connection2.setDoOutput(true);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void httpDisconnect() {

        for (int i = 0; i < NUM_CONNECTIONS; i++) {

            connectionList[i].disconnect();

        }

        connection2.disconnect();
        connectionOutput.disconnect();

    }

    public void getRequest(HttpURLConnection connection) {
        try {
            // Request method
            connection.setRequestMethod("GET");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postRequest(HttpURLConnection connection, boolean state) {
        try {

            // Create JSON request body based on the state boolean
            String requestBody = "{\"newState\": \"" + (state ? "true" : "false") + "\"}";
            //String requestBody = "{\"state\": \"" + (state ? "true" : "false") + "\"}";
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                byte[] data = requestBody.getBytes(StandardCharsets.UTF_8);
                wr.write(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postSensorRequest2(HttpURLConnection connection, String name) {
        try {

            // Create JSON request body based on the state boolean
            String requestBody = "{\"newInformation\": \"" + name + "\"}";
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                byte[] data = requestBody.getBytes(StandardCharsets.UTF_8);
                wr.write(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setConnectionOutput(String device) {

        String baseURL = "https://server-o8if.onrender.com/static/device/" + device + "/" + pwdkey;
        //String baseURL = "https://server-o8if.onrender.com/device/" + device;

        URL url = null;
        try {
            url = new URL(baseURL);

            connectionOutput = (HttpURLConnection) url.openConnection();
            connectionOutput.setRequestMethod("POST");
            connectionOutput.setRequestProperty("Content-Type", "application/json");
            connectionOutput.setDoOutput(true);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public JSONObject convertArrayToJSONandSend(int[] result) {

        JSONObject obj;
        int sensorNum = 667;

        //for (int i = 0; i < result.length; i++) {

            if(result[7] == 1) {    // gasSensor true
                postRequest(connectionList[0], true);
                postSensorRequest2(connection2, "gasSensor");
                sensorNum = 0;
            }
            else {
                postRequest(connectionList[0], false);
            }
            if(result[6] == 1) {    // lightSensor true
                postRequest(connectionList[1], true);
                postSensorRequest2(connection2, "lightSensor");
                setConnectionOutput("whiteLed");
                postRequest(connectionOutput, true);
                System.out.println("LIGHT SENSOR TRUE");
                sensorNum = 1;
            }
            else {
                postRequest(connectionList[1], false);
                setConnectionOutput("whiteLed");
                postRequest(connectionOutput, false);
            }
            if(result[5] == 1) {    // steamSensor true
                postRequest(connectionList[2], true);
                postSensorRequest2(connection2, "steamSensor");
                //System.out.println("STEAM SENSOR TRUE");
                sensorNum = 2;
            }
            else {
                //postRequest(connectionList[2], false);
            }
            if(result[4] == 1) {    // moistureSensor true
                postRequest(connectionList[3], true);
                postSensorRequest2(connection2, "moistureSensor");
                sensorNum = 3;
            }
            else {
                postRequest(connectionList[3], false);
            }
            if(result[3] == 1) {   // motionSensor true
                postRequest(connectionList[4], true);
                postSensorRequest2(connection2, "motionSensor");
                sensorNum = 4;
            }
            else {
                postRequest(connectionList[4], false);
            }

        //}

        //for (int i = 0; i < NUM_CONNECTIONS; i++) {
          //  printResponse(connectionList[i]);
        //}

        if(sensorNum != 667) {
            printResponse(connectionList[sensorNum]);
            printResponse(connection2);
            printResponse(connectionOutput);
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
