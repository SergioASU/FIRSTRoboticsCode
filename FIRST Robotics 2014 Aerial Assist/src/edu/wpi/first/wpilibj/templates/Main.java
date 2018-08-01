
package edu.wpi.first.wpilibj.templates;
// Imports
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.TimerTask;

public class Main extends SimpleRobot
{
    //////////////////////////////CHANGE ENCODER ON AUTO TO POSITIVE FOR REAL BOT
    Relay photonCannon = new Relay(3);
    //Relay photonCannon = new Relay(2);
    //Relay psiLights = new Relay(2);
    Button middleBtn = new Button(8);
    Button sideButton1 = new Button(7);
    Button sideButton2 = new Button(6);
    
    Preferences prefs = Preferences.getInstance();
    PID pid = new PID();
    MotorDrive robotDrive = new MotorDrive();
    boolean shootingBool = false;
   
    SmartDashboard smartDashboard = new SmartDashboard();
    Joystick coPilot = new Joystick(2);
    
    Arm arm = new Arm();
    Shooting shooting = new Shooting(arm);
    Timer time = new Timer();
    Timer time2 = new Timer();
    
    AnalogChannel ultraSonic = new AnalogChannel(3);
    AnalogChannel psiSensor = new AnalogChannel(7);
    AnalogChannel psiSensor2 = new AnalogChannel(6);
   
    double autoRightEncoderSet = 1000;
    double autoLeftEncoderSet  = 1500;
    int countForPulse = 1;
    boolean singlePulse = true;
    boolean doublePulse = false;
    boolean autoDone = false;
    boolean ballGrabbed = false;
    boolean buttonsEnabled = true;
    boolean startThreads = true;
    boolean canShoot = false;
    
    boolean lifeAlert = false;
    
    double threshHold = .15;
    
    Gyro gyro = new Gyro(1);
    double gyroRate = 0;
    int count = 0;
    double oldAngle = 0;
    double targetGyroValue = 0;
    double output = 0;
   
    BallDetector bd = BallDetector.getInstance();
    
    double psi60 = 0;
    double psi120 = 0;
 
    
    public void getGyroRate()
    {
        gyroRate = gyro.getRate();
        
        // Calculate gyro rate every 100 counts or .2 seconds
        /*
        while (true)
        {
            SmartDashboard.putNumber("GyroR", gyroRate);
            if (count >= 100)
            {
                //System.out.println("GyroRate " + gyroRate);
                // Change calculation based on gyro direction
                if (gyro.getAngle() < 0)
                {
                    gyroRate = (oldAngle - gyro.getAngle());
                    oldAngle = gyro.getAngle();
                    count = 0;
                }
                else
                {
                    gyroRate = (gyro.getAngle() - oldAngle);
                    oldAngle = gyro.getAngle();
                    count = 0;
                }

                // Eliminate noise
                if (gyroRate < 1 && gyroRate > -1)
                {
                    gyroRate = 0;
                }
                
                break;
            }
            count++;
        }
        */
        
    }
    
    public void robotInit()
    {
       shooting.shootingThread.start();
       arm.grabberThread.start(); 
       arm.ArmThread.start();
       shooting.compressor.start();
       bd.start();
    }
    
