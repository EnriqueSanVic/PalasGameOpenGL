/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PalasGame;

/**
 *
 * @author ####
 */
public class FuncionesCalculo {
    
    public static boolean randomRebote(){
       int random = (int) (Math.random() * (100 - 0));
       if(random > LogicaEscena.REBOTESPORCENTAJE){
           return true;
       }
       return false;
   }
   
   public static float randomNumber(float min,float max){
       return (float) ((float) min + Math.random() * (max - min));
   }
   
}
