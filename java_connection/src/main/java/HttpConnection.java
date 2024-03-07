import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpConnection {

    public int responseCode;
    public StringBuffer response;

    public void HttpConnect() {
        try {
            // URL of server
            //URL url = new URL("https://httpbin.org/post");
            //URL url = new URL("https://ptsv3.com/t/testinghouse/d/4/");
            //URL url = new URL("https://webhook.site/30e1a518-c9cf-425b-9bd8-07c4b598c1bd");
            URL url = new URL("https://server-o8if.onrender.com/device/whiteLed/state");
            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // CALL FUNCTION FOR THE CONNECTION HERE
            getRequest(connection);
            //postRequest(connection);
            //putRequest(connection);

            // Get response code
            responseCode = connection.getResponseCode();
            //System.out.println("Response code: " + responseCode);

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            //System.out.println("Response: " + response);



            // Close connection
            connection.disconnect();
        } catch (Exception e){
            e.printStackTrace();
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
}
