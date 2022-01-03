
package PalasGame;


public class Vector{
    
    private float[][] puntos;
    
    public Vector(float[][] puntos) {
        this.puntos = puntos;
    }

    public float[][] getPuntos() {
        return puntos;
    }
    

    public boolean isColision(Vector recta){ /*Este metodo le pregunta a nuestro vector si choca con otro*/
        
        /*
        System.out.println("\n\n");
        
        System.out.println("Vector Bola: "
                + "\n\tP1x:" + puntos[0][0]
                + "\n\tP1y:" + puntos[0][1]
                + "\n\tP2x:" + puntos[1][0]
                + "\n\tP2y:" + puntos[1][1]
        
        );
        */
        //primero calculamos la recta del objeto vector propio

        float[] recta1 = eqContinua(puntos[0][0], puntos[0][1], puntos[1][0], puntos[1][1]);

        //System.out.println("M1: " + recta1[0] + " N1: " + recta1[1]);
        //System.out.println("m = " + recta1 [0] + "\nn = " + recta1 [1]);
        
        /*
        System.out.println("Vector Pala: "
                + "\n\tP1x:" + recta.puntos[0][0]
                + "\n\tP1y:" + recta.puntos[0][1]
                + "\n\tP2x:" + recta.puntos[1][0]
                + "\n\tP2y:" + recta.puntos[1][1]
        );
        */
        
        //calculamos la recta del vector que se pasa por parametros
        float[] recta2 = eqContinua(recta.puntos[0][0], recta.puntos[0][1], recta.puntos[1][0], recta.puntos[1][1]);

        //System.out.println("M2: " + recta2[0] + " N2: " + recta2[1]);

        float corteX, corteY;

        if (Float.isInfinite(recta1[0]) && !Float.isInfinite(recta2[0])) {

            corteX = puntos[0][0];
            /*si la recta de el bector obejto actual 
            y la del otro vector tiene pendiente es vertical 
            podremos asegurar que el punto de corte esta en X = componenteX 
            de cualquira de los dos puntos que conforman la recta vertical
             */
            corteY = equacion(recta2, corteX);
            
        } else if (!Float.isInfinite(recta1[0]) && Float.isInfinite(recta2[0])) {

            corteX = recta.puntos[0][0];
            corteY = equacion(recta1, corteX);
            
        } else {
            corteX = puntoDeCorte(recta1, recta2);
            corteY = equacion(recta1, corteX); //vale cualquiera de las dos rectas para calcular el core en Y
        }

        

        //System.out.println("CorteX: " + corteX + " CorteY: " + corteY);

        if ( //si tanto el punto de corteX como el punto de corteY estan en los limites de ambos vectores
                isInLimits(corteX, corteY, puntos[0][0], puntos[0][1], puntos[1][0], puntos[1][1])
                && isInLimits(corteX, corteY, recta.puntos[0][0], recta.puntos[0][1], recta.puntos[1][0], recta.puntos[1][1])) {
            return true;
        } else {
            return false;
        }
        
    }
    
    
    private float[] eqContinua(float x1, float y1, float x2, float y2){ // mx+n = y
        
        float[] m_n = new float[2];//se retornaran las componentes m y n
        
        m_n[0] = (y2 - y1)/(x2-x1); //calculamos m
        
        m_n[1] = - ((((y2-y1)*x1) - ((x2-x1)*y1)) / (x2-x1));
        
        return m_n;
    }
    
    private float puntoDeCorte(float[] compA, float[] compB){
        
        return -(compA[1] - compB[1]) / (compA[0] - compB[0]);
        
    }
    
    private float equacion(float[] componentes, float x){
        return componentes[0]*x + componentes[1];
    }
    
    private boolean isInLimits(float corteX, float corteY, float Ax, float Ay,  float Bx, float By){
        
        float mayorX, menorX, mayorY, menorY;
            
            if(Ax >= Bx){
                mayorX = Ax;
                menorX = Bx;
            }else{
                mayorX = Bx;
                menorX = Ax;
            }
            
            if(Ay >= By){
                mayorY = Ay;
                menorY = By;
            }else{
                mayorY = By;
                menorY = Ay;
            }
            
            if(
                corteX <= mayorX && corteX >= menorX
                &&
                corteY <= mayorY && corteY >= menorY
            ){
                return true;
            }
            
            return false;
    
    }

    
    
}
