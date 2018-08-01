package org.usfirst.frc.team842.robot.commands;

import org.usfirst.frc.team842.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ResetAuto extends Command
{

	public ResetAuto()
	{
		requires(Robot.driveTrainMecanum);
	}
	@Override
	protected void initialize() 
	{
		Robot.driveTrainMecanum.resetControllers();
	
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
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
