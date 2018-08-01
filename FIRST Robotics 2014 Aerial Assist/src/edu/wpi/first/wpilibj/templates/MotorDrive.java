
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Preferences;

public class MotorDrive 
{
    boolean life = false;
    
    Preferences prefs = Preferences.getInstance();
    
    Encoder rightEncoder = new Encoder(1,2);
    Encoder leftEncoder = new Encoder(3,4);
    //Joystick
    Joystick rcController = new Joystick(1);
    //Driving Variables
    public double throttle;
    public double rotation;
    //Drive Motors
    public SpeedController leftMotor1 = new Talon(1);
    public SpeedController leftMotor2 = new Talon(2);
    public SpeedController leftMotor3 = new Talon(3);
    public SpeedController rightMotor1 = new Talon(4);
    public SpeedController rightMotor2 = new Talon(5);
    public SpeedController rightMotor3 = new Talon(6);
    
    boolean pulseDrive = true;
    int accelerationCount = 0;
    boolean notAcceleratingForward = true;
    boolean notAcceleratingBackward = true;
    
    double backwardsValue = .75;
    double forwardsValue = 1;
    long backwardsTime = 100;
    long forwardsTime = 100;
    
    double kp = .002;
    double kf = .75;
    double posKP = .05;
    double posKD = -0.004;
    double maxRotationSpeed = 360;
    
    // Method to get gyro rate
    
   
    MotorDrive()
    {
        prefs.putDouble("GyroKP", kp);
        prefs.putDouble("GyroKF", kf);
        prefs.putDouble("MaxRotationSpeed", maxRotationSpeed);
        prefs.putDouble("posKP", posKP);
        prefs.putDouble("posKD", posKD);
        
        prefs.putDouble("FWDValue", forwardsValue);
        prefs.putDouble("BWDValue", backwardsValue);
        prefs.putLong("FWDTime", forwardsTime);
        prefs.putLong("BWDTime", backwardsTime);
        
    }
    
    public void update()
    {
        kp = prefs.getDouble("GyroKP", kp);
        kf = prefs.getDouble("GyroKF", kf);
        maxRotationSpeed = prefs.getDouble("MaxRotationSpeed", maxRotationSpeed);
        forwardsValue = prefs.getDouble("FWDValue", forwardsValue);
        backwardsValue = prefs.getDouble("BWDValue", backwardsValue);
        forwardsTime = prefs.getLong("FWDTime", forwardsTime);
        backwardsTime = prefs.getLong("BWDTime", backwardsTime);
        
        //SmartDashboard.putNumber("GyroKf", kp);
        //SmartDashboard.putNumber("GyroKf", kf);
        //SmartDashboard.putNumber("maxRotationspeed", maxRotationSpeed);
    }
        //System.out.println("Reached end of while");
    double LeddarKP = 1.0/30.0;
    
    double gyroSP = 0;
   
    public void robotDrive(Gyro myGyro,boolean autoBall,BallDetector bd)
    {
        if (rcController.getRawButton(8)) 
        {
            throttle = rcController.getY();
        }
        else
        {
            throttle = rcController.getAxis(Joystick.AxisType.kThrottle);
            if(throttle > 0)
            {
                throttle = throttle - .03937007874015748;
                throttle = throttle * 1.475;
            }
        }
      
        
        rotation = -rcController.getX();
        for(int i = 0; i < 8; i++)
        {
            SmartDashboard.putNumber("ax"+i, rcController.getRawAxis(i));
        }
        
        SmartDashboard.putNumber("Throttle", throttle);
        SmartDashboard.putNumber("Rotation", rotation);
        double rate = myGyro.getRate();
        SmartDashboard.putNumber("gRate", rate);
        //if (true) return;
        LeddarKP = prefs.getDouble("LeddarKP", LeddarKP);
        prefs.putDouble("LeddarKP", LeddarKP);
        double output=0;
        
        //Multiplier since full range of RC controller does not reach 1
        /*
        if(throttle > 0)
        {
            throttle = throttle - .03937007874015748;
            throttle = throttle * 1.475;
        }
        */
        double targetGyroValue = (rotation * maxRotationSpeed);
        double prop = (targetGyroValue - rate) * kp;
        double feedForward = rotation * kf;
        
        if(autoBall && bd.m_ballInView)
        {
            output = -
                    LeddarKP * bd.m_ballAngle;
            
        }
        else
        {
            //output = prop + feedForward;
              output = rotation+prop;
        }
        SmartDashboard.putNumber("Output2", output);
        
        
        //Position control mode
        if(rcController.getRawButton(1))
        {
           //normal
            gyroSP=myGyro.getAngle();
        }
        else
        {
          //position
            
            posKP = prefs.getDouble("posKP",posKP);
             posKD = prefs.getDouble("posKD",posKD);
            double error = myGyro.getAngle() - gyroSP;
            output = (25*rotation - error) * posKP + myGyro.getRate()*posKD;
        }
        
        
        leftMotor1.set(output + throttle);
        leftMotor2.set(output + throttle);
        leftMotor3.set(output + throttle);
        rightMotor1.set(output -throttle);
        rightMotor2.set(output -throttle);
        rightMotor3.set(output -throttle);
    }
    