    public void autonomous() 
    {
        shooting.updateShooting();
        autoDone = false;
        shooting.compressor.start();
        gyro.reset();
        robotDrive.leftEncoder.stop();
        robotDrive.rightEncoder.stop();
        robotDrive.leftEncoder.reset();
        robotDrive.rightEncoder.reset();
        robotDrive.leftEncoder.start();
        robotDrive.rightEncoder.start();
        double distanceToLowGoal = -3800;
        double distance = 2150;
        shooting.ballSensorOn = false;
        
        if(shooting.autoBallNumber == 1)
        {
            arm.armSolenoid.set(true);
            arm.armSolenoid2.set(true);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        
            arm.armSolenoid.set(false);
            arm.armSolenoid2.set(false);
        
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        
            while(isAutonomous() && isEnabled() && !autoDone)
            {
                robotDrive.move(1,0,gyro);
                if (robotDrive.rightEncoder.getDistance() >= distance) 
                {
                    robotDrive.disableDriveMotors();
                    autoDone = true;
                }
            }
        
            try {
                Thread.sleep(750);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            arm.armSolenoid.set(true);
            arm.armSolenoid2.set(true);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        
            //shooting.shootingCount = 3;
            shooting.shootingCount = 5;
        
        }
        
        if(shooting.autoBallNumber == 2)
        {
            arm.armSolenoid.set(true);
            arm.armSolenoid2.set(true);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            arm.grabberSetpoint = arm.grabberDown;
        
            try {
                Thread.sleep(400);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        
            arm.armSolenoid.set(false);
            arm.armSolenoid2.set(false);
        
            try {
                Thread.sleep(350);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        
       
            while(isAutonomous() && isEnabled() && !autoDone)
            {
                robotDrive.move(1,0,gyro);
                if (robotDrive.rightEncoder.getDistance() >= distance) 
                {
                    robotDrive.disableDriveMotors();
                    autoDone = true;
                }
            }
        
            try {
                Thread.sleep(750);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        
            //shooting.shootingCount = 3;
            shooting.shootingCount = 10;
            //shooting.ballSensorOn = true;
        
            try {
                Thread.sleep(1750);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        
            arm.grabberSetpoint = arm.grabberUp;
        
            try {
                Thread.sleep(1250);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        
            arm.armSolenoid.set(true);
            arm.armSolenoid2.set(true);
        
            try {
                Thread.sleep(1250);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        
            //shooting.shootingCount = 3;
            shooting.shootingCount = 5;
        }
                  
        /*
        double firstForwardEncoderTicks = 1730;
        
        boolean firstPositionReached = false;
        boolean secondPositionReached = false;
        boolean gotBall = false;
        
        arm.armSolenoid.set(true);
        arm.armSolenoid2.set(true);
        try {
            Thread.sleep(550);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        arm.armSolenoid.set(false);
        arm.armSolenoid2.set(false);
        
        try {
            Thread.sleep(400);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        while(isAutonomous() && isEnabled() && !autoDone)
        {
            middleBtn.update();
            sideButton1.update();
            sideButton2.update();
            while(!firstPositionReached && isAutonomous())
            {
                System.out.println("Right Encoder: " + robotDrive.rightEncoder.getDistance());
                robotDrive.move(0.5,0,gyro);
                
                if(robotDrive.rightEncoder.getDistance() > firstForwardEncoderTicks)
                {
                    robotDrive.disableDriveMotors();
                    firstPositionReached = true;
                }
            }
            arm.grabberCounter = 1;
            try {
        
                Thread.sleep(750);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            shooting.shootingCount = 4;
            try {
                Thread.sleep(250);
                //End of One ball auto
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            arm.grabberSetpoint = arm.grabberDown;
            middleBtn.update();
            sideButton1.update();
            sideButton2.update();
            while(!secondPositionReached && isAutonomous())
            {
                System.out.println("Left Encoder: " + robotDrive.leftEncoder.getDistance());
                middleBtn.update();
                sideButton1.update();
                sideButton2.update();
                robotDrive.move(-0.5,0,gyro);
                
                if (middleBtn.getPressed() || sideButton1.getPressed() || sideButton2.getPressed())
                {
                    boolean madeIt = false;
                    arm.grabberCounter = 4;
                    robotDrive.move(0.5,0,gyro);
                    while(!madeIt)
                    {
                        if(robotDrive.rightEncoder.getDistance() >= 1800)
                        {
                            robotDrive.disableDriveMotors();
                            madeIt = true;
                        }
                    }
                    robotDrive.disableDriveMotors();
                    arm.grabberCounter = 1;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    shooting.shootingCount = 4;
                    secondPositionReached = true;
                }
                
                else if(robotDrive.rightEncoder.getDistance() <= -1000)
                {
                    System.out.println("Left Encoder: " + robotDrive.leftEncoder.getDistance());
                    robotDrive.move(0.5,0,gyro);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    robotDrive.disableDriveMotors();
                    secondPositionReached = true;
                }
            }
            autoDone = true;
        }
        */
        robotDrive.leftEncoder.reset();
        robotDrive.rightEncoder.reset();
    }
              
    //Tele-Op
    public void operatorControl() 
    { 
        
        //arm.armSolenoid.set(false);
        //arm.armSolenoid2.set(false);
        //arm.armClosed = true;
        gyro.reset();
        
        robotDrive.leftEncoder.reset();
        robotDrive.rightEncoder.reset();
        
        robotDrive.leftEncoder.start();
        robotDrive.rightEncoder.start();
        shooting.compressor.start();
        
        while (true && isOperatorControl() && isEnabled()) // loop until change
        {
            psi60 = (psiSensor.getVoltage() -.5) * 25;  
            SmartDashboard.putNumber("PSI LowSide: ", psi60);
            SmartDashboard.putNumber("PSI: LowSide", psi60);
            
            psi120 = (((psiSensor2.getVoltage() /99.6) - .004) / .016) * 200;
            SmartDashboard.putNumber("PSI HighSide:", psi120);
            SmartDashboard.putNumber("PSI HighSide:", psi120);
            
            if(psi120 < 60.0)
            {
                //psiLights.set(Relay.Value.kOn);
            }
            else
            {
                //psiLights.set(Relay.Value.kOff);
            }
            try
            {
                Thread.sleep(10);
            } catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            SmartDashboard.putNumber("Right E: ", robotDrive.rightEncoder.getDistance());
            SmartDashboard.putNumber("Gyro ", gyro.getAngle());
            getGyroRate();
            middleBtn.update();
            sideButton1.update();
            sideButton2.update();
            robotDrive.update();
            arm.update();
            shooting.updateShooting();
            
            robotDrive.robotDrive(gyro,coPilot.getRawButton(9), bd);
            SmartDashboard.putNumber("Grabber Pot", arm.grabberPot.getVoltage());
            
            if(buttonsEnabled == true)
            {
                if (middleBtn.getPressed() || sideButton1.getPressed() || sideButton2.getPressed() && !arm.ballGrabbed) 
                {
                   // arm.grabberCounter = 4;
                   // arm.ballGrabbed = true;     
                }
                
                /*
                if(arm.grabberSetpoint != arm.grabberUp)
                {
                
                    if(bd.getGrab()&& !arm.ballGrabbed)
                    {
                        arm.grabberCounter = 2;
                        arm.ballGrabbed = true;    
                    }
                }
                else
                {
                    if(bd.getGrabTop()&& !arm.ballGrabbed)
                    {
                        arm.grabberCounter = 2;
                        arm.ballGrabbed = true;    
                    }
                }
               if(bd.getGrab() && arm.ballGrabbed)
               {
                   canShoot = true;
               }
                */
                
            }
            
            if (coPilot.getRawButton(7) && coPilot.getRawButton(4)) 
            {
                //arm.grabberCounter = 1;
                shooting.shootingCount = 2;
                arm.ballGrabbed = false;
            }
            
            if(robotDrive.rcController.getRawButton(1))
            {
                photonCannon.set(Relay.Value.kOff);
            }
            else
            {
                photonCannon.set(Relay.Value.kOn);
            }
            
            if(coPilot.getRawButton(7) && coPilot.getRawButton(2))
            {
                shooting.shootingCount = 1;
                arm.ballGrabbed = false;
            }
            else if(coPilot.getRawButton(2))
            {
                arm.grabberCounter = 3;
            }
            
            if (coPilot.getRawButton(7) && coPilot.getRawButton(3)) 
            {
                shooting.shootingCount = 6;
                arm.ballGrabbed = false;
                
            }
            
            else if(coPilot.getRawButton(3))
            {
                arm.grabberCounter = 1;
                buttonsEnabled = false;
                arm.ballGrabbed = false; 
            }
            
            if (robotDrive.rcController.getRawButton(2)) 
            {
                robotDrive.life = true;
                robotDrive.lifeAlert(robotDrive.backwardsValue ,robotDrive.forwardsValue , robotDrive.backwardsTime,robotDrive.forwardsTime);
            }
            else
            {
                robotDrive.life = false;
            }
             
            
            if(coPilot.getRawButton(7) && coPilot.getRawButton(8))
            {
                
                System.out.println("tried to shoot.");
                shooting.shootingCount = 4;
                arm.ballGrabbed = false;
            }
            
            if(coPilot.getRawButton(7) && coPilot.getRawButton(6))
            {
                shooting.shootingCount = 3;
                arm.ballGrabbed = false;   
            }
            
            if(coPilot.getRawButton(1))
            {
                arm.grabberCounter = 2;
            }
            
            if(coPilot.getRawButton(3))
            {
                /*
                arm.grabberCounter = 1;
                buttonsEnabled = false;
                arm.ballGrabbed = false; 
                */
            }
            
            double coPilotY = coPilot.getY();
            double coPilotX = coPilot.getX();
            SmartDashboard.putNumber("Joystick Val: ", coPilotY);
            
            
            if(-coPilotY > .7)
            {
                arm.grabberSetpoint = arm.grabberUp;
                //arm.ballGrabbed = false;
            }
            else if(-coPilotY < -.7)
            {
                arm.grabberSetpoint = arm.grabberDown;
                //arm.ballGrabbed = false;
            }
            if (coPilotX > .7) 
            {
                arm.grabberSetpoint = arm.grabberMiddle;
                
            }
            
            else if(coPilotX < -.7)
            {
                arm.grabberSetpoint = arm.grabberBackStop;
            }
            
            if (coPilot.getRawButton(11)) 
            {
                shooting.overRideSensor = true;
            }
            else
            {
                shooting.overRideSensor = false;
            }
            /*
            if(coPilot.getRawButton(11))
            {
                arm.grabberSetpoint = arm.grabberUp;
            }
            
            if(coPilot.getRawButton(12))
            {
                arm.grabberSetpoint = arm.grabberDown;
            }
                    */
            LedComms leds = LedComms.getInstance();
            if(coPilot.getRawButton(5))
            {
                buttonsEnabled = false;
                leds.setMode((byte)0);
                
            }
            else
            {
                buttonsEnabled = true;
                leds.setMode((byte)1);
            }
            
            if(coPilot.getRawButton(10))
            {
                arm.ballGrabbed = false;
                
            }
        }
    }
    
    public void disabled()
    {
        while(isDisabled())
        {
            try {
                SmartDashboard.putNumber("Grabber Pot", arm.grabberPot.getVoltage());
                SmartDashboard.putNumber("Right Encoder: ", robotDrive.rightEncoder.getDistance());
                
                SmartDashboard.putNumber("Gyro ", gyro.getAngle());
                Thread.sleep(10L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            double rate = gyro.getRate();
            SmartDashboard.putNumber("gRate", rate);
            
        }
        
    }
    
    public void test()
    {
        for(int i = 0; i < 8; i++)
        {
            SmartDashboard.putNumber("ax"+i, robotDrive.rcController.getRawAxis(i));
        }
    }
    
}