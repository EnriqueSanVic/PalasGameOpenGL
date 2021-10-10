/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogl_prueba;

import com.jogamp.opengl.GL2;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;
import com.jogamp.opengl.GLAutoDrawable;
import java.util.ArrayList;

/**
 *
 * @author ####
 */
public class Bola implements Dibujar, Colliders{

    
    private float velocidadX, velocidadY;
     
    private float[] posicion;
    
    
    private float[] puntoTL;
    private float[] puntoTR;
    private float[] puntoBL;
    private float[] puntoBR;
    
    private Vector topTL;
    private Vector topTR;
    
    private Vector bottomBL;
    private Vector bottomBR;   
    
    private Vector leftTL;
    private Vector leftBL;
    
    private Vector rightTR;
    private Vector rightBR;
    
    private ArrayList<Colliders> objetosColisionables;
    
    private int contadorGolpes;
    
    public void reinicioGolpes(){
        contadorGolpes = 0;
    }

    public int getContadorGolpes() {
        return contadorGolpes;
    }
   
    
    public Bola(ArrayList<Colliders> objetosColisionables, float velocidadX, float velocidadY, float[] posicion, float[] puntoTL, float[] puntoTR, float[] puntoBL, float[] puntoBR) {
        this.objetosColisionables = objetosColisionables;
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.posicion = posicion;
        this.puntoTL = puntoTL;
        this.puntoTR = puntoTR;
        this.puntoBL = puntoBL;
        this.puntoBR = puntoBR;
        
    }
    
    public void actualizar(){
        
        
        /*Comprobamos las colisiones controlando el estado de la ejecucion 
        con el booleano isCol para que si una colision ha sucedido que no
        se comprueben las demásn*/
        
        boolean isCol = false;
        
        if(!isCol)isCol=comprobarColisionesLeft(); 
        
        if(!isCol)isCol=comprobarColisionesRight();
        
        if(!isCol)isCol=comprobarColisionesTop();
        
        if(!isCol)comprobarColisionesBottom();
        
        /*Actualizamos el estado de la posicion*/
        posicion[0] = posicion[0] + velocidadX;
        posicion[1] = posicion[1] + velocidadY;
        
        /*Evaluamos los periodos de incremento de la velocidad de la bola segun los golpes transcurridos
        si los golpes actuales son multiple de 3 entonces se incrementa la velocidad de la bola
        Como esto se ejectua muchas veces en el transcurso de los renderizados de un golpe 3N al 3N+1
        se percibirá como que en el tiempo de juego del golpe 3N al 3N+1 la bola acelera poco a poco.
        */
        
        if(contadorGolpes%3==0 && contadorGolpes>0){
            aumentarVelocidad();
        }
        

    }
    
    
   
    
    
    private boolean comprobarColisionesTop() {

        boolean isCol = false;

        /*Creo los vectores top que salen de los puntos TL y TR*/
        float[][] puntosVecTopTL = {{puntoTL[0] + posicion[0], puntoTL[1] + posicion[1]},
        {puntoTL[0] + posicion[0], puntoTL[1] + posicion[1] + velocidadY}};

        /* Solo se suma el componente una componente de velocidad en el segundo punto 
        del vector para objetener el vector de colision normal a la cara del posigono que se esta evaluando*/
        topTL = new Vector(puntosVecTopTL);

        float[][] puntosVecTopTR = {{puntoTR[0] + posicion[0], puntoTR[1] + posicion[1]},
        {puntoTR[0] + posicion[0], puntoTR[1] + posicion[1] + velocidadY}};

        topTR = new Vector(puntosVecTopTR);

        /*Comprobamos si alguno de los dos vectores de la cara TOP colisiona 
        con alguno de los vectores Bottom de los objetos colisionables*/
        for (int i = 0; i < objetosColisionables.size() && !isCol; i++) {

            /*Comprovamos la colision con cada vector superficie de la superficie opuesta de cada objeto colisionable
              Esto quiere decir que si estamos evaluando como en este caso las colisiones de la superficie top de la bola
              tendremos que determinar si los vectores normales de deteccion sufren interseccion con el vector de la superficie 
              opuesta en este caso la bottom del resto de objetos
             */
            if (topTL.isColision(objetosColisionables.get(i).getLimiteBottom())
                    || topTR.isColision(objetosColisionables.get(i).getLimiteBottom())) {
                /*COLISION TOP*/

                /*Caso de arrastre bottom con una pala
                if (objetosColisionables.get(i).getClass() == Pala.class) {
                    ((Pala)objetosColisionables.get(i)).movimientoArrastreBottom(this);
                }
                */
                
                isCol = true;
                colisionHorizontal();
            }

        }

        return isCol;

    }

