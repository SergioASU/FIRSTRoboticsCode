package org.usfirst.frc.team842.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import org.omg.PortableInterceptor.ObjectIdHelper;
import org.usfirst.frc.team842.robot.OI;
import org.usfirst.frc.team842.robot.Robot;
import org.usfirst.frc.team842.robot.RobotMap;
import org.usfirst.frc.team842.robot.commands.Drive;
import org.usfirst.frc.team842.robot.utility.PIDController;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Preferences;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.*;

import edu.wpi.first.wpilibj.DigitalInput;
public class DriveTrainMecanum extends Subsystem
{
	public CANTalon mecanumFrontLeft;
	public CANTalon mecanumRearLeft;
	public CANTalon mecanumFrontRight;
	public CANTalon mecanumRearRight;
	
	Timer timer = new Timer();
	double lastTime;
	
	public Gyro driveGyro;
	
	RobotDrive mecanumDrive;
	
	double gyro_kp;
	double gyro_kf;
	double gyroIntegral = 0;
	boolean harvesterFront = true;
	

	double mecanumFrontLeftSetValues;
	double mecanumRearLeftSetValues;
	double mecanumFrontRightSetValues;
	double mecanumRearRightSetValues;
	
	DigitalOutput orientationA = new DigitalOutput(0);
	DigitalOutput orientationB = new DigitalOutput(1);
	
	double driveP = 1.03;
	double driveI = 0.1;
	double driveD = 0;
	double ff = 1.1;
	int iZone = 0;
	double ramp = 1;	
	public double drivePosP = 1;
	public double drivePosI = 0;
	public double drivePosD = 100;
	public double drivePosRamp = 5;
	
	double driveAcc = .15;
	double driveTwistAcc = .5;
	double driveRatedX = 0;
	double driveRatedY = 0;
	double driveRatedTwist = 0;
	
	boolean halfSpeed = false;
	
	public Encoder strafeWheelEncoder = new Encoder(RobotMap.strafeWheelEncoderA, RobotMap.strafeWheelEncoderB);
	public double ticksPerInchStrafe = 29.5;
	
	List<CANTalon> driveTalons;
	//Preferences prefs = Preferences.getInstance();
	//PIDController
	public PIDController gyroAngleController = new PIDController(0.03, 0.003, 0);
	public PIDController gyroRateController = new PIDController(0, 0.0, 0, 1/218.0);
	
	//public PIDController strafePosController = new PIDController(.025,0,0,0);
	public PIDController strafePosController = new PIDController(.1, 0, 0, 0);
	public PIDController forwardPosController = new PIDController(0.05, 0.005, 0);
	public FileWriter f;
    CameraServer server;
    public boolean fileOpened = false;
	
	public DriveTrainMecanum()
	{	
		mecanumFrontLeft = new CANTalon(RobotMap.mecanumTalonFrontLeftID);
		mecanumRearLeft = new CANTalon(RobotMap.mecanumTalonRearLeftID);
		mecanumFrontRight = new CANTalon(RobotMap.mecanumTalonFrontRightID);
		mecanumRearRight = new CANTalon(RobotMap.mecanumTalonRearRightID);
		
		driveTalons = new ArrayList<CANTalon>();
		
		driveGyro = new Gyro(RobotMap.driveGyroID);
		
		OI.driverController = new Joystick(1);
		//prefs.putDouble("Gyro KP: ", gyro_kp);
		//timer.start();
		//lastTime = timer.get();
		
		
		driveTalons.add(mecanumFrontLeft);
		driveTalons.add(mecanumRearLeft);
		driveTalons.add(mecanumFrontRight);
		driveTalons.add(mecanumRearRight);
		
		for(int i = 0; i < driveTalons.size(); i++)
		{
			//driveTalons.get(i).setFeedbackDevice(FeedbackDevice.QuadEncoder);
			driveTalons.get(i).changeControlMode(ControlMode.Speed);
			driveTalons.get(i).setProfile(0);
			driveTalons.get(i).enableBrakeMode(false);
			//driveTalons.get(i).setPID(driveP, driveI, driveD, ff, iZone, ramp, 0);
			//driveTalons.get(i).setPID(drivePosP, drivePosI, drivePosD, 0, 0, drivePosRamp, 1);
		}
		
		
		//server = CameraServer.getInstance();
        //server.setQuality(50);
        //the camera name (ex "cam0") can be found through the roborio web interface
       // server.startAutomaticCapture("cam0");
        gyroAngleController.start();
        gyroRateController.start();
        strafePosController.start();
        forwardPosController.start();
        strafePosController.setTarget(strafeWheelEncoder.get()/ticksPerInchStrafe);
        forwardPosController.setTarget(getAverageEncoder() / ((1440 / (6 * 3.14159)) * (60.0/59.0) * (144.0/141.0)));
        gyroRateController.setTarget(0);
        gyroAngleController.setTarget(driveGyro.getAngle());
	}
	
