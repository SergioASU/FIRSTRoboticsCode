/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author Borna
 */
public class LeddarWD extends Thread
{
    Leddar ld;
    IDistSink m_r;
    
    LeddarWD(IDistSink r)
    {
        super("LeddarWD");
        m_r = r;
        //this.setPriority(5);
    }
    
    public void run()
    {
        ld = new Leddar(m_r);
        ld.start();
        while(true)
        {
            ld.connect();
            try
            {
                Thread.sleep(1000);
                ld.last_read=System.currentTimeMillis();
                long now = System.currentTimeMillis();
                while(now-ld.last_read < 2000 )
                {
                    Thread.sleep(100);
                    now = System.currentTimeMillis();
                }
            } catch (InterruptedException ex)
            {
                ex.printStackTrace();
            }
            ld.stop();                    
        }
    }   
}