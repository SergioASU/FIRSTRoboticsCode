package org.usfirst.frc.team842.robot.commands;

import java.io.IOException;

import org.usfirst.frc.team842.robot.Robot;
import org.usfirst.frc.team842.robot.utility.PIDController;
import org.usfirst.frc.team842.robot.utility.PositionRampFunction;

import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SetDriveTrainPosition extends Command 
{
	
	PIDController gyroController = Robot.driveTrainMecanum.gyroAngleController;
	PIDController strafeController = Robot.driveTrainMecanum.strafePosController;
	PIDController forwardController = Robot.driveTrainMecanum.forwardPosController;
	
	long startTime = 0;
	double distanceY;
	double distanceX;
	double exccTime;
	double angle;
	double initGyroPosition = 0;
	double initStrafePosition = 0;
	double initForwardPosition = 0;
	
	PositionRampFunction gyroFunction;
	PositionRampFunction strafeFunction;
	PositionRampFunction forwardFunction;
	
	public SetDriveTrainPosition(double yInches, double xInches, double angle, double exccTime)
	{
		this.distanceY = yInches; //* (1440 / (6 * 3.14159)) * (60.0/59.0) * (144.0/141.0);
		this.distanceX = -xInches;
		this.exccTime = exccTime;
		this.angle = angle;
		gyroFunction = new PositionRampFunction(exccTime, 0, angle);
		strafeFunction = new PositionRampFunction(exccTime, 1, distanceX);
		forwardFunction = new PositionRampFunction(exccTime, 0, distanceY);
		
		requires(Robot.driveTrainMecanum);
	}

	@Override
	protected void initialize() 
	{
		startTime = System.currentTimeMillis();
		Robot.driveTrainMecanum.setTalonMode(ControlMode.Speed);
		initGyroPosition = gyroController.getTarget();
		gyroController.setTarget(initGyroPosition + angle);
		initStrafePosition = strafeController.getTarget();
		//strafeController.setTarget(initStrafePosition);
		initForwardPosition = forwardController.getTarget();
		/*
		gyroController.start();
		strafeController.start();
		forwardController.start();
		*/
	}

	double m_ratio = 0;
	double strafeSpinConpensation = 0;
	
	@Override
	protected void execute() 
	{
		double time = (System.currentTimeMillis() - startTime) / 1000.0;
		SmartDashboard.putNumber("autoTime", time);
		System.out.println(time);
		
		/*
		if(Robot.driveTrainMecanum.fileOpened)
		{
			try {
				Robot.driveTrainMecanum.f.write(time + " ");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		*/
		
		m_ratio = (System.currentTimeMillis() - startTime) / (exccTime * 1000.0);
		m_ratio = Math.min(1, m_ratio);
		//m_ratio = 1;
		/*
		double gyroAngle = Robot.driveTrainMecanum.driveGyro.getAngle();
		double spin = gyroController.iterate(gyroAngle);
		//Robot.driveTrainMecanum.setMotors(0, 0, -spin);
		 */
		double strafePos = initStrafePosition + strafeFunction.get(time);
		double forwardPos = initForwardPosition + forwardFunction.get(time);
		//double strafePos = initStrafePosition +distanceX * m_ratio;
		//double forwardPos = initForwardPosition + distanceY * m_ratio;
		strafeController.setTarget(strafePos);
		forwardController.setTarget(forwardPos);
		Robot.driveTrainMecanum.driveAuto(forwardPos, strafePos, initGyroPosition + angle);
		/*
		double strafePositionInches = Robot.driveTrainMecanum.strafeWheelEncoder.get()/Robot.driveTrainMecanum.ticksPerInchStrafe ;
		//strafePositionInches += (gyroAngle - initGyroPosition) * (735/90.0)/Robot.driveTrainMecanum.ticksPerInchStrafe;
		double strafe = strafeController.iterate(strafePositionInches);
		double forwardPositionInches = Robot.driveTrainMecanum.getAverageEncoder() / ((1440 / (6 * 3.14159)) * (60.0/59.0) * (144.0/141.0));
		double forward = forwardController.iterate(forwardPositionInches);
		SmartDashboard.putNumber("StrafeError", strafeController.getError());
		SmartDashboard.putNumber("GyroError", gyroController.getError());
		SmartDashboard.putNumber("ForwardError", forwardController.getError());
		
		forward = forward > 1? 1:forward;
		forward = forward < -1? -1:forward;
		
		strafe = strafe > 1? 1:strafe;
		strafe = strafe < -1? -1:strafe;
		
		spin = spin > 1? 1:spin;
		spin = spin < -1? -1:spin;
		Robot.driveTrainMecanum.setMotors(forward, strafe, -spin + strafe*strafeSpinConpensation);
		*/
	}
	
	public void setStrafeSpinCompensation(double strafeCompensation)
	{
		strafeSpinConpensation = strafeCompensation;
	}
	@Override
	protected boolean isFinished() 
	{
		// TODO Auto-generated method stub
		return startTime != 0 && System.currentTimeMillis() > startTime + (1000 * exccTime) + 100;
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
