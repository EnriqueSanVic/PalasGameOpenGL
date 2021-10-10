
package jogl_prueba;




import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * Apuntes
 */
public class Render implements GLEventListener{

    private static GraphicsEnvironment graphicsEnviorment;
    private static boolean isFullScreen = false;
    public static DisplayMode dm,dm_old;
    private static Dimension xgraphic;
    private static GL2 gl;
    private static Point point = new Point(0,0);
    
    private final static int WIDTH = 800;
    private final static int HEIGHT = 600;
    private final static int MAX_FPS = 60;
    
    private static LogicaEscena logicaEscena;
 
    private GLU glu = new GLU();
    
    
    @Override
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2();
        logicaEscena = new LogicaEscena();
        logicaEscena.init();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        

    }

    
    
    @Override
    public void display(GLAutoDrawable drawable) {
        
        update(); /*Primero se actualiza la logica del dibujado y luego se dibuja con el render*/
        render(drawable);
    }
    
    private void update() {/*Todo se maneja desde la case LogicaEscena*/
        logicaEscena.update();
    }
    
     private void render(GLAutoDrawable drawable) {
        logicaEscena.render(drawable);
    }
    
   
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        gl = drawable.getGL().getGL2();
        
        if(height<=0)height=1;
        
        final float h = (float) width / (float) height;
        
        gl.glViewport(0, 0, width, height);
        
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f,h,1.0,20.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    
    public static void main(String[] args) {
        
        //abre la version del entorno de configuracion de OpenGL 2
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        
        //el camvas
        
        final GLCanvas glcanvas = new GLCanvas(capabilities);
        Render r = new Render();
        glcanvas.addGLEventListener(r);
        glcanvas.setSize(WIDTH, HEIGHT);
        
        
        
        //El numero del constructor es el maximo de FPS
        final FPSAnimator animator = new FPSAnimator(glcanvas, MAX_FPS, true);
        
        final JFrame frame = new JFrame("Palas");
        
        frame.getContentPane().add(glcanvas);//se crea el marco y se le añade el canvas
        
        //evento de cerrar la ventana se añade al frame
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                if(animator.isStarted()){
                    animator.stop();
                }
                //si el animator funciona lo destrulle y cierra la ejecucion del sistema
                System.exit(0);
            }
        
        });
        
        frame.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                
                if (ke.getKeyCode() == KeyEvent.VK_W) {
                    logicaEscena.isWPressed = true;
                }

                if (ke.getKeyCode() == KeyEvent.VK_S) {
                     logicaEscena.isSPressed = true;
                } 
                
                if (ke.getKeyCode() == KeyEvent.VK_F1) {
                     fullScreen(frame);
                } 
                
                if (ke.getKeyCode() == KeyEvent.VK_R) {
                     logicaEscena.reInit();
                } 
                
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_W) {
                    logicaEscena.isWPressed = false;
                }

                if (ke.getKeyCode() == KeyEvent.VK_S) {
                     logicaEscena.isSPressed = false;
                } 
            }
            
            
        });
        
        frame.setFocusable(true);
        
        frame.setSize(frame.getContentPane().getPreferredSize());
        
        /**
         * centra la pantalla cuando se inicia
         */
        
        graphicsEnviorment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        
        GraphicsDevice[] devices = graphicsEnviorment.getScreenDevices();
        
        dm_old = devices[0].getDisplayMode();
        dm = dm_old;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        //calculamos las coordenadas del medio de la pantalla para una colocacion optima del frame
        
        int windowX = Math.max(0, ((screenSize.width - frame.getWidth())/2));
        int windowY = Math.max(0, ((screenSize.height - frame.getHeight())/2));
        
        frame.setLocation(windowX,windowY); //damos la colozacion inicial de la ventana
        
        
        
        frame.setVisible(true);//hacemos visible la ventana
        
        //vamos a añadir botone de control
        
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(0,0));
        
        frame.add(p, BorderLayout.SOUTH);
        
      
        animator.start();
        
    }

    

    private static void fullScreen(JFrame f) {
        if(!isFullScreen){
            f.dispose();
            f.setUndecorated(true);
            f.setVisible(true);
            f.setResizable(false);
            xgraphic = f.getSize();
            point = f.getLocation();
            f.setLocation(0,0);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            f.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
            isFullScreen=true;
        }else{
            f.dispose();
            f.setUndecorated(false);
            f.setResizable(true);
            f.setLocation(point);
            f.setSize(xgraphic);
            f.setVisible(true);
            isFullScreen=false;
        }
    }

    
    
    
    
}