    private boolean comprobarColisionesBottom() {

        boolean isCol = false;

        float[][] puntosVecBottomBL = {{puntoBL[0] + posicion[0], puntoBL[1] + posicion[1]},
        {puntoBL[0] + posicion[0], puntoBL[1] + posicion[1] + velocidadY}};

        /* Solo se suma el componente una componente de velocidad en el segundo punto 
        del vector para objetener el vector de colision normal a la cara del posigono que se esta evaluando*/
        bottomBL = new Vector(puntosVecBottomBL);

        float[][] puntosVecBottomBR = {{puntoBR[0] + posicion[0], puntoBR[1] + posicion[1]},
        {puntoBR[0] + posicion[0], puntoBR[1] + posicion[1] + velocidadY}};

        bottomBR = new Vector(puntosVecBottomBR);

        for (int i = 0; i < objetosColisionables.size() && !isCol; i++) {

            if (bottomBL.isColision(objetosColisionables.get(i).getLimiteTop())
                    || bottomBR.isColision(objetosColisionables.get(i).getLimiteTop())) {
                /*COLISION BOTTOM*/
                
                /*Caso de arrastre bottom con una pala
                if (objetosColisionables.get(i).getClass() == Pala.class) {
                    ((Pala)objetosColisionables.get(i)).movimientoArrastreTop(this);
                }
                */
                isCol = true;

                colisionHorizontal();
            }

        }

        return isCol;

    }

    
    
    private boolean comprobarColisionesLeft() {

        boolean isCol = false;

        float[][] puntosVecLeftLT = {{puntoTL[0] + posicion[0], puntoTL[1] + posicion[1]},
        {puntoTL[0] + posicion[0] + velocidadX, puntoTL[1] + posicion[1]}};

        /* Solo se suma el componente una componente de velocidad en el segundo punto 
        del vector para objetener el vector de colision normal a la cara del posigono que se esta evaluando*/
        leftTL = new Vector(puntosVecLeftLT);

        float[][] puntosVecLeftLB = {{puntoBL[0] + posicion[0], puntoBL[1] + posicion[1]},
        {puntoBL[0] + posicion[0] + velocidadX, puntoBL[1] + posicion[1]}};

        leftBL = new Vector(puntosVecLeftLB);

        for (int i = 0; i < objetosColisionables.size() && !isCol; i++) {

            if (leftTL.isColision(objetosColisionables.get(i).getLimiteRight())
                    || leftBL.isColision(objetosColisionables.get(i).getLimiteRight())) {
                /*COLISION BOTTOM*/
                isCol = true;

                colisionVertical();
            }

        }

        return isCol;

    }
    
    

