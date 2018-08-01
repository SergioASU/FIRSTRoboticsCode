package org.usfirst.frc.team842.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

public class StopWatch
{
	static double startTime;
	
	public static void start()
	{
		startTime = System.currentTimeMillis() / 1000.0;
	}
	
	public static double getTime()
	{
		return (System.currentTimeMillis() / 1000.0) - startTime;
	}

}
