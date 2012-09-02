/*
 *   Copyright (C) 2012 Yi Han Tsai (poormusic2001@gmail.com)
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, write to the Free Software Foundation.
 *
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jclock;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;

/**
 *
 * @author home
 */
public class ticle implements Runnable{
    public Thread ticleThread;
    public static String Hour;
    public static String Minute;
    private Agent dataAgent = new Agent();
   public void run(){
        while(true){
            try{
                ticleThread.sleep(1000);                
                Calendar c = Calendar.getInstance();
                Hour =Integer.toString(c.get(Calendar.HOUR_OF_DAY));
                Minute = Integer.toString(c.get(Calendar.MINUTE));
                //Agent is to check the tip time and invoke the music and confirmbox.
                try {
                    dataAgent.readData();
                } catch (IOException ex) {
                    Logger.getLogger(ticle.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JavaLayerException ex) {
                    Logger.getLogger(ticle.class.getName()).log(Level.SEVERE, null, ex);
                }
        
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            
        }
    }
   
    public void startToTicle(){
        ticleThread = new Thread(this);
        ticleThread.start();
    }


}
