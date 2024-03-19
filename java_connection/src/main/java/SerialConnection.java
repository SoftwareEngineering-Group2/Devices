import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class SerialConnection {
    public byte data;
    public void serialConnect(SerialPort sp) throws IOException, InterruptedException {
        // default connection settings for Arduino
        sp.setComPortParameters(
                9600,
                8,
                1,
                0
        );
        // block until bytes can be written
        sp.setComPortTimeouts(
                SerialPort.TIMEOUT_WRITE_BLOCKING,
                0,
                0
        );

        if (sp.openPort()) {
            System.out.println("Port is open :)");
        } else {
            System.out.println("Failed to open port :(");
            return;
        }
    }

    public void serialWrite(SerialPort sp){
        try {
            sp.getOutputStream().write(data);
            sp.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serialDisconnect(SerialPort sp){
        if (sp.closePort()) {
            System.out.println("Port is closed :)");
        } else {
            System.out.println("Failed to close port :(");
            return;
        }
    }

    public byte convertToByte(Integer[] data) {
        byte resultByte = 0;
        for (int i = 0; i < 8; i++) {
            resultByte |= (data[i] & 0x01) << (7 - i);
        }
        return resultByte;
    }
}


