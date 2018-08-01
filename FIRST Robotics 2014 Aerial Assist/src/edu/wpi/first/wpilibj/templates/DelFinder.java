/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author Borna
 */
public class DelFinder
{

    String m_del;
    byte m_del_byte[];
    byte m_test[];
    int m_size;

    DelFinder(String del)
    {
        m_del = del;
        try
        {
            m_del_byte = m_del.getBytes("US-ASCII");

        } catch (UnsupportedEncodingException ex)
        {
            ex.printStackTrace();
        }
        m_test = new byte[m_del_byte.length];
        m_size = m_del_byte.length;
    }

    public boolean check(byte input)
    {
        for (int i = 0; i < m_size-1; i++)
        {
            m_test[i] = m_test[i+1];
        }
        m_test[m_size - 1] = input;

        boolean eq = true;

        for (int i = 0; i < m_size; i++)
        {
            if(m_test[i] != m_del_byte[i])
            {
                eq = false;
                break;
            }
        }
        return eq;
    }

    public void clear()
    {
        for (int i = 0; i < m_size; i++)
        {
            m_test[i] = (byte) (m_del_byte[i] + 10);
        }
    }

}
