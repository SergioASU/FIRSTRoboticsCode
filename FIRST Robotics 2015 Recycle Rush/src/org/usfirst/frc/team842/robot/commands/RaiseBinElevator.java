package org.usfirst.frc.team842.robot.commands;

import org.usfirst.frc.team842.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

public class RaiseBinElevator extends Command
{
	Preferences prefs = Preferences.getInstance();
	double speed = .5;
	
	public RaiseBinElevator()
	{
		requires(Robot.binElevator);
		//prefs.putDouble("BinElevatorSpeed", speed);
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void execute() 
	{
		Robot.binElevator.raise();
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFinished() 
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void end() 
	{
		Robot.binElevator.stop();
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void interrupted() 
	{
		Robot.binElevator.stop();
		// TODO Auto-generated method stub
		
	}

}
