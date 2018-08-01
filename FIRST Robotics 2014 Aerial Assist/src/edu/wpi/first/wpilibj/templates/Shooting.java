
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogChannel;
public class Shooting 
{
    boolean overRideSensor = false;
    Preferences prefs = Preferences.getInstance();
    public Joystick pulseController = new Joystick(3);
    Solenoid catapult = new Solenoid(1);
    Solenoid steve = new Solenoid(2);
    Compressor compressor = new Compressor(5, 1);
    
    double pulseOneSeconds = .04;
    double pulseTwoSeconds = .2;
    double pulseGap = .03;
    double pulseThreeSeconds = .1;
    double pulseGap2 = .1;
    boolean pulse1 = true;
    boolean pulse2 = false;
    boolean pulse3 = true;
    double trussSteveDelay = .1;
    double steveDelay = .1;//NewArm
    double autoBallNumber = 2;
    
    Timer time2 = new Timer();
     
    double highShot = .2;
    double highShotPulse1 = .040;
    double highShotGap = .015;
    double highShotPulse2 = .15;
    double trussShot = .105;
    double highShotFromLowGoal = .07;//New Arm
    double lowShot = .025;
    double autoShot = .2;
    Timer time = new Timer();
    int shootingCount = 0;
    double highShotWithStevePulse = .11;//New Arm
    double highShotWithSteveDelay = .065;//New Arm
    double lowShotWithLowAir = .05;
    BallDetector bd = BallDetector.getInstance();
    
    boolean ballSensorOn = true;
    
    
    Arm m_arm;
    
    Shooting(Arm arm)
    {
        m_arm = arm;
        //High Shot
        prefs.putDouble("HighShotPulseOne", highShotPulse1);
        prefs.putDouble("HighShotGap", highShotGap);
        prefs.putDouble("HighShotPulseTwo", highShotPulse2);
        
        prefs.putDouble("Auto", autoBallNumber);
        
        //High Shot From Low Goal
        prefs.putDouble("HighShotFromLowGoalPulse", highShotFromLowGoal);
        prefs.putDouble("HighShotFromLowGoalDelay", steveDelay);
        
        //Truss Shot
        prefs.putDouble("TrussShotPulse", trussShot);
        
        //Low Shot
        prefs.putDouble("LowShotPulse", lowShot);
        
        prefs.putDouble("HighShotP", highShotWithStevePulse);
    }
    
    public void updateShooting()
    {
        highShotPulse1 = prefs.getDouble("HighShotPulseOne", pulseGap);
        highShotGap = prefs.getDouble("HighShotGap", highShotGap);
        highShotPulse2 = prefs.getDouble("HighShotPulseTwo", highShotPulse2);
        
        autoBallNumber = prefs.getDouble("Auto", autoBallNumber);
        
        //High Shot From Low Goal
        highShotFromLowGoal = prefs.getDouble("HighShotFromLowGoalPulse", highShotFromLowGoal);
        steveDelay = prefs.getDouble("HighShotFromLowGoalDelay", steveDelay);
        
        highShotWithStevePulse = prefs.getDouble("HighShotP", highShotWithStevePulse);
        
        //Truss Shot
        trussShot = prefs.getDouble("TrussShotPulse", trussShot);
        
        //Low Shot
        lowShot = prefs.getDouble("LowShotPulse", lowShot);
        
        //High Goal Shot Display
        SmartDashboard.putNumber("High Goal Shot Pulse One", highShotPulse1);
        SmartDashboard.putNumber("High Goal Shot Gap", highShotGap);
        SmartDashboard.putNumber("High Goal Shot Pulse Two", highShotPulse2);
                
        //Truss Shot Display
        SmartDashboard.putNumber("Truss Shot Pulse", trussShot);
        
        //Low Shot Display
        SmartDashboard.putNumber("Low Goal Shot Pulse", lowShot);
        
        //High Goal Shot From Low Goal Position Display
        SmartDashboard.putNumber("Delay In Between Steve Fingers and Shot", steveDelay);
        SmartDashboard.putNumber("High Goal Shot From Low Goal Position Pulse", highShotFromLowGoal);
        
        
    }
 
