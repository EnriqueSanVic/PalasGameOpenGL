/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogl_prueba;


import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;



/**
 *
 * @author ####
 */
public class Sonido extends Thread{
    
    private File aud;

    private Clip clip;
    
    private AudioInputStream audioInputStream ;
    
    public Sonido(String ruta) {
        super();
        aud = new File(ruta);
        
        
    }
    

    @Override
    public void run(){
        
    }
    
    public void reproducir() {
        
          if (aud.exists()) {
            // create AudioInputStream object
            try {
                
                
                audioInputStream = AudioSystem.getAudioInputStream(aud);

                // create clip reference
                clip = AudioSystem.getClip();

                // open audioInputStream to the clip
                clip.open(audioInputStream);

                clip.start();
                
                clip.close();
                
                
            } catch (Exception ex) {
                System.out.println("Error de audio");
            }
        }

    }
            

}
  


