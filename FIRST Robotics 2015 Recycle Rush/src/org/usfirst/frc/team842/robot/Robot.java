
package org.usfirst.frc.team842.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import org.usfirst.frc.team842.robot.commands.Autonomous;
import org.usfirst.frc.team842.robot.commands.ExampleCommand;
import org.usfirst.frc.team842.robot.commands.ShootCanBurgler;
import org.usfirst.frc.team842.robot.subsystems.BinElevator;
import org.usfirst.frc.team842.robot.subsystems.Claw;
import org.usfirst.frc.team842.robot.subsystems.DriveTrainMecanum;
import org.usfirst.frc.team842.robot.subsystems.ExampleSubsystem;
import org.usfirst.frc.team842.robot.subsystems.MiddleCanGrabber;
import org.usfirst.frc.team842.robot.subsystems.OffBoardComputer;
import org.usfirst.frc.team842.robot.subsystems.Spiderman;
import org.usfirst.frc.team842.robot.subsystems.StopWatch;

import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.PIDController;

import edu.wpi.first.wpilibj.Compressor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot  
{
	public static final DriveTrainMecanum driveTrainMecanum = new DriveTrainMecanum();
	public static final Claw claw = new Claw();
	public static final BinElevator binElevator = new BinElevator();
	public static final MiddleCanGrabber arm = new MiddleCanGrabber();
	public static final Spiderman spiderman = new Spiderman();
	OffBoardComputer visionComputer = new OffBoardComputer();
	Preferences prefs = Preferences.getInstance();
	Command autonomousCommand;
	Command canShooting;
	public static OI oi;
	
	double spiderman1ShootTime = 180;
	double spiderman1ReverseTime = 50;
	double spiderman1ShootPower = -1;
	double spiderman1ReversePower = .9;
	
	double spiderman2ShootTime = 190;
	double spiderman2ReverseTime = 50;
	double spiderman2ShootPower = -1;
	double spiderman2ReversePower = .9;
	
	Compressor compressor = new Compressor();
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	
	public Robot()
	{
		prefs.putDouble("spiderman1ShootTime", spiderman1ShootTime);
		prefs.putDouble("spiderman1ReverseTime", spiderman1ReverseTime);
		prefs.putDouble("spiderman1ShootPower", spiderman1ShootPower);
		prefs.putDouble("spiderman1ReversePower", spiderman1ReversePower);
		
		prefs.putDouble("spiderman2ShootTime", spiderman2ShootTime);
		prefs.putDouble("spiderman2ReverseTime", spiderman2ReverseTime);
		prefs.putDouble("spiderman2ShootPower", spiderman2ShootPower);
		prefs.putDouble("spiderman2ReversePower", spiderman2ReversePower);
	}
    public void robotInit() 
    {
    	oi = new OI();
        // instantiate the command used for the autonomous period
        autonomousCommand = new Autonomous();
        //SmartDashboard.putData(claw);
        //SmartDashboard.putData(driveTrainMecanum);
        //visionComputer.VisionThread.start();
    }
	
	public void disabledPeriodic() 
	{
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("FrontRightEncoderCount", driveTrainMecanum.mecanumFrontRight.getPosition());
        SmartDashboard.putNumber("RearRightEncoderCount", driveTrainMecanum.mecanumRearRight.getPosition());
        SmartDashboard.putNumber("FrontLeftEncoderCount", driveTrainMecanum.mecanumFrontLeft.getPosition());
        SmartDashboard.putNumber("RearLeftEncoderCount", driveTrainMecanum.mecanumRearLeft.getPosition());
        SmartDashboard.putNumber("StrafeWheelEncoderValue", driveTrainMecanum.strafeWheelEncoder.get());
		//driveTrainMecanum.driveGyro.reset();
	}
	
	public void shoot(double forwardPower, double reversePower, double shootTime, double reverseTime, double waitTime, double reverseTime2, double reversePower2, double reverseTime3, double reversePower3, CANTalon spidermanTalon)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				double loopCounter = 0;
				double endTime = 0;
				double startTime = System.currentTimeMillis();
				double startTime1 = System.currentTimeMillis();
				double startTime2;
				double startTime3;
				double startTime4;
				double startTime5;
				
				spidermanTalon.set(forwardPower);
				try {
					Thread.sleep((long) shootTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				spidermanTalon.set(0);
				
				try {
					Thread.sleep((long) 0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				spidermanTalon.set(reversePower);
				//Robot.driveTrainMecanum.moveForward(1);
				try {
					Thread.sleep((long) reverseTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				spidermanTalon.set(-.20);
				
				try {
					Thread.sleep((long) waitTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				spidermanTalon.set(reversePower2);
				//Robot.driveTrainMecanum.moveForward(1);
				try {
					Thread.sleep((long) reverseTime2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				spidermanTalon.set(0);
			}
		}).start();
	}
	
	public void reelIn(double power, double time, CANTalon spidermanTalon)
	{
		spidermanTalon.set(power);
		try {
			Thread.sleep((long) time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		spidermanTalon.set(0);
	}
	

    public void autonomousInit() 
    {
    	
    	compressor.stop();
    	// original = 145,70
        // schedule the autonomous command (example)
    	driveTrainMecanum.driveGyro.reset();
    	
    	shoot((prefs.getDouble("spiderman1ShootPower", spiderman1ShootPower)), (prefs.getDouble("spiderman1ReversePower", spiderman1ReversePower)), 
    			(prefs.getDouble("spiderman1ShootTime", spiderman1ShootTime)), (prefs.getDouble("spiderman1ReverseTime", spiderman1ReverseTime)),
    			400,100,.5,0,0, Robot.spiderman.spidermanTalon1);
    	
    	shoot((prefs.getDouble("spiderman2ShootPower", spiderman2ShootPower)), (prefs.getDouble("spiderman2ReversePower", spiderman2ReversePower)), 
       		 (prefs.getDouble("spiderman2ShootTime", spiderman2ShootTime)), (prefs.getDouble("spiderman2ReverseTime", spiderman2ReverseTime)),
       			400,100,.5,0,0, Robot.spiderman.spidermanTalon2);
    	
    	//spiderman.shoot(-1, 1, 175, 50, 70 * 0, 0, 0, 0, 0);
    	//Robot.spiderman.pivotSpiderman();
        /*
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    	
    	
    	try {
			Thread.sleep(870);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    	Robot.driveTrainMecanum.moveForward(1);
    	try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Robot.driveTrainMecanum.moveForward(0);
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//Robot.driveTrainMecanum.moveMotors(0, 0, 0, 0);
		
    	//Robot.driveTrainMecanum.moveForward(0);
    	reelIn(.5, 1250, Robot.spiderman.spidermanTalon1);
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	reelIn(.4, 1500, Robot.spiderman.spidermanTalon2);
        if (autonomousCommand != null) autonomousCommand.start();
        compressor.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
    	/*
        Scheduler.getInstance().run();
        SmartDashboard.putDouble("Gyro:", driveTrainMecanum.driveGyro.getAngle());
        SmartDashboard.putNumber("FrontLeft", Math.abs(driveTrainMecanum.mecanumFrontLeft.getOutputVoltage()));
        SmartDashboard.putNumber("BackLeft", Math.abs(driveTrainMecanum.mecanumRearLeft.getOutputVoltage()));
        SmartDashboard.putNumber("FrontRight", Math.abs(driveTrainMecanum.mecanumFrontRight.getOutputVoltage()));
        SmartDashboard.putNumber("BackRight",  Math.abs(driveTrainMecanum.mecanumRearRight.getOutputVoltage()));
        SmartDashboard.putNumber("AverageEncoder", driveTrainMecanum.getAverageEncoder());
        
        SmartDashboard.putNumber("FrontRightEncoderCount", driveTrainMecanum.mecanumFrontRight.getPosition());
        SmartDashboard.putNumber("RearRightEncoderCount", driveTrainMecanum.mecanumRearRight.getPosition());
        SmartDashboard.putNumber("FrontLeftEncoderCount", driveTrainMecanum.mecanumFrontLeft.getPosition());
        SmartDashboard.putNumber("RearLeftEncoderCount", driveTrainMecanum.mecanumRearLeft.getPosition());
        */
        //SmartDashboard.putNumber("Count", driveTrainMecanum.mecanumRearLeft.getPosition() + driveTrainMecanum.mecanumRearRight.getPosition() + );
    }

    public void teleopInit() 
    {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        //driveTrainMecanum.driveGyro.reset();
        driveTrainMecanum.gyroAngleController.setTarget(0);
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit()
    {

    }

    /**
     * 
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
    	//driveTrainMecanum.drive();
        Scheduler.getInstance().run();
      
        if(oi.coPilot.getRawButton(7))
        {
        	spiderman.spidermanTalon1.set(.4);
        	//spiderman.spidermanTalon2.set(.3);
        }
        else
        {
        	spiderman.spidermanTalon1.set(0);
        	//spiderman.spidermanTalon2.set(0);
        }
        
        if(oi.coPilot.getRawButton(8))
        {
        	//spiderman.spidermanTalon1.set(.3);
        	spiderman.spidermanTalon2.set(.3);
        }
        else
        {
        	//spiderman.spidermanTalon1.set(0);
        	spiderman.spidermanTalon2.set(0);
        }
        SmartDashboard.putNumber("FrontLeftEncoder", driveTrainMecanum.mecanumFrontLeft.getPosition());
        SmartDashboard.putNumber("StrafeWheelEncoderValue", driveTrainMecanum.strafeWheelEncoder.get());
        
        SmartDashboard.putNumber("FrontLeft", Math.abs(driveTrainMecanum.mecanumFrontLeft.getOutputVoltage()));
        SmartDashboard.putNumber("BackLeft", Math.abs(driveTrainMecanum.mecanumRearLeft.getOutputVoltage()));
        SmartDashboard.putNumber("FrontRight", Math.abs(driveTrainMecanum.mecanumFrontRight.getOutputVoltage()));
        SmartDashboard.putNumber("BackRight",  Math.abs(driveTrainMecanum.mecanumRearRight.getOutputVoltage()));
        
        SmartDashboard.putNumber("FrontRightEncoderCount", driveTrainMecanum.mecanumFrontRight.getPosition());
        SmartDashboard.putNumber("RearRightEncoderCount", driveTrainMecanum.mecanumRearRight.getPosition());
        SmartDashboard.putNumber("FrontLeftEncoderCount", driveTrainMecanum.mecanumFrontLeft.getPosition());
        SmartDashboard.putNumber("RearLeftEncoderCount", driveTrainMecanum.mecanumRearLeft.getPosition());
        
        SmartDashboard.putNumber("ElevatorEncoder", binElevator.binElevatorTalon.getPosition());
        SmartDashboard.putNumber("ElevatorSetpoint", binElevator.binElevatorTalon.getSetpoint());
        SmartDashboard.putNumber("POV", OI.coPilot.getPOV());
        

        
        /*
        double gyroAngle = Robot.driveTrainMecanum.driveGyro.getAngle();
		double spin = gyroController.iterate(gyroAngle);
		*/
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() 
    {
        LiveWindow.run();
    }
}
