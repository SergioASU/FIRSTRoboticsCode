package org.usfirst.frc.team842.robot.subsystems;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.usfirst.frc.team842.robot.RobotMap;
import org.usfirst.frc.team842.robot.commands.Drive;
import org.usfirst.frc.team842.robot.commands.ToggleClaw;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Claw extends Subsystem
{	
	//Solenoids for Claw
	Solenoid claw;
	Solenoid wrist;
	public Claw()
	{
		super();
		claw = new Solenoid(RobotMap.clawSolenoidID);
		wrist = new Solenoid(RobotMap.wristSolenoidID);
	}
	
	public void open()
	{
		claw.set(false);
	}
	
	
	public void close()
	{
		claw.set(true);
	}
	
	public void raise()
	{
		wrist.set(true);
	}
	
	public void lower()
	{
		wrist.set(false);
		
	}
	
	public void toggleClaw()
	{
		boolean clawState = claw.get();
		claw.set(!clawState);
	}
	
	public void toggleWrist()
	{
		boolean wristState = wrist.get();
		wrist.set(!wristState);
	}

	@Override
	protected void initDefaultCommand() 
	{
		//setDefaultCommand(new OpenClaw());
		
	}
}
