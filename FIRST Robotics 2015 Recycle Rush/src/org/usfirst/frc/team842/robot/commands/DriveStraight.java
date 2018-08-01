package org.usfirst.frc.team842.robot.commands;

import org.usfirst.frc.team842.robot.Robot;
import org.usfirst.frc.team842.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

public class DriveStraight extends Command
{
	long startTime = 0;
	double distanceY;
	double distanceX;
	double exccTime;

	public DriveStraight(double yInches, double xInches, double exccTime)
	{
		this.distanceY = yInches * (1440 / (6 * 3.14159)) * (60.0/59.0) * (144.0/141.0);
		this.distanceX = xInches * (1440 / (6 * 3.14159)) * (60.0/59.0) * (144.0/141.0) * (60.0 / 42.0);
		this.exccTime = exccTime;
		requires(Robot.driveTrainMecanum);
	}

	@Override
	protected void initialize() 
	{
		//double yInches = Preferences.getInstance().getDouble("DistanceY", 0);
		//double xInches = Preferences.getInstance().getDouble("DistanceX", 0);
		//this.distanceY = yInches * (1440 / (6 * 3.14159)) * (60.0/59.0) * (144.0/141.0);
		//this.distanceX = xInches * (1440 / (6 * 3.14159)) * (60.0/59.0) * (144.0/141.0) * (60.0 / 42.0);
		mRatio = 0;
		startTime = System.currentTimeMillis();
		Robot.driveTrainMecanum.driveStraightInitialize();
	}

	double mRatio = 0;
	
	@Override
	protected void execute() 
	{
		long currentTime = (System.currentTimeMillis() - startTime);
		double ratio = Math.min(1, currentTime / (1000 * exccTime));
		double tt = 0.07;
		mRatio = (1-tt)*mRatio + tt*ratio;
		mRatio = 1;
		Robot.driveTrainMecanum.driveStraightExecute(distanceY * mRatio, distanceX * mRatio);
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return startTime != 0 && System.currentTimeMillis() > startTime + (1000 * exccTime) + 100;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}

}
