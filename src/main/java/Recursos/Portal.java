/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Recursos;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


/**
 *
 * @author rutos
 */
public class Portal {
   
    private CyclicBarrier entrarPortal;
    private boolean ocupado=false;
    private int esperandoRegreso=0;
    private boolean apagon=false;
    
    public Portal(int c){
        this.entrarPortal=new CyclicBarrier(c);
        
    }
    public void cruzarAZona() throws InterruptedException, BrokenBarrierException {
    
        entrarPortal.await(); 

        // bloques synchronized separados para no acaparar la función en el sleep
        synchronized(this) {
          
            while (ocupado || esperandoRegreso > 0|| apagon) {
                wait();
            }
            ocupado = true;
        }

        
        Thread.sleep(1000);

       
        synchronized(this) {
            ocupado = false;
            notifyAll(); 
        }
    }
    public void cruzarABase() throws InterruptedException {
        synchronized(this) {
            esperandoRegreso++; 

            while (ocupado||apagon) {
                wait();
            }
            esperandoRegreso--;
            ocupado = true;
        }

        Thread.sleep(1000); 

        synchronized(this) {
            ocupado = false;
            notifyAll();
        }
    }
    
    //funciones de eventos (para los niños que cruzan portales)
    public synchronized void setApagon(boolean estado) {
        this.apagon = estado;
        if (!estado) {
            notifyAll(); //despierta a los niños
        }
    }
}

