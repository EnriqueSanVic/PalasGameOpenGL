
package PalasGame;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import java.util.ArrayList;



public class LogicaEscena{
    
    private ArrayList<Dibujar> colaDibujado = new ArrayList<Dibujar>();
    
    protected static final int REBOTESPORCENTAJE = 40; /*Porcentaje de rebotes normales*/
    protected static final float DESVIACIONREBOTE = 0.6f; /*30% de desviacion es moderado*/
   
    
    private final float MARGENCOLISION = 0.2f;
    
    
    private float PuntoVictoriaLeft = -4.0f;
    private float PuntoVictoriaRight= 4.0f;
    
    private int marcador[] = new int[2];
   
    private Pala jugador;
    private Pala maquina;
    private Bola bola;
    private Pared paTop,paBottom;
        
    private float minVelBola = -0.02f, maxVelBola = 0.02f;
    
    boolean isWPressed=false;
    boolean isSPressed=false;
    
    protected static void sonido(){
        new Sonido("./kick_sound.aiff").start();
    }

    public LogicaEscena() {

    }
    
    private void comprobarAccionesJugador(){
        if(isWPressed){
            moverTop();
        }else if(isSPressed){
            moverBottom();
        }else{
            jugador.pararMovimiento();
        }
    }

    private void actualizarMaquina() {
        

        float limTop, limBottom;
        final float apertura = 0.30f;
        
        
        limTop = maquina.getPosicion()[1] + maquina.getPosicion()[1] * apertura;
        limBottom = maquina.getPosicion()[1] - maquina.getPosicion()[1] * apertura;
        
        if(bola.getPosicion()[1] <= limTop && bola.getPosicion()[1] >= limBottom){
            maquina.pararMovimiento();
        }else if (bola.getPosicion()[1] > maquina.getPosicion()[1] && maquina.getLimiteTop().getPuntos()[0][1] < paTop.getLimiteBottom().getPuntos()[0][1]) {
            maquina.moverTop();
        } else if (bola.getPosicion()[1] < maquina.getPosicion()[1] && maquina.getLimiteBottom().getPuntos()[0][1] > paBottom.getLimiteTop().getPuntos()[0][1]) {
            maquina.moverBottom();
        }
        
        maquina.actualizar();

    }


 

   public void reInit(){ /*Reinicio de partida*/
       System.out.println("\nJugador: " + marcador[0] + "\nMaquina: " + marcador[1]);
       colaDibujado.clear();/*Se vacía la cola de dibujado para poder volver a crear los objetos*/
       init();
   }
   
   private void comprobarVictoria(){
       
       //se usan los puntos 0 0 de los limites por que son las coorenada x de los limites izquierdo y derecho
       if(bola.getLimiteLeft().getPuntos()[0][0] <= PuntoVictoriaLeft){
           marcador[1]++;//se añade un punto al marcador de la maquina
           reInit();
       }else if(bola.getLimiteRight().getPuntos()[0][0] >= PuntoVictoriaRight){
           marcador[0]++;//se añade un punto al marcador de la maquina
           reInit();
       }

   }
           

    
    public void update(){ /*Actualiza toda la logica a cada frame*/
        
        jugador.actualizar();
        bola.actualizar();
        actualizarMaquina();
        comprobarVictoria();
        comprobarAccionesJugador();
    }
    
    
    
    public void render(GLAutoDrawable drawable){
        
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);     // Clear The Screen And The Depth Buffer
                              // Reset The View

