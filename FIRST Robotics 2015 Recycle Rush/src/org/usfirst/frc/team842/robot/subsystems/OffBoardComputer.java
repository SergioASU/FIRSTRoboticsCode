package org.usfirst.frc.team842.robot.subsystems;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.*;
import java.net.*;

import org.usfirst.frc.team842.robot.commands.DefaultElevatorCommand;

public class OffBoardComputer extends Subsystem 
{
	DatagramSocket sock;
	byte[] buffer = new byte[65536];
    DatagramPacket incoming;
    public String s = "";
	public OffBoardComputer()
	{
		    
            //1. creating a server socket, parameter is local port number
            try {
            	sock = new DatagramSocket(7777);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
             
            //buffer to receive incoming data
            
            incoming = new DatagramPacket(buffer, buffer.length);
            /*
          //2. Wait for an incoming data
            echo("Server socket created. Waiting for incoming data...");
            */
            
           // SmartDashboard.putString("From Python:", "Server socket created. Waiting for incoming data...");
            
            
	}
	
	Runnable receiveData = new Runnable()
	{
		public void run()
		{
			while(true)
			{
				try {
					sock.receive(incoming);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        byte[] data = incoming.getData();
		        s = new String(data, 0, incoming.getLength());
		        
		        SmartDashboard.putString("FromPython:", incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + s);
				
			}
			
		}
	};
	
	public Thread VisionThread = new Thread(receiveData);
	public void receiveDate()
	{
		
	}
	protected void initDefaultCommand() 
	{
		//setDefaultCommand(new GetVisionData());
	}
}
