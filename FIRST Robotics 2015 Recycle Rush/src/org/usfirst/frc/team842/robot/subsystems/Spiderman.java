package org.usfirst.frc.team842.robot.subsystems;

import org.usfirst.frc.team842.robot.Robot;
import org.usfirst.frc.team842.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Spiderman extends Subsystem
{
	public CANTalon spidermanTalon1;
	public CANTalon spidermanTalon2;
	public Solenoid pivot;
	boolean pivoted = false;
	public Spiderman()
	{
		spidermanTalon1 = new CANTalon(RobotMap.spidermanTalonID);
		spidermanTalon1.changeControlMode(ControlMode.PercentVbus);
		
		spidermanTalon2 = new CANTalon(RobotMap.spidermanTalon2ID);
		spidermanTalon2.changeControlMode(ControlMode.PercentVbus);
		
		pivot = new Solenoid(0);
	}
	
	public void pivotSpiderman()
	{
		if(pivot.get())
		{
			pivot.set(false);
		}
		else
		{
			pivot.set(true);
		}
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
					Thread.sleep((long) waitTime);
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
				
				spidermanTalon.set(0);
				/*
				try {
					Thread.sleep((long) waitTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				spidermanTalon.set(reversePower2);
				///Robot.driveTrainMecanum.moveForward(1);
				try {
					Thread.sleep((long) reverseTime2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				spidermanTalon.set(reversePower3);
				try {
					Thread.sleep((long) reverseTime3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				//spidermanTalon.set(0);
				/*
				while(shootTime > (System.currentTimeMillis() - startTime1))
				{
					loopCounter++;
					endTime = System.currentTimeMillis() - startTime;
				}
				
				
				SmartDashboard.putNumber("Phase1Count", loopCounter);
				SmartDashboard.putNumber("Phase1Time", endTime);
				loopCounter = 0;
				startTime2 = System.currentTimeMillis();
				
				spidermanTalon.set(reversePower);
				while(reverseTime > System.currentTimeMillis() - startTime2)
				{
					loopCounter++;
					endTime = System.currentTimeMillis() - startTime;
				}
				SmartDashboard.putNumber("Phase2Count", loopCounter);
				SmartDashboard.putNumber("Phase2Time", endTime);
				spidermanTalon.set(0);
				
				loopCounter = 0;
				startTime3 = System.currentTimeMillis();
				
				while(waitTime > System.currentTimeMillis() - startTime3)
				{
					loopCounter++;
					endTime = System.currentTimeMillis() - startTime;
				}
				
				SmartDashboard.putNumber("Phase3Count", loopCounter);
				SmartDashboard.putNumber("Phase3Time", endTime);
				loopCounter = 0;
				startTime4 = System.currentTimeMillis();
				
				spidermanTalon.set(reversePower2);
				while(reverseTime2 > System.currentTimeMillis() - startTime4)
				{
					loopCounter++;
					endTime = System.currentTimeMillis() - startTime;
				}
				
				SmartDashboard.putNumber("Phase4Count", loopCounter);
				SmartDashboard.putNumber("Phase4Time", endTime);
				
				loopCounter = 0;
				startTime5 = System.currentTimeMillis();
				
				spidermanTalon.set(reversePower3);
				while(reverseTime3 > System.currentTimeMillis() - startTime5)
				{
					loopCounter++;
					endTime = System.currentTimeMillis() - startTime;
				}
				SmartDashboard.putNumber("Phase5Count", loopCounter);
				SmartDashboard.putNumber("Phase5Time", endTime);
				spidermanTalon.set(0);
				startTime1 = 0;
				startTime2 = 0;
				startTime3 = 0;
				startTime4 = 0;
				startTime5 = 0;
				*/
				
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

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}
