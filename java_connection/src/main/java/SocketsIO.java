import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketsIO {

    private Socket socket;

    public SocketsIO() {
        try {
            socket = IO.socket("https://server-o8if.onrender.com");
            initializeEventListeners();
        } catch ( URISyntaxException e) {
            throw new RuntimeException("Invalid Socket.IO server URI", e);
        }
    }

    private void initializeEventListeners() {
        socket.on(Socket.EVENT_CONNECT, args -> System.out.println("Connected to the server."));

        socket.on("device-state-changed", args -> {
            if (args.length > 0 && args[0] instanceof JSONObject) {
                JSONObject data = (JSONObject) args[0];
                handleDeviceStateChanged(data);
            }
        });

        socket.on(Socket.EVENT_DISCONNECT, args -> System.out.println("Disconnected from the server."));
    }

    private void handleDeviceStateChanged(JSONObject data) {
        System.out.println(data.toString());
    }

    public void connect() {
        socket.connect();
    }

    public void disconnect() {
        socket.disconnect();
    }


}