/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogl_prueba;

import com.jogamp.opengl.GL2;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;
import com.jogamp.opengl.GLAutoDrawable;

/**
 *
 * @author ####
 */
public class Pared implements Dibujar, Colliders{
    
    
    private float[] posicion;
    
    private float[] puntoTL;
    private float[] puntoTR;
    private float[] puntoBL;
    private float[] puntoBR;

    public Pared(float[] posicion, float[] puntoTL, float[] puntoTR, float[] puntoBL, float[] puntoBR) {
        this.posicion = posicion;
        this.puntoTL = puntoTL;
        this.puntoTR = puntoTR;
        this.puntoBL = puntoBL;
        this.puntoBR = puntoBR;
    }

    
    @Override
    public void dibujar(GLAutoDrawable drawable) {
        
        final GL2 gl = drawable.getGL().getGL2();
        
        gl.glTranslatef(posicion[0],posicion[1],posicion[2]);    
        
        gl.glColor3f(0.0f,0.6f,0.4f);
                
        gl.glBegin(GL_QUADS);                      // Draw A Quad
        gl.glVertex3f(puntoTL[0], puntoTL[1], puntoTL[2]);              // Top Left
        gl.glVertex3f(puntoTR[0], puntoTR[1], puntoTR[2]);            // Top Right
        gl.glVertex3f(puntoBR[0], puntoBR[1], puntoBR[2]);          // Bottom Right
        gl.glVertex3f(puntoBL[0], puntoBL[1], puntoBL[2]);          // Bottom Left   
        
        

        gl.glEnd(); 
    }
    
    public Vector getLimiteTop(){
        
        float[][] puntos = new float[2][2]; //(x,y) (x,y) se pasan los dos puntos absolutos que conforman la recta
        
        puntos[0][0] = posicion[0] + puntoTL[0]; //punto x absoluto inicio recta
        puntos[0][1] = posicion[1] + puntoTL[1]; //punto y absoluto inicio recta
        
        puntos[1][0] = posicion[0] + puntoTR[0]; //punto x absoluto fin recta
        puntos[1][1] = posicion[1] + puntoTR[1];//punto y absoluto fin recta
                
        return new Vector(puntos);
    }
    
    public Vector getLimiteBottom(){
        
        float[][] puntos = new float[2][2]; //(x,y) (x,y) se pasan los dos puntos absolutos que conforman la recta
        
        puntos[0][0] = posicion[0] + puntoBL[0]; //punto x absoluto inicio recta
        puntos[0][1] = posicion[1] + puntoBL[1]; //punto y absoluto inicio recta
        
        puntos[1][0] = posicion[0] + puntoBR[0]; //punto x absoluto fin recta
        puntos[1][1] = posicion[1] + puntoBR[1];//punto y absoluto fin recta
                
        return new Vector(puntos);
    }
    
    public Vector getLimiteLeft(){
        
        float[][] puntos = new float[2][2]; //(x,y) (x,y) se pasan los dos puntos absolutos que conforman la recta
        
        puntos[0][0] = posicion[0] + puntoTL[0]; //punto x absoluto inicio recta
        puntos[0][1] = posicion[1] + puntoTL[1]; //punto y absoluto inicio recta
        
        puntos[1][0] = posicion[0] + puntoBL[0]; //punto x absoluto fin recta
        puntos[1][1] = posicion[1] + puntoBL[1];//punto y absoluto fin recta
                
        return new Vector(puntos);
    }
    
    public Vector getLimiteRight(){
        
        float[][] puntos = new float[2][2]; //(x,y) (x,y) se pasan los dos puntos absolutos que conforman la recta
        
        puntos[0][0] = posicion[0] + puntoTR[0]; //punto x absoluto inicio recta
        puntos[0][1] = posicion[1] + puntoTR[1]; //punto y absoluto inicio recta
        
        puntos[1][0] = posicion[0] + puntoBR[0]; //punto x absoluto fin recta
        puntos[1][1] = posicion[1] + puntoBR[1];//punto y absoluto fin recta
                
        return new Vector(puntos);
    }
    
}