    public void disableDriveMotors()
    {
        leftMotor1.set(0);
        leftMotor2.set(0);
        leftMotor3.set(0);
        rightMotor1.set(0);
        rightMotor2.set(0);
        rightMotor3.set(0);
    }
    
    public void pulseDriveForShot()
    {
        leftMotor1.set(-.1);
        leftMotor2.set(-.1);
        leftMotor3.set(-.1);
        rightMotor1.set(.1);
        rightMotor2.set(.1);
        rightMotor3.set(.1);
    }
    
    double angleSP=0;
    boolean valid_AngleSP = false;
    
    public void move(double speed,double rot, Gyro myGyro)
    {
       
        
        if(!valid_AngleSP)
        {
            valid_AngleSP = true;
            angleSP = myGyro.getAngle();
        }
        double rot_out = rot;
        if(Math.abs(rot) < 0.001)
        {
            double ang_kp = 1/30.0;
            double error = angleSP - myGyro.getAngle();
            rot_out = error*ang_kp;        
        }
        else
        {
            rot_out = rot;
            angleSP = myGyro.getAngle();
            valid_AngleSP = true;
        }
        
        double left =  speed-rot_out;
        double right = speed+rot_out;
        setLeft(left);
        setRight(right);
    }
    
    void setLeft(double val)
    {
         leftMotor1.set(-val);
         leftMotor2.set(-val);
         leftMotor3.set(-val);
    }
    
    void setRight(double val)
    {
         rightMotor1.set(val);
         rightMotor2.set(val);
         rightMotor3.set(val);
    }
    
    public void goHalfSpeedForward()
    {
         leftMotor1.set(-.75);
         leftMotor2.set(-.75);
         leftMotor3.set(-.75);
         rightMotor1.set(.75);
         rightMotor2.set(.75);
         rightMotor3.set(.75);
    }
    
    public void goHalfSpeedBackwards()
    {
        leftMotor1.set(.75);
        leftMotor2.set(.75);
        leftMotor3.set(.75);
        rightMotor1.set(-.75);
        rightMotor2.set(-.75);
        rightMotor3.set(-.75);
    }
    
    public void lifeAlert(double backwardsValue, double forwardValue, long backwardsTime, long forwardTime)
    {
        if (life) 
        {
            //Going backwards (Arms)
            leftMotor1.set(backwardsValue);
            leftMotor2.set(backwardsValue);
            leftMotor3.set(backwardsValue);
            rightMotor1.set(-backwardsValue);
            rightMotor2.set(-backwardsValue);
            rightMotor3.set(-backwardsValue);
            try {
                Thread.sleep(backwardsTime);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            //Going forwrad (Sheet metal)        
            leftMotor1.set(-forwardValue);
            leftMotor2.set(-forwardValue);
            leftMotor3.set(-forwardValue);
            rightMotor1.set(forwardValue);
            rightMotor2.set(forwardValue);
            rightMotor3.set(forwardValue);
            
            try {
                Thread.sleep(forwardTime);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
        
        
    }
    
 
}