package org.usfirst.frc.team842.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class MiddleCanGrabber extends Subsystem
{
	Solenoid middleCanGrabber;
	public MiddleCanGrabber()
	{
		middleCanGrabber = new Solenoid(7);
	}
	
	public void extendArm()
	{
		middleCanGrabber.set(false);
	}
	
	public void retractArm()
	{
		middleCanGrabber.set(true);
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}

}
