package org.usfirst.frc.team842.robot.commands;

import org.usfirst.frc.team842.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ToggleWrist extends Command
{
	public ToggleWrist()
	{
		requires(Robot.claw);
		setTimeout(1);
	}

	@Override
	protected void initialize() 
	{
		Robot.claw.toggleWrist();
	}

	@Override
	protected void execute(){}

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
