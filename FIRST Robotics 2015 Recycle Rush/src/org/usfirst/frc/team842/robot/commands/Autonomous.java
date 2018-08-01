package org.usfirst.frc.team842.robot.commands;

import org.usfirst.frc.team842.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class Autonomous extends CommandGroup
{
	public Autonomous()
	{
		//addSequential(new ShootCanBurgler());
		//addSequential(new ToggleSpidermanPivot());
		//addSequential(new MoveForward());
		//1 Tote Auto
		/*
		addSequential(new ResetAuto());
		
		addSequential(new SetDriveTrainPositionPickUp(0, 0, 0, .25, 0, PickUpType.FULL));
		addSequential(new SetDriveTrainPosition(-40, 0, 0, 2));
		*/
		/*
		addSequential(new ResetAuto());
		
		addSequential(new SetDriveTrainPositionPickUp(0, 0, 0, .25, 0, PickUpType.FULL));
		addSequential(new SetDriveTrainPosition(-26, 0, 0, 1));
		SetDriveTrainPosition strafeRightOne = new SetDriveTrainPosition(0, 74, -2, 3.25); //73 = 81
		addSequential(strafeRightOne);
		
		addSequential(new SetDriveTrainPosition(18, 0, 0, .5));
		addSequential(new SetDriveTrainPositionPickUp(12, 0, 0, .5, .4, PickUpType.FULL));
		addSequential(new SetDriveTrainPosition(0, 0, 0,.25));
		addSequential(new SetDriveTrainPosition(-30, 0, 0, 1.25));
		//addSequential(new SetDriveTrainPosition(-12, 79, 8, 4));
		SetDriveTrainPosition strafeRightTwo = new SetDriveTrainPosition(0, 70, -2, 3.25);
		addSequential(strafeRightTwo);
		addSequential(new SetDriveTrainPosition(18, 0, 0, .5));
		addSequential(new SetDriveTrainPositionPickUp(12, 0, 0, .5, .4, PickUpType.SMALL));
		addSequential(new HarvesterUp());
		addSequential(new SetDriveTrainPositionPickUp(-112,0, 0, 1.75,1.75,PickUpType.DROP));
		addSequential(new SetDriveTrainPosition(-12, 0, 0, .5));
		*/
		//Sergio
		/*
		addSequential(new ResetAuto());
		addSequential(new SetDriveTrainPosition(13, 0, 0, 2));
		addSequential(new SetDriveTrainPositionPickUp(0, 0, 0, 1, 0, PickUpType.FULL));
		addSequential(new SetDriveTrainPosition(18, 0, 0, 2));
		addSequential(new SetDriveTrainPositionPickUp(0, 0, 0, 1, 0, PickUpType.FULL));
		addSequential(new SetDriveTrainPosition(-36, 0, 0, 2));
		*/
		
		
		/*
		addSequential(new ResetAuto());
		
		addSequential(new SetDriveTrainPositionPickUp(0, 0, 0, .25, 0, PickUpType.FULL));
		addSequential(new SetDriveTrainPosition(-28, 0, 0, .7));
		SetDriveTrainPosition strafeRightOne = new SetDriveTrainPosition(0, 71, 0, 3.25); //73 = 81
		addSequential(strafeRightOne);
		
		addSequential(new SetDriveTrainPosition(22, 0, 0, .5));
		addSequential(new SetDriveTrainPositionPickUp(12, 0, 0, .5, .4, PickUpType.FULL));
		addSequential(new SetDriveTrainPosition(0, 0, 0,.25));
		addSequential(new SetDriveTrainPosition(-39, 0, 0, 1));
		//addSequential(new SetDriveTrainPosition(-12, 79, 8, 4));
		SetDriveTrainPosition strafeRightTwo = new SetDriveTrainPosition(0, 76, 0, 3.25);
		addSequential(strafeRightTwo);
		addSequential(new SetDriveTrainPosition(26, 0, 0, .5));
		addSequential(new SetDriveTrainPositionPickUp(12, 0, 0, .5, .4, PickUpType.SMALL));
		addSequential(new HarvesterUp());
		addSequential(new SetDriveTrainPositionPickUp(-112,0, 0, 1.75,1.75,PickUpType.DROP));
		addSequential(new SetDriveTrainPosition(-12, 0, 0, .5));
		*/
	
		/*
		SetDriveTrainPosition strafeRightOne = new SetDriveTrainPosition(0, 80, 0, 4);
		strafeRightOne.setStrafeSpinCompensation(0);
		addSequential(strafeRightOne);
		*/
		//addSequential(new SetDriveTrainPosition(-12, 80, 8, 4));
		//addSequential(new SetDriveTrainPosition(0, 80, 0, 4));
		//addSequential(new SetDriveTrainPosition(0,60,0,4));
	}

}