    public void singlePulse(Timer timer, double pulse, Solenoid solenoid1)
    {
        
        solenoid1.set(true);
        try {
            Thread.sleep((long) (pulse*1000));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        solenoid1.set(false);
       
    }
    
    public void highShotFromLowGoal(Timer timer, double pulse, double delay, Solenoid solenoid1, Solenoid steve)
    {
        
        
        steve.set(true);
        try {
            Thread.sleep((long) (delay*1000));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        
        
        solenoid1.set(true);
        try {
            Thread.sleep((long) (pulse*1000));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        solenoid1.set(false);
        
        timer.stop();
        timer.reset(); 
        steve.set(false);
    }
    
    public void highShotWithSteveDelay(Timer timer, double pulse, double delay, Solenoid solenoid1, Solenoid steve)
    {
        
        m_arm.open();
        try {
            Thread.sleep((long) (600));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        
        steve.set(true);
        try {
            Thread.sleep((long) (delay*1000));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
                
                
                
                        
        solenoid1.set(true);
        try {
            Thread.sleep((long) (pulse * 1000));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        solenoid1.set(false);
        timer.stop();
        timer.reset(); 
        steve.set(false);
    }
    
    public void highShotWithSteveDelayAuto(Timer timer, double pulse, double delay, Solenoid solenoid1, Solenoid steve)
    {
        
        //m_arm.open();
        try {
            Thread.sleep((long) (300));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        steve.set(true);
        try {
            Thread.sleep((long) (delay*1000));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
                
        
        solenoid1.set(true);
        try {
            Thread.sleep((long) (pulse * 1000));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        
        solenoid1.set(false);
        timer.stop();
        timer.reset(); 
        steve.set(false);
    }
    
    public void trussShot(Timer timer, double pulse, double delay, Solenoid solenoid1, Solenoid steve)
    {
        solenoid1.set(true);
        try {
            Thread.sleep((long) (pulse*1000));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        solenoid1.set(false);
        steve.set(false);
    }
  
    public void highShot(Timer timer, double pulse, double pulse2, double gap, Solenoid solenoid1)
    {
        solenoid1.set(true);
        System.out.println("SetSolenoid1");
        try {
            Thread.sleep((long) (pulse*1000));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        solenoid1.set(false);
        
       
        try {
            Thread.sleep((long) (gap*1000));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        
        solenoid1.set(true);
        try {
            Thread.sleep((long) (pulse2*1000));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        solenoid1.set(false);
        
        
    }
    
    //LedComms leds = LedComms.getInstance();
    Runnable shootingRun = new Runnable()
    {
        public void run()
        {
            while(true)
            {
                try
                {
                   
                        if (!bd.getGrab() && !overRideSensor && shootingCount != 10) 
                        {
                            //leds.setMode((byte)0);
                        
                            shootingCount = 0;
                        }
                        else
                        {
                            //leds.setMode((byte)1);
                        }
                        
                    
                    
                    
                    if(shootingCount == 1)
                    {
                        singlePulse(time, lowShot, catapult);
                        shootingCount = 0;
                        Thread.sleep(1000);
                    }
            
                    if(shootingCount == 2)
                    {
                        //TRUSS
                        Thread.sleep(200);
                        highShotFromLowGoal(time, trussShot, trussSteveDelay, catapult, steve);
                        shootingCount = 0;
                        Thread.sleep(1000);
                    }
                    
                    if(shootingCount == 3)
                    {
                        highShotFromLowGoal(time, highShotFromLowGoal, steveDelay, catapult, steve);
                        shootingCount = 0;
                        Thread.sleep(1000);
                    }
            
                    if(shootingCount == 4)
                    {
                        //robotDrive.pulseDriveForShot();
                        System.out.println("ShootingThread = 5");
                        //highShot(time, highShotPulse1, highShotPulse2, highShotGap, catapult);  
                        highShotWithSteveDelay(time, highShotWithStevePulse, highShotWithSteveDelay, catapult, steve);
                        shootingCount = 0;
                        Thread.sleep(1000);
                    }
                    
                    if(shootingCount == 5)
                    {
                        //robotDrive.pulseDriveForShot();
                        System.out.println("ShootingThread = 5");
                        //highShot(time, highShotPulse1, highShotPulse2, highShotGap, catapult);  
                        highShotWithSteveDelayAuto(time, highShotWithStevePulse, highShotWithSteveDelay, catapult, steve);
                        shootingCount = 0;
                        Thread.sleep(1000);
                    }
                    
                    if(shootingCount == 6)
                    {
                        singlePulse(time, lowShotWithLowAir, catapult);
                        shootingCount = 0;
                        Thread.sleep(1000);
                    }
                    
                    if(shootingCount == 7 || shootingCount == 10)
                    {
                        //robotDrive.pulseDriveForShot();
                        System.out.println("ShootingThread = 5");
                        //highShot(time, highShotPulse1, highShotPulse2, highShotGap, catapult);  
                        highShotWithSteveDelayAuto(time, highShotWithStevePulse, (highShotWithSteveDelay), catapult, steve);
                        shootingCount = 0;
                        Thread.sleep(1000);
                    }
                    
                    shootingCount = 0;
                    
                    Thread.sleep(10);
                }
                catch(InterruptedException iex)
                {
                    
                }
            }
        }
    };
    Thread shootingThread = new Thread(shootingRun);
 } 