/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalSource;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.buttons.DigitalIOButton;

/**
 *
 * @author Borna
 */


public class LedComms
{
    DigitalOutput[] pins = new DigitalOutput[3];
    private static LedComms m_instance=null; 
    public static LedComms getInstance()
    {
        if (m_instance==null)
        {
            m_instance = new LedComms();
        }
        return m_instance;
    }
    
     
    
    private LedComms()
    {
              
                
        pins[0] = new DigitalOutput(9);
        pins[1] = new DigitalOutput(10);
        pins[2] = new DigitalOutput(11);
    }   
    
    public void setMode(byte mode)
    {
        /*
        pins[0].set(mode>0);
        pins[1].set(mode>0);
        pins[2].set(mode>0);
        /**/
        SmartDashboard.putBoolean("pin10", (mode & 0x1)> 0);
        pins[0].set((mode & 0x1)> 0);
        pins[1].set((mode & 0x2)> 0);
        pins[2].set((mode & 0x4)> 0);
        /**/
                SmartDashboard.putNumber("LED Mode", mode);
    }
}
