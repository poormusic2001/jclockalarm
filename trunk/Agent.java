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


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.decoder.JavaLayerException;
import jclock.ClockFrame;
import jclock.EditPage;
import jclock.ticle;

/**
 *
 * @author poormusic
 */
public class Agent implements Runnable{
    TableModel tcm = ClockFrame.jTable1.getModel();
    public int CloseMusic=1;     
    public int currentIndex =0;
    //0 means no music is selected.1 means a music is selected.
    public int MusicPathStatus =0;
     
    
    private void checkDataAndTime(String tipTime) throws IOException, JavaLayerException{
        
        
        String[] tipTimeString = tipTime.split("：");
        String hour = tipTimeString[0];
        String minute =tipTimeString[1];
        if(ticle.Hour.length() == 1)
            ticle.Hour='0'+ticle.Hour;
        if(ticle.Minute.length() ==1)
            ticle.Minute='0'+ticle.Minute;
        
       
        if(hour.equals(ticle.Hour)){
            if(minute.equals(ticle.Minute)){
                
                
                if(tcm.getValueAt(currentIndex, 2)!=null){
                   showAlarmMusic();
                }
                String Tips = String.valueOf(tcm.getValueAt(currentIndex, 1));                
                showTipMessage(Tips);
                setStatus();
            }
        }
    }
    private void showTipMessage(String message){
               
        CloseMusic=JOptionPane.showConfirmDialog(new javax.swing.JFrame(), message, "提醒", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if(CloseMusic ==0 && MusicPathStatus ==1)
                EditPage.mPlayer.stop();
    }
    private void showAlarmMusic()throws IOException, JavaLayerException{
           Thread tAgent = new Thread(this);    
           tAgent.start();
    }
    private void setStatus(){
         Object status = tcm.getValueAt(currentIndex, 3);
         
         if(status == null)
             tcm.setValueAt("已提醒", currentIndex, 3);
         
    }
    
    public void readData()throws IOException, JavaLayerException{
        
        for(int i=0;i< EditPage.jTableContentRow;i++){
            currentIndex =i;
            Object status = tcm.getValueAt(i, 3);
        
            if(status == null){
                
                String tipTime = String.valueOf(tcm.getValueAt(i, 0));
                
                if(!tipTime.equals("null"))
                    checkDataAndTime(tipTime);
            }
        }
    }
    public void run(){
        FileInputStream fis = null;
        try {
            String MusicPath = String.valueOf(tcm.getValueAt(currentIndex, 2));
          
            if(MusicPath.length() != 0){
                pbListener listener = new pbListener();
                fis = new FileInputStream(MusicPath); 
                InputStream is = (InputStream) fis;
                EditPage.mPlayer = new AdvancedPlayer(is);
                EditPage.mPlayer.setPlayBackListener(listener);
                listener.playbackStarted(EditPage.mPlayer);
                is.close();
                fis.close();
            }else{
                MusicPathStatus=0;
            }
        } catch (JavaLayerException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
