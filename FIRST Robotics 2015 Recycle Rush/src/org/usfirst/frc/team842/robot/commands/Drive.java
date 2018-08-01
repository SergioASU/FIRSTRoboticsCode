package org.usfirst.frc.team842.robot.commands;

import org.usfirst.frc.team842.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive extends Command
{
	public static double gyro_P = 1;
	public static double gyro_I = 0;
	double gyro_kf;
	Preferences prefs = Preferences.getInstance();
	
	public Drive()
	{
		requires(Robot.driveTrainMecanum);
		//setTimeout(1);
		prefs.putDouble("GyroP", gyro_P);
		prefs.putDouble("GyroI", gyro_I);
		prefs.putDouble("Setpoint", 0);
		Preferences.getInstance().putDouble("DrivePosP", Robot.driveTrainMecanum.drivePosP);
		Preferences.getInstance().putDouble("DrivePosI", Robot.driveTrainMecanum.drivePosI);
		Preferences.getInstance().putDouble("DrivePosD", Robot.driveTrainMecanum.drivePosD);
		Preferences.getInstance().putDouble("DistanceX", 0);
		Preferences.getInstance().putDouble("DistanceY", 0);
	}

	@Override
	protected void initialize() 
	{
		//prefs.putDouble("GyroKP", gyro_kp);
	}

	@Override
	protected void execute()
	{
		//gyro_P = prefs.getDouble("GyroP" , gyro_P);
		//SmartDashboard.putDouble("GyroP", gyro_P);
		//gyro_I = prefs.getDouble("GyroI", gyro_I);
		//SmartDashboard.putDouble("GyroI", gyro_I);
		
		Robot.driveTrainMecanum.drive();
	}

	@Override
	protected boolean isFinished() 
	{
		return isTimedOut();
	}

	@Override
	protected void end(){}

	@Override
	protected void interrupted(){}
}
