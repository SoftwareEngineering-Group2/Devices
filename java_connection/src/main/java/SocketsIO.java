import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class SocketsIO {

    private Socket socket;

    SerialConnection serial = new SerialConnection();
    private String[] deviceList = {"whiteLed", "yellowLed", "door", "window", "buzzer", "fan"};
    private Integer[] statusArray = new Integer[8];


    public SocketsIO() {

        try {
            serial.serialConnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            socket = IO.socket("https://server-o8if.onrender.com");
            initializeEventListeners();
        } catch ( URISyntaxException e) {
            throw new RuntimeException("Invalid Socket.IO server URI", e);
        }


    }

    private void initializeEventListeners() {
        socket.on(Socket.EVENT_CONNECT, args -> System.out.println("Connected to the server."));

        //this will receive all devices status on startup
        socket.on("all-devices", args -> {
            System.out.println(args[0]);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("SOMEBODY IS SENDING ALL DEVICES MSG");

            updateHouse(args[0]);

        });

        socket.on("device-state-changed", args -> {
            System.out.println(args[0]);

            updateHouse(args[0]);

        });


        socket.on(Socket.EVENT_DISCONNECT, args -> {

            System.out.println("Disconnected from the server.");

            serial.serialClose();

        });
    }

    private void updateHouse(Object... args) {

        try {
            int status = 0;
            JSONArray jsonArray = (JSONArray) args[0];
            for (int i = 0; i < jsonArray.length(); i++){
                status = getStatus(jsonArray.getJSONObject(i));
                if(jsonArray.getJSONObject(i).optString("deviceName").equals("buzzer")){
                    statusArray[7] = status;
                } else if(jsonArray.getJSONObject(i).optString("deviceName").equals("yellowLed")){
                    statusArray[6] = status;
                } else if(jsonArray.getJSONObject(i).optString("deviceName").equals("fan")){
                    statusArray[5] = status;
                } else if(jsonArray.getJSONObject(i).optString("deviceName").equals("door")){
                    statusArray[3] = status;
                } else if(jsonArray.getJSONObject(i).optString("deviceName").equals("window")){
                    statusArray[2] = status;
                }else if(jsonArray.getJSONObject(i).optString("deviceName").equals("whiteLed")){
                    statusArray[1] = status;
                }
            }
            //temporary code to avoid null-values
            statusArray[0] = 0;
            statusArray[4] = 0;
            //SerialConnection serial = new SerialConnection();
            System.out.println(serial.convertToByte(statusArray));
            System.out.println(Arrays.toString(statusArray));

            byte dataToWrite = serial.convertToByte(statusArray);

            String binaryString = String.format("%8s", Integer.toBinaryString(dataToWrite & 0xFF)).replace(' ', '0');
            System.out.println(binaryString);
            serial.serialWriteData(dataToWrite);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void handleDeviceStateChanged(JSONObject data) {
        // this is an example of how the components can react to changes through websocket
        // later we can change the print statements for method calls.
        System.out.println(data.toString());
        if ("whiteLed".equals(data.optString("type"))) {
            if ("off".equals(data.optString("state"))) {
                System.out.println("whiteLed off");
            } else if ("on".equals(data.optString("state"))){
                System.out.println("whiteLed on");
            }
        }
    }

    public void connect() {
        socket.connect();

        try {
            serial.serialReadData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        socket.disconnect();
    }


    public int getStatus(JSONObject data){
        if(data.optString("deviceState") == "true"){
            System.out.println(data.optString("deviceName"));
            return 1;
        }
        return 0;
    }

    public Integer[] getStatusArray(){
        return statusArray;
    }


}
