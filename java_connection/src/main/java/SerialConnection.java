import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class SerialConnection {
    //public byte data;
    private SerialPort sp;

    private HTTPHelper httpHelper = new HTTPHelper();

    public void serialConnect() throws IOException, InterruptedException {
        // Get an array of available serial ports
        SerialPort[] ports = SerialPort.getCommPorts();

        // Print information about each serial port
		/*for (SerialPort port : ports) {
			System.out.println("Port Name: " + port.getSystemPortName());
			//System.out.println("Port Description: " + port.getPortDescription());
			System.out.println("Baud Rate: " + port.getBaudRate());
			//System.out.println("Port Number: " + port.getComPortNumber());
			System.out.println("Is Open: " + port.isOpen());
			System.out.println("----------------------------------");
		}*/

        sp = SerialPort.getCommPort("COM3"); // device name TODO: must be changed
        sp.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
        sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 1000);

        if (sp.openPort()) {
            System.out.println("Port is open :)");
        } else {
            System.out.println("Failed to open port :(");
            return;
        }

    }

    public void serialWriteData(byte data) throws IOException {

        sp.getOutputStream().write(data);
        sp.getOutputStream().flush();

    }

    public void serialReadData() throws IOException {

        httpHelper.HttpConnect();

        try {
            while (true) {
                if (sp.getInputStream().available() > 0) {
                    int receivedByte = sp.getInputStream().read(); // Read the byte from Arduino
                    System.out.println("Received Byte: " + receivedByte);
                    System.out.print("Converted: ");
                    int[] result = byteToArray((byte)receivedByte);
                    for (int i = 0; i < 8; i++) {
                        System.out.print(result[i]);
                    }
                    System.out.println("");

                    httpHelper.convertArrayToJSONandSend(result);
                    //Thread.sleep(500);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void serialClose() {

        if (sp.closePort()) {
            System.out.println("Port is closed :)");
        } else {
            System.out.println("Failed to close port :(");
        }

    }

    public byte convertToByte(Integer[] data) {
        byte resultByte = 0;
        for (int i = 0; i < 8; i++) {
            resultByte |= (data[i] & 0x01) << (7 - i);
        }
        return resultByte;
    }

    public int[] byteToArray(byte b) {
        int[] arr = new int[8];
        for (int i = 0; i < 8; i++) {
            arr[i] = b & 1;
            b = (byte) (b >> 1);
        }
        return arr;
    }

}
