package de.mschoeffler.arduino.serialcomm.example01;

import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;

/**
 * Simple application that is part of an tutorial. 
 * The tutorial shows how to establish a serial connection between a Java and Arduino program.
 * @author Michael Schoeffler (www.mschoeffler.de)
 *
 */
public class Startup {

	public static void main(String[] args) throws IOException, InterruptedException {

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

		SerialPort sp = SerialPort.getCommPort("COM5"); // device name TODO: must be changed
		sp.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
		sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0); // block until bytes can be written
		
		if (sp.openPort()) {
			System.out.println("Port is open :)");
		} else {
			System.out.println("Failed to open port :(");
			return;
		}		
		
		for (Integer i = 0; i < 5; ++i) {			
			sp.getOutputStream().write(i.byteValue());
			sp.getOutputStream().flush();
			System.out.println("Sent number: " + i);
			Thread.sleep(1000);
		}		
		
		if (sp.closePort()) {
			System.out.println("Port is closed :)");
		} else {
			System.out.println("Failed to close port :(");
			return;
		}
		

	}

}