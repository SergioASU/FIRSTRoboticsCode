package org.usfirst.frc.team842.robot;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team842.robot.commands.ChangeOrientation;
import org.usfirst.frc.team842.robot.commands.ChangeSpeed;
import org.usfirst.frc.team842.robot.commands.DriveStraight;
import org.usfirst.frc.team842.robot.commands.ExampleCommand;
import org.usfirst.frc.team842.robot.commands.ExtendArm;
import org.usfirst.frc.team842.robot.commands.LowerBinElevator;
import org.usfirst.frc.team842.robot.commands.RetractArm;
import org.usfirst.frc.team842.robot.commands.ShootCanBurgler;
import org.usfirst.frc.team842.robot.commands.ToggleClaw;
import org.usfirst.frc.team842.robot.commands.RaiseBinElevator;
import org.usfirst.frc.team842.robot.commands.ToggleSpidermanPivot;
import org.usfirst.frc.team842.robot.commands.ToggleWrist;

import edu.wpi.first.wpilibj.*;
/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI 
{
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
	
    public static Joystick coPilot = new Joystick(0);
    public static Joystick driverController = new Joystick(1);
	Button gamePad1 = new JoystickButton(coPilot, 1);
	Button gamePad3 = new JoystickButton(coPilot, 3);
	Button gamePad2 = new JoystickButton(coPilot, 2);
	Button gamePad4 = new JoystickButton(coPilot, 4);
	Button gamePad5 = new JoystickButton(coPilot,5);
	Button gamePad7 = new JoystickButton(coPilot,7);
	Button gamePad6 = new JoystickButton(coPilot, 6);
	Button gamePad8 = new JoystickButton(coPilot, 8);
	Button gamePad10 = new JoystickButton(coPilot,10);
	Button gamePad9 = new JoystickButton(coPilot, 9);
	Button gamePad12 = new JoystickButton(coPilot, 12);
	Button gamePad11 = new JoystickButton(coPilot, 11);
	Button driver6 = new JoystickButton(driverController, 6);
	Button driver1 = new JoystickButton(driverController, 1);
	Button driver3 = new JoystickButton(driverController, 3);
	Button driver2 = new JoystickButton(driverController, 2);
	
	Button driver5 = new JoystickButton(driverController,5);
	Button driver7 = new JoystickButton(driverController, 7);
	Button driver4 = new JoystickButton(driverController, 4);
	public OI()
	{
		//SmartDashboard.putData("Open Claw", new OpenClaw());
		//Claw 
		gamePad1.whenPressed(new ToggleClaw());
		//gamePad3.whenPressed(new LowerWrist());
		gamePad4.whenPressed(new ToggleWrist());
		
		//Bin Elevator
		
	//	gamePad10.whenPressed(new RaiseToteTwoInches());
		//driver2.whenPressed(new RaiseToteTwoInches());
		
		//gamePad3.whenPressed(new DriveStraight(0,60,2));
		//driver5.whenPressed(new ExtendArm());
		//driver7.whenPressed(new RetractArm());
		//driver5.whileHeld(new ExtendArm());
		//driver5.whenReleased(new RetractArm());
		
		//gamePad9.whenPressed(new ExtendArm());
		//gamePad10.whenPressed(new RetractArm());
		gamePad10.whileHeld(new RetractArm());
		//gamePad10.whenReleased(new ExtendArm());
		gamePad3.whenPressed(new ShootCanBurgler());
		//gamePad8.whenPressed(new ToggleSpidermanPivot());
		
		//driver3.whenPressed(new ChangeOrientation());
		//driver4.whenPressed(raiseOneTote);
		//driver1.whenPressed(new DropOneTote());
		//driver2.whileHeld(new ChangeSpeed());
	}
	
	public Joystick getJoystick() {
        return coPilot;
    }
}