    private boolean comprobarColisionesRight() {

        boolean isCol = false;

        float[][] puntosVecRightRT = {{puntoTR[0] + posicion[0], puntoTR[1] + posicion[1]},
        {puntoTR[0] + posicion[0] + velocidadX, puntoTR[1] + posicion[1]}};

        /* Solo se suma el componente una componente de velocidad en el segundo punto 
        del vector para objetener el vector de colision normal a la cara del posigono que se esta evaluando*/
        rightTR = new Vector(puntosVecRightRT);

        float[][] puntosVecRightRB = {{puntoBR[0] + posicion[0], puntoBR[1] + posicion[1]},
        {puntoBR[0] + posicion[0] + velocidadX, puntoBR[1] + posicion[1]}};

        rightBR = new Vector(puntosVecRightRB);

        for (int i = 0; i < objetosColisionables.size() && !isCol; i++) {

            if (rightTR.isColision(objetosColisionables.get(i).getLimiteLeft())
                    || rightBR.isColision(objetosColisionables.get(i).getLimiteLeft())) {
                /*COLISION BOTTOM*/
                isCol = true;
                
                colisionVertical();
            }

        }

        return isCol;

    }
    
    public void aumentarVelocidad(){
        

        final float FACTORAUMENTO = 0.00007f;
        
        if(velocidadX > 0){
            velocidadX+=FACTORAUMENTO;
        }else{
            velocidadX-=FACTORAUMENTO;
        }
        
         if(velocidadY > 0){
            velocidadY+=FACTORAUMENTO;
        }else{
            velocidadY-=FACTORAUMENTO;
        }
        
        
    }

    public float getVelocidadX() {
        return velocidadX;
    }

    public void setVelocidadX(float velocidadX) {
        this.velocidadX = velocidadX;
    }

    public float getVelocidadY() {
        return velocidadY;
    }

    public void setVelocidadY(float velocidadY) {
        this.velocidadY = velocidadY;
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
    
    
    
    public float[] getPosicion(){
        return posicion;
    }


    public void arrastreTop(float vel){
        //posicion[1]+=vel;
        velocidadY+=vel;
    }
    
    public void arrastreBottom(float vel){
        //posicion[1]-=vel;
        velocidadY-=vel;
    }
    
    public float[] vectorVelocidad(){
        
        float[] vec = new float[3];
        
        vec[0] = posicion[0] + velocidadX;
        vec[1] = posicion[1] + velocidadY;
        vec[2] = 0f;
        
        return vec;

    }
    
    
    @Override
    public void dibujar(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        
        gl.glTranslatef(posicion[0],posicion[1],posicion[2]);    
        
        gl.glColor3f(0.6f,0.8f,0.8f);
        
        gl.glBegin(GL_QUADS);                      // Draw A Quad
        gl.glVertex3f(puntoTL[0], puntoTL[1], puntoTL[2]);              // Top Left
        gl.glVertex3f(puntoTR[0], puntoTR[1], puntoTR[2]);            // Top Right
        gl.glVertex3f(puntoBR[0], puntoBR[1], puntoBR[2]);          // Bottom Right
        gl.glVertex3f(puntoBL[0], puntoBL[1], puntoBL[2]);          // Bottom Left   

        gl.glEnd(); 
    }
    
    private void colisionVertical() {
        System.out.println("Vertical");
        
        LogicaEscena.sonido();
                                            
        velocidadX = -(velocidadX); /*Se cambia de sentido la velocidad en X*/

        if (FuncionesCalculo.randomRebote()) {//si hay un rebote random se calcula la desviación aleatoria y se asigna a la velocidadX
            float temp = velocidadY * LogicaEscena.DESVIACIONREBOTE;
            velocidadY = velocidadY + FuncionesCalculo.randomNumber(-temp, temp);
        }
        
        contadorGolpes++;
    }
   
    private void colisionHorizontal() {
        System.out.println("Horizontal");
        
        LogicaEscena.sonido();
        
        velocidadY = -(velocidadY);/*Se cambia de sentido la velocidad en Y*/

        if (FuncionesCalculo.randomRebote()) {//si hay un rebote random se calcula la desviación aleatoria y se asigna a la velocidadX
            float temp = velocidadX * LogicaEscena.DESVIACIONREBOTE;
            velocidadX = velocidadX + FuncionesCalculo.randomNumber(-temp, temp);
        }
        
        contadorGolpes++;
    }
    
}
