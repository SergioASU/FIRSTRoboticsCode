/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

//import java.text.DecimalFormat;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import java.text.NumberFormat;
//import java.util.Arrays;
//import java.util.Formatter;
/**
 *
 * @author Borna
 */
public class LeddarPck
{

    int m_detections = 0;
    byte m_buffer[];
    int m_length = 0;
    long m_time = 0;
    int m_dist[] = new int[16];
    double m_angle = 0;
    boolean m_ballInView = false;

    public LeddarPck()
    {
        m_buffer = new byte[1024];
    }

    boolean update(byte buffer[], int length)
    {
        m_detections = buffer[0];
        m_length = length + 2;
        m_buffer[0] = 1;
        m_buffer[1] = 0x41;
        System.arraycopy(buffer, 0, m_buffer, 2, length);

        /*
         System.out.println();
         System.out.println();
         System.out.println(this.toHexString());
         System.out.println();
         System.out.println();
         */
        //check CRC;
        int crcoff = 3 + m_detections * 5 + 6;
        int calccrc = doCRC16(m_buffer, 0, crcoff);
        int rxcrc = bytesToInt(m_buffer, crcoff);

        byte[] crca = {(byte) (calccrc & 0xFF), (byte) ((calccrc & 0xFF00) >> 8)};
        //System.out.print(bytesToHex(crca, 2));

        //System.out.println("calccrc: " + String.valueOf(calccrc)+ " rxcrc: " + String.valueOf(rxcrc));
        if (calccrc == rxcrc) {
            setAll(m_dist, 5000);

            for (int i = 0; i < m_detections; i++) 
            {
                byte sub[] = new byte[5];
                for (int j = 0; j < 5; j++) {
                    sub[j] = m_buffer[i * 5 + 3 + j];
                }
                int dist = ((int) sub[0] & 0xFF) + ((int) sub[1] & 0xff) * 256;
                int seg = ((int) sub[4] & 0xf0) >> 4;

                double amp = (((int) sub[2] & 0xFF) + ((int) sub[3] & 0xff) * 256) / 64;
                if (amp > 50) {
                    m_dist[seg] = Math.min(dist, m_dist[seg]);
                }
            }
            int eoff = 3 + m_detections * 5; //offset for the timestamp
            m_time = bytesToLong(m_buffer, eoff);
            
            
            double avgDist = 0;
            for (int i = 0; i < 16; i++)
            {
                avgDist += m_dist[i]/100.0;
            }
            
            double[] w= new double[16];
            
            double angleSum = 0;
            double wsum = 0;
            for (int i = 0; i < 16; i++)
            {
                double d = m_dist[i]/100.0;
                if (m_dist[i] > avgDist)
                    w[i] = 0;
                else
                    w[i] = 1/d;
                angleSum += segToAngle(i)*w[i];
                wsum += w[i];
            }
            m_angle = angleSum/wsum;
            SmartDashboard.putNumber("BallAngle",m_angle);
            
            if (wsum > 0.001)
            {
                m_ballInView = true;
            }
            else
            {
                m_ballInView = false;
            }
            
            
            return true;
        } else {
            return false;
        }
    }
    
    double segToAngle(int seg)
    {
        return (seg - 7.5)*2.8; 
    }

    long bytesToLong(byte[] buff, int offset)
    {
        long resp = 0;
        for (int i = 0; i < 4; i++) {
            resp += ((long) buff[offset + i] & 0xFF) << 8 * i;
        }
        return resp;
    }

    int bytesToInt(byte[] buff, int offset)
    {
        int resp = 0;
        for (int i = 0; i < 2; i++) {
            resp += ((int) buff[offset + i] & 0xFF) << 8 * i;
        }
        return resp;
    }

    private void setAll(int[] o, int val)
    {
        for (int i = 0; i < o.length; i++) {
            o[i] = val;
        }
    }

    String toDistString()
    {
        //DecimalFormat f = new DecimalFormat("### ");

        String resp = new String();
        resp = resp + String.valueOf(m_detections) + ":";
        for (int i = 0; i < m_dist.length; i++) {
            resp = resp + String.valueOf(m_dist[i]) + " ";
        }
        return resp;
    }

    String toHexString()
    {
        return bytesToHex(m_buffer, m_length);
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes, int length)
    {
        char[] hexChars = new char[length * 3];
        for (int j = 0; j < length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }

    private static final int[] CRC16_TAB
            = {
                0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241,
                0xC601, 0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440,
                0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81, 0x0E40,
                0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841,
                0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1, 0xDA81, 0x1A40,
                0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0, 0x1C80, 0xDC41,
                0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641,
                0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1, 0xD081, 0x1040,
                0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240,
                0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0, 0x3480, 0xF441,
                0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41,
                0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840,
                0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0, 0x2A80, 0xEA41,
                0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1, 0xEC81, 0x2C40,
                0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640,
                0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0, 0x2080, 0xE041,
                0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240,
                0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0, 0x6480, 0xA441,
                0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41,
                0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840,
                0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0, 0x7A80, 0xBA41,
                0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40,
                0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640,
                0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0, 0x7080, 0xB041,
                0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241,
                0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1, 0x9481, 0x5440,
                0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40,
                0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841,
                0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81, 0x4A40,
                0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41,
                0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0, 0x4680, 0x8641,
                0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040
            };

    public static int doCRC16(byte[] array, int off, int len)
    {
        int crc = 0xFFFF;

        for (int i = off; i < (off + len); i++) {
            crc = (crc >>> 8) ^ CRC16_TAB[(crc ^ array[i]) & 0xff];
        }

        return crc;

    }

}