        for(Dibujar i:colaDibujado){//Se recorre toda la cola de dibujado, dibujando los objetos en pantalla en el orden en el que están
            
            gl.glLoadIdentity(); /*sirve para reestablecer las coordenadas del cursor de dibujado y dibujar en un marco de referencia absoluto*/
            
            i.dibujar(drawable);
            
            gl.glFlush();
        }

        
    }

    
    
    //INICIO DE LA ESCENA
    
    void init() {
        
        /*Se crea el objeto pala del jugador y se añade*/
        float[] posicion, TL, TR, BL, BR;   
        ArrayList<Colliders> objetosColisionables = new ArrayList<Colliders>();
        
        posicion = new float[3];

        posicion[0] = 2.8f;
        posicion[1] = 0.0f;
        posicion[2] = -6.0f;
        
        
        TL = new float[3];
        
        TL[0] = -0.1f;
        TL[1] = 0.4f;
        TL[2] = 0.0f;
        
        TR = new float[3];
        
        TR[0] = 0.1f;
        TR[1] = 0.4f;
        TR[2] = 0.0f;
        
        BL = new float[3];
        
        BL[0] = -0.1f;
        BL[1] = -0.4f;
        BL[2] = 0.0f;
        
        BR = new float[3];
        
        BR[0] = 0.1f;
        BR[1] = -0.4f;
        BR[2] = 0.0f;
          
        maquina = new Pala(0.3f,posicion.clone(),TL.clone(),TR.clone(),BL.clone(),BR.clone());
        
        colaDibujado.add(maquina);
        objetosColisionables.add(maquina);
        
        posicion = new float[3];

        posicion[0] = -2.8f;
        posicion[1] = 0.0f;
        posicion[2] = -6.0f;
        
        
        TL = new float[3];
        
        TL[0] = -0.1f;
        TL[1] = 0.4f;
        TL[2] = 0.0f;
        
        TR = new float[3];
        
        TR[0] = 0.1f;
        TR[1] = 0.4f;
        TR[2] = 0.0f;
        
        BL = new float[3];
        
        BL[0] = -0.1f;
        BL[1] = -0.4f;
        BL[2] = 0.0f;
        
        BR = new float[3];
        
        BR[0] = 0.1f;
        BR[1] = -0.4f;
        BR[2] = 0.0f;
        
        jugador = new Pala(0.3f,posicion.clone(),TL.clone(),TR.clone(),BL.clone(),BR.clone());
        
        colaDibujado.add(jugador);
        objetosColisionables.add(jugador);
        
        
        /*PARED SUPERIOR*/
        
        posicion = new float[3];

        posicion[0] = 0.0f;
        posicion[1] = 3.0f;
        posicion[2] = -6.0f;
        
        
        TL = new float[3];
        
        TL[0] = -4f;
        TL[1] = 2f;
        TL[2] = 0.0f;
        
        TR = new float[3];
        
        TR[0] = 4f;
        TR[1] = 2f;
        TR[2] = 0.0f;
        
        BL = new float[3];
        
        BL[0] = -4f;
        BL[1] = -1f;
        BL[2] = 0.0f;
        
        BR = new float[3];
        
        BR[0] = 4f;
        BR[1] = -1f;
        BR[2] = 0.0f;
        
        paTop = new Pared(posicion.clone(),TL.clone(),TR.clone(),BL.clone(),BR.clone());

        colaDibujado.add(paTop);
        objetosColisionables.add(paTop);
        
        /*PARED SUPERIOR*/
        
        posicion = new float[3];

        posicion[0] = 0.0f;
        posicion[1] = -3.0f;
        posicion[2] = -6.0f;
        
        
        TL = new float[3];
        
        TL[0] = -4f;
        TL[1] = 1f;
        TL[2] = 0.0f;
        
        TR = new float[3];
        
        TR[0] = 4f;
        TR[1] = 1f;
        TR[2] = 0.0f;
        
        BL = new float[3];
        
        BL[0] = -4f;
        BL[1] = -1f;
        BL[2] = 0.0f;
        
        BR = new float[3];
        
        BR[0] = 4f;
        BR[1] = -1f;
        BR[2] = 0.0f;
        
        paBottom = new Pared(posicion.clone(),TL.clone(),TR.clone(),BL.clone(),BR.clone());

        colaDibujado.add(paBottom);
        objetosColisionables.add(paBottom);
        /*BOLA*/
        posicion = new float[3];

        posicion[0] = 0.0f;
        posicion[1] = 0.0f;
        posicion[2] = -6.0f;
        
        
        TL = new float[3];
        
        TL[0] = -0.1f;
        TL[1] = 0.1f;
        TL[2] = 0.0f;
        
        TR = new float[3];
        
        TR[0] = 0.1f;
        TR[1] = 0.1f;
        TR[2] = 0.0f;
        
        BL = new float[3];
        
        BL[0] = -0.1f;
        BL[1] = -0.1f;
        BL[2] = 0.0f;
        
        BR = new float[3];
        
        BR[0] = 0.1f;
        BR[1] = -0.1f;
        BR[2] = 0.0f;
          
        
        /*Velocidad inicial Random para la bola*/
        
        double randomVelX = FuncionesCalculo.randomNumber(minVelBola,maxVelBola),
               randomVelY = FuncionesCalculo.randomNumber(minVelBola,maxVelBola);
        
        if(randomVelX==0.00f){
            randomVelX+=0.1;
        }
        
        if(randomVelY==0.00f){
            randomVelY+=0.1;
        }
        

        bola = new Bola(objetosColisionables, (float)randomVelX, (float)randomVelY, posicion.clone(), TL.clone(), TR.clone(), BL.clone(), BR.clone());
        
        colaDibujado.add(bola);
        

        /*Pasamos objetos necesarios para las palas*/  
        jugador.setCollisionables(objetosColisionables); 
        jugador.setParedes(paTop, paBottom);
        
        maquina.setCollisionables(objetosColisionables); 
        maquina.setParedes(paTop, paBottom);
        
        bola.reinicioGolpes(); //reniniciamos el contador de golpes de la bola
        
    }  
        
    
    /*Controlan los botones del usuario*/
    public void moverTop() {
        
        jugador.moverTop();
        
    }
    
    public void pararMovimiento(){
        jugador.pararMovimiento();
    }
    
    public void moverBottom() {
        
      
        jugador.moverBottom();
      
    }
    

}



 /*Apuntes interesantes

*/
    
                   