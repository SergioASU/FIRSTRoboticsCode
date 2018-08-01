/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

//import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Programming
 */
public class BallDetector implements IDistSink 
{

    Preferences prefs = Preferences.getInstance();
   
    double grab_latency = 0.1;   //Time to close Grabber in seconds.
    double old_dist=0;      //Distance from last reading. Used to calculate the velocity.
    long old_t = 0;         //Time of the last reading in milliseconds.
    
    boolean m_grab= true;
    long m_grab_time = -1;
    long m_last_reading = -1;
    
    boolean m_ballInView = false;
    double m_ballAngle = 0;
    
    boolean sensorOn = true;
    
    LeddarWD ld = null;
    
    private static BallDetector m_instance = null;
    
    public static BallDetector getInstance()
    {
        if (m_instance == null) 
        {
            m_instance = new BallDetector();
        }
        return m_instance;
    }
    
    private BallDetector() 
    {
        old_dist = 0;   //Set initial distance to 0
        grab_latency= prefs.getDouble("GrabLatency", grab_latency); 
        prefs.putDouble("GrabLatency",grab_latency); //Read latency from dashboard and put default one if not existing
        ld = new LeddarWD(this);                //Creat a leddarWD class to communicate with the sensor.
    }
    
    public void start()
    {
        ld.start(); // start the sensor communications.
        System.out.println("LD Thread Started");
    }
    
    public boolean getGrab()
    {
        long timeToGrab = m_grab_time - System.currentTimeMillis();
        long timeSinceLastReading = System.currentTimeMillis() - m_last_reading;
        
        if (timeSinceLastReading>200) return false;
        
        if (distReading[4] < 0.25)
        {
            return true;
        }
        
        grab_latency = prefs.getDouble("GrabLatency", grab_latency);
        if (distReading[4] < (0.27 - m_velocity*grab_latency) )
        {
            return true;
        }
        //if (timeToGrab < -500) return false;
        //if (timeToGrab > 500) return false;
        //if (timeToGrab < 0) return true;
       
        return false;
    }
    
    public boolean getGrabTop()
    {
        long timeSinceLastReading = System.currentTimeMillis() - m_last_reading;
        
        if (timeSinceLastReading>200) return false;
        
        if (distReading[4] < 0.25)
        {
            return true;
        }
        return false;
    }
   
    double[] distReading= {0,0,0,0,0};
    long[] times = {0,0,0,0,0};
    double m_velocity =0;
    public void setDist(double dist, Object o)  // Called from sensor class each time a new reading is availble.
    {
        for(int i = 0; i < 4; i++)
        {
            distReading[i] = distReading[i+1];
            times[i] = times[i+1];
        }
        LeddarPck pck = (LeddarPck) o;
        distReading[4] = dist;
        times[4] = pck.m_time;
        
        long PERIOD = times[4]-times[3];
        double velocity = (distReading[4] - distReading[2])/((times[4]-times[2])/1000.0);
        if(Math.abs(velocity) < 9)
        {
            m_velocity = 0.0*m_velocity + 1.0  *velocity;
        }
        
        SmartDashboard.putNumber("IR-Speed", m_velocity);
        
        grab_latency = prefs.getDouble("GrabLatency", grab_latency);
        //System.out.println(String.valueOf(times[4]-times[3])+ "ms " + String.valueOf(dist) + " "  + String.valueOf(m_velocity));
        m_last_reading = System.currentTimeMillis();
        m_ballAngle = pck.m_angle;
        m_ballInView = pck.m_ballInView;
    }
}
