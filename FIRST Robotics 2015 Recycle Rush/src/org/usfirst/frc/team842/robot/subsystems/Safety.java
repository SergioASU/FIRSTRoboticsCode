package org.usfirst.frc.team842.robot.subsystems;

import org.usfirst.frc.team842.robot.Robot;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Safety extends Subsystem 
{
	Claw claw;
	//Harvester harvester;
	DriveTrainMecanum driveTrain;
	BinElevator binElevator;
	
	public Safety(Robot robot)
	{
		claw = robot.claw;
		//harvester = robot.harvester;
		driveTrain = robot.driveTrainMecanum;
		binElevator = robot.binElevator;
	}

	@Override
	protected void initDefaultCommand() 
	{
		//harvester.harvesterTalon.getOutputCurrent();
		
	}

}
