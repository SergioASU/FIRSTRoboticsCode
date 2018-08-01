
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Gyro;

public class PID 
{
    //Auto Driving
    public double aKp = 0.03;
    public double aKi = 0.0;
    public double aKd = 0.02;
    public double aKf = 0.0;
    public double range = 360;
    PIDController rightPID1;
    PIDController rightPID2;
    PIDController rightPID3;
    PIDController leftPID1;
    PIDController leftPID2;
    PIDController leftPID3;
    double autoDriveSetPoint = 5;
    
   
    
    public void setUpEncodersPID(Encoder rightEnc, Encoder leftEnc, int pulse)
    {
        rightEnc.setDistancePerPulse(pulse);
        leftEnc.setDistancePerPulse(pulse);
        
        rightEnc.start();
        leftEnc.start();
        
        rightEnc.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
        leftEnc.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
    }
    
    public PIDController createPID(PIDController pid, double kp, double ki, double kd, double kf, Gyro gyro, SpeedController speedController)
    {
        pid = new PIDController(kp, ki, kd, kf, gyro, speedController);
        return pid; 
    }
    
    public void setUpPID(PIDController pid, double range)
    {
        pid.enable();
        
        pid.setInputRange(-range, range);
        pid.setInputRange(-100, 100); 
    }
    
    public void disableAllPID(PIDController one, PIDController two, PIDController three, PIDController four, PIDController five, PIDController six)
    {
        one.disable();
        two.disable();
        three.disable();
        four.disable();
        five.disable();
        six.disable();
    }
    
    public void setAllSetPoints(PIDController one, PIDController two, PIDController three, PIDController four, PIDController five, PIDController six, double amount)
    {
        one.setSetpoint(-amount);
        two.setSetpoint(-amount);
        three.setSetpoint(-amount);
        four.setSetpoint(amount);
        five.setSetpoint(amount);
        six.setSetpoint(amount);
    }
}
