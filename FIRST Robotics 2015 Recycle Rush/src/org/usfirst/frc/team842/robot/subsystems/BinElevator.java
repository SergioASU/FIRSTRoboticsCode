package org.usfirst.frc.team842.robot.subsystems;

import org.usfirst.frc.team842.robot.OI;
import org.usfirst.frc.team842.robot.RobotMap;
import org.usfirst.frc.team842.robot.commands.DefaultElevatorCommand;
import org.usfirst.frc.team842.robot.commands.Drive;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.command.Subsystem;

public class BinElevator extends Subsystem
{
	public CANTalon binElevatorTalon;
	Solenoid binElevatorSolenoid;
	Encoder binElevatorEncoder;
	
	double driveP = 1;
	double driveI = 0;
	double driveD = 0;
	double ff = 0;
	int iZone = 0;
	double ramp = 0;
	
	public double startPosition;
	public double currentPosition;
	public double maxSpeed = 2000;
	public double currentSpeed = 0;
	
	double lastJoystickValue = 0;
	double maxDriveAccel = .2;
	double encoderRange = 21266.0;
	double ticksPerInch = encoderRange / 59.0;

	public BinElevator()
	{
		binElevatorTalon = new CANTalon(RobotMap.binElevatorTalonID);
		binElevatorSolenoid = new Solenoid(RobotMap.binElevatorSolenoidID);
		binElevatorTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		binElevatorTalon.changeControlMode(ControlMode.Position);
		binElevatorTalon.setProfile(0);
		binElevatorTalon.enableBrakeMode(true);
		binElevatorTalon.setPID(driveP, driveI, driveD, ff, iZone, ramp, 0);
		//startPosition = binElevatorTalon.getPosition();
		currentPosition = binElevatorTalon.getPosition();
		startPosition = currentPosition - 7 *ticksPerInch;
		//currentPosition = startPosition;
		binElevatorTalon.set(currentPosition);
		binElevatorTalon.reverseOutput(true);
		//binElevatorEncoder = new Encoder(RobotMap.binElevatorEncoderIDA, RobotMap.binElevatorEncoderIDB);
	}
	
	public void raise()
	{
		binElevatorSolenoid.set(false);
		binElevatorTalon.set(.75);
	}
	
	public void lower()
	{
		binElevatorSolenoid.set(false);
		binElevatorTalon.set(-.75);
	}
	
	public void manipulate()
	{
		if(OI.coPilot.getPOV() > -1)
		{
			switch(OI.coPilot.getPOV())
			{
			case 0:
				currentPosition = startPosition + 59*ticksPerInch;
				break;
			case 180:
				currentPosition = startPosition;
				break;
				default:
					break;
			
			}
		}
		//double driveX = OI.coPilot.getX();
		double driveY = OI.coPilot.getY();
		
		if(Math.abs(lastJoystickValue - driveY) > maxDriveAccel)
		{
			if(driveY > lastJoystickValue)
			{
				driveY = lastJoystickValue + maxDriveAccel;
			}
			else
			{
				driveY = lastJoystickValue - maxDriveAccel;
			}
		}
		if(driveY < .055 && driveY > -.055)
		{
			binElevatorTalon.changeControlMode(ControlMode.Position);
			binElevatorTalon.set(currentPosition);
			currentSpeed = 0;
		}
		else
		{
			binElevatorTalon.changeControlMode(ControlMode.Speed);
			currentSpeed = Math.abs(binElevatorTalon.get());
			binElevatorTalon.set(-driveY * maxSpeed);
			currentPosition = binElevatorTalon.getPosition();
		}
		
		//double driveTwist = OI.coPilot.getZ();
		lastJoystickValue = driveY;
	}
	
	public void setPosition(ElevatorPosition position)
	{
		switch(position)
		{
		case GROUND:
			currentPosition = startPosition;
			break;
		case MAX:
			currentPosition = startPosition + 59 * ticksPerInch;
			break;
			default:
				break;
		}
	}
	
	public void stop()
	{
		binElevatorTalon.set(0);
		binElevatorSolenoid.set(true);
	}
	
	public void resetEncoder()
	{
		binElevatorEncoder.reset();
	}
	
	public void raiseTo(double encoderValue)
	{
		binElevatorTalon.set(binElevatorEncoder.get() - encoderValue);
		
	}
	
	public void keepPosition()
	{
		//binElevatorTalon.set(currentPosition);
	}
	
	
	@Override
	protected void initDefaultCommand() 
	{
		setDefaultCommand(new DefaultElevatorCommand());
		
	}
	
	public enum ElevatorPosition
	{
		GROUND,
		MAX
	}
}
