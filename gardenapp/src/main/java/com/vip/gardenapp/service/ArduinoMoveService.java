package com.vip.gardenapp.service;

import java.io.BufferedWriter;

import java.io.OutputStreamWriter;


import com.fazecast.jSerialComm.SerialPort;

public class ArduinoMoveService {

	public boolean move(String axis, String distance) {
		SerialPort ports[] = SerialPort.getCommPorts();

//		System.out.println("Select a port:");
		int i = 1;
		for (SerialPort port : ports) {
//			System.out.println(i++ + ". " + port.getSystemPortName());
		}

		SerialPort port = ports[1];
		port.setComPortParameters(9600, 8, 1, 0);
		port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
		
	    if (port.openPort()) {
//			System.out.println("Successfully opened the port.");
		} else {
//			System.out.println("Unable to open the port. on " + ports[1].getSystemPortName());
			return false;
		}

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(port.getOutputStream()));
		try {
			if(axis.equals("0"))
			{
				bw.write("x" + distance);
				bw.flush();

				
			}
			else if(axis.equals("1"))
			{
				bw.write("y" + distance);
				bw.flush();
			}
//			TimeUnit.SECONDS.sleep(1);
//			String str = input.readLine();
//			System.out.println("recieved from arduino :" + str);
//			bw = new BufferedWriter(new OutputStreamWriter(port.getOutputStream()));
//			bw.write("y" + y);
//			bw.flush();
//			
//			TimeUnit.SECONDS.sleep(1);
//			SerialPort ports1[] = SerialPort.getCommPorts();
//			SerialPort port1 = ports1[1];
//			port.setComPortParameters(9600, 8, 1, 0);
//			port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
//			if(port.isOpen())
//			{
//				System.out.println("yet opened");
//			}
	
				port.closePort();
//				System.out.println("closing port");
				
//			str = input.readLine();
//			System.out.println("recieved from arduino :" + str);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
