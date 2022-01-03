/*
    crear sistema de colisiones independiente al de la bola para poner limites a las paredes y las colisiones de los laterales con la bola
 */
package PalasGame;

import com.jogamp.opengl.GL2;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;
import com.jogamp.opengl.GLAutoDrawable;
import java.util.ArrayList;


public class Pala implements Movimiento, Dibujar, Colliders{
    
    private float[] posicion;
    
    private float[] puntoTL;
    private float[] puntoTR;
    private float[] puntoBL;
    private float[] puntoBR;
   
    private ArrayList<Colliders> objetosColisionables;
    
    private float velocidadY;

    private float VelocidadYMax;
    
    public Pala(float VelocidadYMax, float[] posicion, float[] puntoTL, float[] puntoTR, float[] puntoBL, float[] puntoBR) {
        this.posicion = posicion;
        this.puntoTL = puntoTL;
        this.puntoTR = puntoTR;
        this.puntoBL = puntoBL;
        this.puntoBR = puntoBR;
        this.VelocidadYMax = VelocidadYMax;
        this.velocidadY = 0;
        
        
    }
    
    private Pared paTop;
    private Pared paBottom;
    
    public void setParedes(Pared paTop, Pared paBottom){
        this.paTop = paTop;
        this.paBottom = paBottom;
    }
    
    public void setCollisionables(ArrayList<Colliders> objetosColisionables){
        this.objetosColisionables = objetosColisionables;
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
   
  
    
    private float FACTORV = 0.2f;
    
    
    
    public float velocidad(){
        return FACTORV * velocidadY;
    }
   
    
    public void actualizar(){
        
        if(     

            (
            velocidadY>0f
            &&
            getLimiteTop().getPuntos()[0][1] > paTop.getLimiteBottom().getPuntos()[0][1]
            )   
        ||
            (
            velocidadY<0f
            &&
            getLimiteBottom().getPuntos()[0][1] <  paBottom.getLimiteTop().getPuntos()[0][1]
            )  
          )pararMovimiento();
        
         posicion[1]+=FACTORV * velocidadY;
           
        
    }
    
    @Override
    public void moverTop() {
        
        velocidadY = VelocidadYMax;
    }
    
    public void pararMovimiento(){
        velocidadY = 0f;
    }
    
    @Override
    public void moverBottom() {
        velocidadY = -(VelocidadYMax);
    }
    
    

    public void movimientoArrastreTop(Bola bola){
        bola.arrastreTop(FACTORV * velocidadY);
    }
    
    public void movimientoArrastreBottom(Bola bola){
        bola.arrastreBottom(FACTORV * velocidadY);
    }
    
    
    @Override
    public void dibujar(GLAutoDrawable drawable) {
        
        final GL2 gl = drawable.getGL().getGL2();
        
        gl.glTranslatef(posicion[0],posicion[1],posicion[2]);    
        
        gl.glColor3f(1.0f, 1.0f, 0.9f);
        
        gl.glBegin(GL_QUADS);                      // Draw A Quad
        gl.glVertex3f(puntoTL[0], puntoTL[1], puntoTL[2]);              // Top Left
        gl.glVertex3f(puntoTR[0], puntoTR[1], puntoTR[2]);            // Top Right
        gl.glVertex3f(puntoBR[0], puntoBR[1], puntoBR[2]);          // Bottom Right
        gl.glVertex3f(puntoBL[0], puntoBL[1], puntoBL[2]);          // Bottom Left   

        gl.glEnd();  
        
    }
    
   
    

    public float[] getPosicion() {
        return posicion;
    }

    public float[] getPuntoTL() {
        return puntoTL;
    }

    public float[] getPuntoTR() {
        return puntoTR;
    }

    public float[] getPuntoBL() {
        return puntoBL;
    }

    public float[] getPuntoBR() {
        return puntoBR;
    }

    public float getVelocidadY() {
        return velocidadY;
    }

   

    
    
    
    
    
    
}
