/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



/**
 *
 * @author rutos
 */
package Recursos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Portal {
   
    private CyclicBarrier entrarPortal;
    private boolean ocupado = false;
    private int esperandoRegreso = 0;
    private boolean apagon = false;
    
    //variables de rastreo para la interfaz
    private List<String> esperandoIda = new ArrayList<>();
    private List<String> esperandoVuelta = new ArrayList<>();
    private String ninoCruzando = ""; // Solo cruza uno a la vez
    
    public Portal(int c) {
        this.entrarPortal = new CyclicBarrier(c);
    }
    
    // AHORA RECIBE EL ID DEL NIÑO
    public void cruzarAZona(String idNino) throws InterruptedException, BrokenBarrierException {
        //Llega al portal y espera a formar grupo
        synchronized(this) {
            esperandoIda.add(idNino);
        }
        
        entrarPortal.await(); 

        synchronized(this) {
            while (ocupado || esperandoRegreso > 0 || apagon) {
                wait();
            }
            ocupado = true;
            //Empieza a cruzar: sale de la cola de espera y entra al túnel
            esperandoIda.remove(idNino);
            ninoCruzando = idNino;
        }

        Thread.sleep(1000); // Tiempo cruzando

        synchronized(this) {
            //Termina de cruzar
            ninoCruzando = "";
            ocupado = false;
            notifyAll(); 
        }
    }
    
    // Recibe el id del niño para apuntarlo
    public void cruzarABase(String idNino) throws InterruptedException {
        synchronized(this) {
            //Llega al portal desde el Upside Down
            esperandoVuelta.add(idNino);
            esperandoRegreso++; 

            while (ocupado || apagon) {
                wait();
            }
            esperandoRegreso--;
            ocupado = true;
            //Empieza a cruzar
            esperandoVuelta.remove(idNino);
            ninoCruzando = idNino;
        }

        Thread.sleep(1000); 

        synchronized(this) {
            //Termina de cruzar
            ninoCruzando = "";
            ocupado = false;
            notifyAll();
        }
    }
    
    public synchronized void setApagon(boolean estado) {
        this.apagon = estado;
        if (!estado) {
            notifyAll();
        }
    }
    
    // getters para la interfaz
    public synchronized String getIdaTexto() {
        return String.join(", ", esperandoIda);
    }

    public synchronized String getVueltaTexto() {
        return String.join(", ", esperandoVuelta);
    }

    public synchronized String getNinoCruzando() {
        return ninoCruzando;
    }
    
    public synchronized int getConteoTotalPortal() {
        int total = esperandoIda.size() + esperandoVuelta.size();
        if (ninoCruzando != null && !ninoCruzando.isEmpty()) {
            total++;
        }
        return total;
    }
}

