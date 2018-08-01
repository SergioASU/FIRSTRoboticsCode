package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm
{

    SpeedController grabberMotor = new Talon(7);
    Solenoid armSolenoid = new Solenoid(3);
    Solenoid armSolenoid2 = new Solenoid(4);
    boolean armClosed = false;
    double armDownSetpoint = 3.5;
    double armUpSetpoint = 1.5;

    AnalogChannel grabberPot = new AnalogChannel(2);
    PIDController grabberPID;
    double grabberKP = 1.5;
    double grabberKI = 0.0;
    double grabberKD = 0.0;
    double grabberPIDMin = 0;
    double grabberPIDMax = 5;
    double grabberUp = 1.14;//Comp1.13;
    //double grabberUp = 1.7;
    double grabberDown =  2.51;//Comp2.54;
    double grabberSetpoint = 1.14;//Comp1.13
    //double grabberSetpoint = 1.7;
    double grabberBackStop = 2.15;
    
    double grabberMiddle = 2.15;
    double grabberCounter = 0;
    boolean ballGrabbed = false;
    Preferences prefs = Preferences.getInstance();

    Arm()
    {
        prefs.putDouble("GrabberKP", grabberKP);
    }

    public void update()
    {
        grabberKP = prefs.getDouble("GrabberKP", grabberKP);
        SmartDashboard.putNumber("GrabberKPdb", grabberKP);
    }
    
    
    Runnable ArmRun = new Runnable()
    {
        public void run()
        {
            while(true)
            {   
                 grabberMotor.set(grabberKP * (grabberSetpoint - grabberPot.getVoltage()));
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                 
            }
        }
    };
    Thread ArmThread = new Thread(ArmRun);

    public void open()
    {
        armSolenoid.set(true);
        armSolenoid2.set(true);
    }
    
    Runnable grabberRun = new Runnable()
    {
        public void run()
        {

            while (true) {
                try {
                   

                    if (grabberCounter == 1) {
                        //Open grabber
                        armSolenoid.set(true);
                        armSolenoid2.set(true);
                        armClosed = false;
                        grabberCounter = 0;
                        Thread.sleep(250);
                    }

                    if (grabberCounter == 2) {
                        //Close grabber
                        armSolenoid.set(false);
                        armSolenoid2.set(false);
                        armClosed = true;
                        grabberCounter = 0;
                        Thread.sleep(250);
                    }

                    if (grabberCounter == 3) {
                        //Shimmy Twerk
                        armSolenoid.set(true);
                        armSolenoid2.set(true);
                        armClosed = false;
                        Thread.sleep(90);

                        armSolenoid.set(false);
                        armSolenoid2.set(false);
                        armClosed = true;
                        Thread.sleep(250);
                        grabberCounter = 0;
                    }

                    if (grabberCounter == 4) {
                        //Auto closing and picking up
                        armSolenoid.set(false);
                        armSolenoid2.set(false);
                        armClosed = true;
                        Thread.sleep(250);

                        grabberSetpoint = grabberUp;

                        boolean grabberAtPosition = false;
                        while (!grabberAtPosition) {
                            grabberMotor.set(grabberKP * (grabberSetpoint - grabberPot.getVoltage()));
                            if (grabberMotor.get() > -.05 && grabberMotor.get() < .05) {
                                grabberAtPosition = true;
                            }
                        }

                        //Thread.sleep(750);
                        armSolenoid.set(true);
                        armSolenoid2.set(true);
                        armClosed = false;

                        Thread.sleep(50);
                        armSolenoid.set(false);
                        armSolenoid2.set(false);
                        armClosed = true;
                        Thread.sleep(100);

                        ballGrabbed = true;
                        grabberCounter = 0;

                    }

                    Thread.sleep(10);
                } catch (InterruptedException iex) {

                }
            }

        }
    };

    Runnable openGrabber = new Runnable()
    {
        public void run()
        {
            armSolenoid.set(true);
            armSolenoid2.set(true);
            armClosed = false;
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    };

    Runnable closeGrabber = new Runnable()
    {
        public void run()
        {
            armSolenoid.set(false);
            armSolenoid2.set(false);
            armClosed = true;
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    };

    Runnable shimmyTwerk = new Runnable()
    {
        public void run()
        {
            armSolenoid.set(true);
            armSolenoid2.set(true);
            armClosed = false;
            try {
                Thread.sleep(165);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            armSolenoid.set(false);
            armSolenoid2.set(false);
            armClosed = true;
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    };

    //Thread openGrabberThread = new Thread(openGrabber);
    //Thread closeGrabberThread = new Thread(closeGrabber);
    //Thread shimmyTwerkThread = new Thread(shimmyTwerk);
    Thread grabberThread = new Thread(grabberRun, "GrabberThread");
}