	public void driveAuto(double forwardPos, double strafePos, double angle)
	{
		
		if(!fileOpened) {
			try {
				f = new FileWriter("/home/lvuser/Robot.txt");
				fileOpened = true;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		double gyroAngle = Robot.driveTrainMecanum.driveGyro.getAngle();
		double spin = gyroAngleController.iterate(gyroAngle);
		//Robot.driveTrainMecanum.setMotors(0, 0, -spin);
		strafePosController.setTarget(strafePos);
		forwardPosController.setTarget(forwardPos);
		double strafePositionInches = Robot.driveTrainMecanum.strafeWheelEncoder.get()/Robot.driveTrainMecanum.ticksPerInchStrafe ;
		//strafePositionInches += (gyroAngle - initGyroPosition) * (735/90.0)/Robot.driveTrainMecanum.ticksPerInchStrafe;
		double strafe = strafePosController.iterate(strafePositionInches);
		double forwardPositionInches = Robot.driveTrainMecanum.getAverageEncoder() / ((1440 / (6 * 3.14159)) * (60.0/59.0) * (144.0/141.0));
		double forward = forwardPosController.iterate(forwardPositionInches);
		SmartDashboard.putNumber("StrafeError", strafePosController.getError());
		SmartDashboard.putNumber("GyroError", gyroAngleController.getError());
		SmartDashboard.putNumber("ForwardError", forwardPosController.getError());
		
		forward = forward > 1? 1:forward;
		forward = forward < -1? -1:forward;
		
		strafe = strafe > 1? 1:strafe;
		strafe = strafe < -1? -1:strafe;
		
		spin = spin > 1? 1:spin;
		spin = spin < -1? -1:spin;
		Robot.driveTrainMecanum.setMotors(forward, strafe, -spin);
		SmartDashboard.putNumber("StrafePositionTarget", strafePosController.getTarget());
		SmartDashboard.putNumber("StrafePositionInches", strafePositionInches);
		
		
		try {
			if(fileOpened)
			{
				f.write(StopWatch.getTime() + "\t" + strafePosController.getTarget() + "\t" + strafePositionInches + "\t" +
						gyroAngleController.getTarget() + "\t" + driveGyro.getAngle() + "\t" + 
						forwardPosController.getTarget() + "\t" + forwardPositionInches + "\n");
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	int count = 0;
	double maxOmega = 90; //degrees per sec
	
	public void driveGamepad()
	{
		for(int i = 0; i < driveTalons.size(); i++)
		{
			driveTalons.get(i).changeControlMode(ControlMode.PercentVbus);
			driveTalons.get(i).setProfile(0);
		}
		
		double driveXNew = OI.driverController.getX();
		double driveYNew = OI.driverController.getY();
		double driveTwistNew = OI.driverController.getZ();
		
		setMotors(driveYNew, driveTwistNew, 0);
		
		
	}
	public void drive()
	{
		SmartDashboard.putNumber("Derp2", count++);
		for(int i = 0; i < driveTalons.size(); i++)
		{
			driveTalons.get(i).changeControlMode(ControlMode.Speed);
			driveTalons.get(i).setProfile(0);
		}
		
		double driveXNew = OI.driverController.getX();
		double driveYNew = OI.driverController.getY();
		//double driveTwistNew = OI.driverController.getZ();
		double driveTwistNew = OI.driverController.getRawAxis(4);
		/*
		if(driveXNew <= .055 && driveXNew >= -.055)
		{
			driveXNew = 0;
		}
		
		if(driveYNew <= .055 && driveYNew >= -.055)
		{
			driveYNew = 0;
		}
		*/
		double currentAngle = driveGyro.getAngle();
		double currentARate = driveGyro.getRate();
		
		/*
		if(driveTwistNew <= .055 && driveTwistNew >= -.055)
		{
			driveTwistNew = 0;
			gyroRateController.setTarget(0);
		}
		else
		{
			driveTwistNew = driveTwistNew * Math.abs(driveTwistNew);
			gyroRateController.setTarget(-driveTwistNew*maxOmega);
			gyroAngleController.setTarget(currentAngle);
		}
		
		*/
		SmartDashboard.putNumber("GyroRate", currentARate);
		
		/*
		if(OI.driverController.getRawButton(1))
		{
			gyroAngleController.setTarget(90);
		}
		else if(OI.driverController.getRawButton(3))
		{
			gyroAngleController.setTarget(0);
		}
		*/
		
		
		double spin = gyroRateController.iterate(currentARate); //+ gyroAngleController.iterate(currentAngle);
		
		driveXNew = driveXNew * Math.abs(driveXNew)* Math.abs(driveXNew) ;
		driveYNew = driveYNew * Math.abs(driveYNew) * Math.abs(driveYNew);
	//	driveTwistNew = driveTwistNew * Math.abs(driveTwistNew) * Math.abs(driveTwistNew);
		/*
		if(driveXNew - driveRatedX > driveAcc)
		{
			driveRatedX = driveRatedX + driveAcc;
		}
		else if(driveXNew - driveRatedX < -driveAcc)
		{
			driveRatedX = driveRatedX - driveAcc;
		}
		else
		{
			driveRatedX = driveXNew;
		}
		
		if(driveYNew - driveRatedY > driveAcc)
		{
			driveRatedY = driveRatedY + driveAcc;
		}
		else if(driveYNew - driveRatedY < -driveAcc)
		{
			driveRatedY = driveRatedY - driveAcc;
		}
		else
		{
			driveRatedY = driveYNew;
		}
		
		/*
		if(driveTwistNew - driveRatedTwist > driveTwistAcc)
		{
			driveRatedTwist = driveRatedTwist + driveTwistAcc;
		}
		else if(driveTwistNew - driveRatedTwist < -driveTwistAcc)
		{
			driveRatedTwist = driveRatedTwist - driveTwistAcc;
		}
		else
		{
			driveRatedTwist = driveTwistNew;
		}
		*/
		driveRatedTwist = driveTwistNew;
		
		SmartDashboard.putNumber("DriveX", driveRatedX);
		SmartDashboard.putNumber("DriveY", driveRatedY);
		SmartDashboard.putNumber("DriveTwist", driveRatedTwist);
		
		if(Math.abs(driveYNew) < .055 && Math.abs(driveXNew) < .055)
		{
			for(int i = 0; i < driveTalons.size(); i++)
			{
				driveTalons.get(i).ClearIaccum();
			}
		}
		if(!OI.driverController.getRawButton(1))
		{
			setMotors(-driveYNew,driveXNew,0);
			
		}
		else
		{
			if(OI.driverController.getRawButton(2))
			{
				setMotors(-driveYNew,-driveXNew *1,-driveXNew*.5);
			}
			else
			{
				setMotors(-driveYNew, -driveXNew);
			}
		}
		
		
		/*
		if(OI.driverController.getRawButton(6))
		{
			
			if(harvesterFront)
			{
				orientationA.set(false);
				orientationB.set(true);
				setMotors(driveRatedY*.25, driveRatedX*.25, driveRatedTwist *.25);
				//setMotors(driveRatedY,driveRatedX, -spin);

			}
			else
			{
				orientationA.set(true);
				orientationB.set(false);
				setMotors(-driveRatedY*.25,-driveRatedX*.25, driveRatedTwist*.25);
				//setMotors(-driveRatedY,-driveRatedX, -spin);
				
			}
		}
		else
		{
			if(harvesterFront)
			{
				orientationA.set(false);
				orientationB.set(true);
				setMotors(driveRatedY*.5,driveRatedX, driveRatedTwist *.5);
				//setMotors(driveRatedY,driveRatedX, -spin);

			}
			else
			{
				orientationA.set(true);
				orientationB.set(false);
				setMotors(-driveRatedY*.5,-driveRatedX, driveRatedTwist*.5);
				//setMotors(-driveRatedY,-driveRatedX, -spin);
				
			}
		}
		*/
		//driveRatedTwist = driveRatedTwist * .5;
		
		
		/*
		 * Harvester as front
		 */
		/*
		mecanumFrontLeft.set((-driveY) + (driveTwist) + (driveX));
		mecanumRearLeft.set((-driveY) + (driveTwist) - (driveX));
		mecanumFrontRight.set((driveY) + (driveTwist) + (driveX));
		mecanumRearRight.set((driveY)  + (driveTwist) - (driveX));
		*/
		
		/*
		 * Claw as front
		 */
		
		
		//RobotDrive
		SmartDashboard.putDouble("Gyro:", driveGyro.getAngle());
	}
	
	public void halfSpeed()
	{
		halfSpeed = true;
	}
	
	double[] initialPositions;
	
	public void driveStraightInitialize()
	{
		initialPositions = new double[4];
		drivePosP = Preferences.getInstance().getDouble("DrivePosP", drivePosP);
		drivePosI = Preferences.getInstance().getDouble("DrivePosI", drivePosI);
		drivePosD = Preferences.getInstance().getDouble("DrivePosD", drivePosD);
		for(int i = 0; i < driveTalons.size(); i++)
		{
			driveTalons.get(i).setPID(drivePosP, drivePosI, drivePosD, 0, 0, drivePosRamp, 1);
			driveTalons.get(i).setProfile(1);
			driveTalons.get(i).changeControlMode(ControlMode.Position);
			initialPositions[i] = driveTalons.get(i).getPosition();
		}
		
	}
	
	public void driveStraightExecute(double y, double x)
	{
		int[] flipsY = {1,1,-1,-1};
		int[] flipsX = {1,-1,1,-1};
		for(int i = 0; i < driveTalons.size(); i++)
		{
			driveTalons.get(i).set(initialPositions[i] + (y * flipsY[i]) + x * flipsX[i]);
		}
	}
	
	public void setTalonMode(ControlMode mode)
	{
		for(int i = 0; i < driveTalons.size(); i++)
		{
			driveTalons.get(i).changeControlMode(mode);
		}
	}
	
	public void resetControllers()
	{
		strafePosController.setTarget(strafeWheelEncoder.get()/ticksPerInchStrafe);
        forwardPosController.setTarget(getAverageEncoder() / ((1440 / (6 * 3.14159)) * (60.0/59.0) * (144.0/141.0)));
        gyroRateController.setTarget(0);
        gyroAngleController.setTarget(driveGyro.getAngle());
	}
	
	public void changeOrientation()
	{
		if(harvesterFront)
		{
			harvesterFront = false;
		}
		else
		{
			harvesterFront = true;
		}
	}
	
	public void moveForward(double power)
	{
		for(int i = 0; i < driveTalons.size(); i++)
		{
			driveTalons.get(i).changeControlMode(ControlMode.PercentVbus);
		}
		mecanumFrontLeft.set(-power);
		mecanumRearLeft.set(-power);
		mecanumFrontRight.set(power);
		mecanumRearRight.set(power);
	}
	
	public void moveMotors(double fl, double rl, double fr, double rr)
	{
		for(int i = 0; i < driveTalons.size(); i++)
		{
			driveTalons.get(i).changeControlMode(ControlMode.PercentVbus);
		}
		mecanumFrontLeft.set(-fl);
		mecanumRearLeft.set(-rl);
		mecanumFrontRight.set(fr);
		mecanumRearRight.set(rr);
	}
	public void setMotors(double forward, double spin)
	{
		forward = forward > 1? 1:forward;
		forward = forward < -1? -1:forward;
		
		spin = spin > 1? 1:spin;
		spin = spin < -1? -1:spin;
		
		SmartDashboard.putNumber("forward", forward);
		SmartDashboard.putNumber("spin", spin);
		double maxSpeed;
		double max = 0;
		
		
		if(OI.driverController.getRawButton(8))
		{
			maxSpeed = 1;
			setTalonMode(ControlMode.PercentVbus);
		} else {
			maxSpeed = 1000;
			setTalonMode(ControlMode.Speed);
		}
		
	
		mecanumFrontLeftSetValues = ((-forward) + (spin)) * maxSpeed;
		mecanumRearLeftSetValues = ((-forward) + (spin)) * maxSpeed;
		mecanumFrontRightSetValues = ((forward) + (spin)) * maxSpeed;
		mecanumRearRightSetValues = ((forward) + (spin)) * maxSpeed;
		
		double max1 = Math.max(Math.abs(mecanumFrontLeftSetValues), Math.abs(mecanumRearLeftSetValues));
		double max2 = Math.max(Math.abs(mecanumFrontRightSetValues), Math.abs(mecanumRearRightSetValues));
		max = Math.max(max1, max2);
			
		max = Math.max(max, maxSpeed);
		mecanumFrontLeft.set(mecanumFrontLeftSetValues / max * maxSpeed);
		mecanumRearLeft.set(mecanumRearLeftSetValues/ max * maxSpeed);
		mecanumFrontRight.set(mecanumFrontRightSetValues/ max * maxSpeed);
		mecanumRearRight.set(mecanumRearRightSetValues/ max * maxSpeed); 
	}
	public void setMotors(double forward, double strafe, double spin)
	{
		forward = forward > 1? 1:forward;
		forward = forward < -1? -1:forward;
		
		strafe = strafe > 1? 1:strafe;
		strafe = strafe < -1? -1:strafe;
		
		spin = spin > 1? 1:spin;
		spin = spin < -1? -1:spin;
		
		SmartDashboard.putNumber("forward", forward);
		SmartDashboard.putNumber("strafe", strafe);
		SmartDashboard.putNumber("spin", spin);
		double maxSpeed;
		double max = 0;
		if(OI.driverController.getRawButton(8))
		{
			maxSpeed = 1;
			setTalonMode(ControlMode.PercentVbus);
		} else {
			maxSpeed = 1000;
			setTalonMode(ControlMode.Speed);
		}
	
		mecanumFrontLeftSetValues = ((-forward) + (spin) + (strafe)) * maxSpeed;
		mecanumRearLeftSetValues = ((-forward) + (spin)  - (strafe)) * maxSpeed;
		mecanumFrontRightSetValues = ((forward) + (spin) + (strafe)) * maxSpeed;
		mecanumRearRightSetValues = ((forward) + (spin) - (strafe)) * maxSpeed;
		
		double max1 = Math.max(Math.abs(mecanumFrontLeftSetValues), Math.abs(mecanumRearLeftSetValues));
		double max2 = Math.max(Math.abs(mecanumFrontRightSetValues), Math.abs(mecanumRearRightSetValues));
		max = Math.max(max1, max2);
			
		max = Math.max(max, maxSpeed);
		mecanumFrontLeft.set(mecanumFrontLeftSetValues / max * maxSpeed);
		mecanumRearLeft.set(mecanumRearLeftSetValues/ max * maxSpeed);
		mecanumFrontRight.set(mecanumFrontRightSetValues/ max * maxSpeed);
		mecanumRearRight.set(mecanumRearRightSetValues/ max * maxSpeed); 
	}
	
	public double getAverageEncoder()
	{
		int[] flipsY = {1,1,-1,-1};
		//int[] flipsY = {1,0,-1,0};
		double sum = 0;
		for(int i = 0; i < driveTalons.size(); i++)
		{
			//driveTalons.get(i).changeControlMode(ControlMode.Position);
			sum += driveTalons.get(i).getPosition() * flipsY[i];

		}
		//return sum / driveTalons.size();
		return sum / 4;
	}
	
	public double getCount()
	{
		return 0;
	}
	
	public void zeroGyro()
	{
		driveGyro.reset();
	}
	@Override
	protected void initDefaultCommand() 
	{
		setDefaultCommand(new Drive());
	}

}
