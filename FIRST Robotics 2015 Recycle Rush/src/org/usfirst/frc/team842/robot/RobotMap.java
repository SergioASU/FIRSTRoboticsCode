package org.usfirst.frc.team842.robot;
/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap 
{
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static int leftMotor = 1;
    // public static int rightMotor = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
	
	/*
	 * Claw ID's
	 */
	public static final int clawSolenoidID = 2;
	public static final int wristSolenoidID = 3;
	/*
	
    /*
     * Bin Elevator ID's
     */
	public static final int binElevatorTalonID = 6;
	public static final int binElevatorSolenoidID = 4;
	
	
	
	/*
	 * DriveTrain ID's
	 * 
	 */
	public static final int mecanumTalonFrontLeftID = 1;
	public static final int mecanumTalonRearLeftID = 2;
	public static final int mecanumTalonFrontRightID = 3;
	public static final int mecanumTalonRearRightID = 4;
	
	public static final int driveGyroID = 0;
	
	public static final int strafeWheelEncoderA = 3;
	public static final int strafeWheelEncoderB = 4;
	
	public static final int driveEncoderID0A = 0;
	public static final int driveEncoderID0B = 1;
	
	public static final int driveEncoderID1A = 0;
	public static final int driveEncoderID1B = 1;
	
	public static final int driveEncoderID2A = 0;
	public static final int driveEncoderID2B = 1;
	
	public static final int driveEncoderID3A = 0;
	public static final int driveEncoderID3B = 1;
	
	
	public static final int spidermanTalonID = 5;
	public static final int spidermanTalon2ID = 7;
	
	/*
	 * Harvester ID's
	 */
	public static final int harvesterTalonID = 7;
	public static final int harvesterLockSolenoidID = 0;
	public static final int harvesterUpAndDownSolenoidID1 = 5;
	public static final int harvesterUpAndDownSolenoidID2 = 6;
	public static final int harvesterEncoderIDA = 2;
	public static final int harvesterEncoderIDB = 3;
	
}
