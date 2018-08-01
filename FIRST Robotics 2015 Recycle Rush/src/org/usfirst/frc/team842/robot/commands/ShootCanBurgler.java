package org.usfirst.frc.team842.robot.commands;

import org.usfirst.frc.team842.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ShootCanBurgler extends Command 
{
	public ShootCanBurgler()
	{
		requires(Robot.spiderman);
	}

	@Override
	protected void initialize() 
	{
		//Robot.spiderman.shoot(-1, 1, 165, 50, 70 * 0, 0, 0, 0, 0, Robot.spiderman.spidermanTalon1);
		//Robot.spiderman.shoot(-1, 1, 165, 50, 70 * 0, 0, 0, 0, 0, Robot.spiderman.spidermanTalon2);
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
