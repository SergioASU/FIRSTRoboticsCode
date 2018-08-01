
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

//~--- JDK imports ------------------------------------------------------------
import java.io.*;

import javax.microedition.io.*;

/**
 *
 * @author Programming
 */
public class Leddar extends Thread
{

    long last_read = 0;
    SocketConnection sc = null;

    InputStream is = null;
    OutputStream os = null;
    IDistSink m_r = null;
    
    double m_ballAngle = 0;
    boolean m_ballInView = false;

    Leddar(IDistSink r)
    {
        super("LeddarThread");
        m_r = r;

    }

    public void start()
    {
        super.start();
        this.setPriority(9);
    }

    private int read() throws IOException, InterruptedException, Exception
    {
        long st = System.currentTimeMillis();
        while (true) {
            if (is.available() > 0) {
                return is.read();
            }
            if (System.currentTimeMillis() - st > 100) {
                throw new Exception("IS read timed out.");
            }
            Thread.sleep(5);
        }
    }

    public void connect()
    {
        try {
            sc = (SocketConnection) Connector.open("socket://10.8.42.25:2222", Connector.READ_WRITE, true);
            sc.setSocketOption(SocketConnection.LINGER, 1);
            is = sc.openInputStream();
            os = sc.openOutputStream();
            os.write("Test".getBytes());
        } catch (Exception e) {
            System.err.println("Failed to open socket:" + e);
        }
    }

    void stop()
    {
        try {
            sc.close();
        } catch (IOException ex) {
        }
        try {
            is.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        sc = null;
        is = null;
        os = null;
    }

    public void run()
    {
        LeddarPck pck = new LeddarPck();
        DelFinder df = new DelFinder("$$$");
        byte buffer[] = new byte[1024];
        int state = 0;
        int packetLength = 0;

        while (true) {
            try {
                Thread.sleep(1);

                int d = 0;

                d = read();

                switch (state) {
                    case 0:    // looking for delim;
                        if (df.check((byte) d)) {
                            df.clear();
                            state = 1;
                        }

                        break;

                    case 1:    // read length and data;
                        packetLength = d * 5 + 6 + 2;
                        buffer[0] = (byte) d;

                        int bytesRead = 0;
                        long st = System.currentTimeMillis();
                        while (bytesRead < packetLength && System.currentTimeMillis() - st < 100) {
                            if (is.available() > Math.min(20, packetLength - bytesRead)) {
                                int bytesToReadNow = Math.min(is.available(),packetLength - bytesRead);
                                bytesRead += is.read(buffer, 1 + bytesRead, bytesToReadNow);
                            }
                            Thread.sleep(1);
                        }

                        if (pck.update(buffer, packetLength + 1)) {

                            // System.out.println(pck.toDistString());
                            m_r.setDist((pck.m_dist[7] + pck.m_dist[8]) / 200.0, pck);
                            this.m_ballAngle = pck.m_angle;
                            this.m_ballInView = pck.m_ballInView;
                            /*double minDist = 5000;
                            for(int i=0;i < 16; i++)
                            {
                                minDist = Math.min(minDist,pck.m_dist[i]);
                            }
                            m_r.setDist(minDist/100.0, pck);*/
                            
                            
                            
                        }

                        //os.write('a');
                        last_read = System.currentTimeMillis();
                        state = 0;

                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException ex1) {
                    ex1.printStackTrace();
                }
            }
        }

    }
}


//~ Formatted by Jindent --- http://www.jindent.com
