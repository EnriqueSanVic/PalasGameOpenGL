/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PalasGame;


import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;



/**
 *
 * @author ####
 */
public class Sonido extends Thread{
    
   
    private AudioInputStream audio;

    private long duracion;
    
    private Clip reproductor;

    private String id;
    
    public Sonido(String path) {
        
        this.id = path;
        
        this.setPriority(Thread.MAX_PRIORITY);
        
        try {
            
            audio = AudioSystem.getAudioInputStream(new File(path));
            duracion = audio.getFrameLength()/2;
   
                
        }catch(Exception ex){

        }
        
    }

    

    @Override
    public void run() {
        
        try {
            
            reproductor = AudioSystem.getClip();
            
            reproductor.open(audio);
            
            reproductor.start();
            
            Thread.sleep(duracion);
            
            reproductor.stop();
            
            reproductor.flush();
            
            reproductor.close();
            
            audio.close();
            
            
        } catch (Exception ex) {
            //si se mata el hilo va a saltar esta excepcion por el sleep para la reproducción
        }
        
        reproductor = null;
        audio= null;

        
    }


    @Override
    public String toString() {
        return "ReproductorUnaVez " + id;
    }
            

}
  


