package org.usfirst.frc.team842.robot.commands;

import org.usfirst.frc.team842.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ToggleClaw extends Command
{
	public ToggleClaw()
	{
		requires(Robot.claw);
		//setTimeout(1);
	}

	@Override
	protected void initialize() 
	{
		Robot.claw.toggleClaw();;
	}

	@Override
	protected void execute()
	{
	
	}

	@Override
	protected boolean isFinished() 
	{
		return true;
	}

	@Override
	protected void end(){}

	@Override
	protected void interrupted(){}
}
