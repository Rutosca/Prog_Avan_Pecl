/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hilos_y_control;

import Recursos.Portal;
import Recursos.Zonas;
import java.util.concurrent.BrokenBarrierException;


/**
 *
 * @author rutos
 */
public class Nino extends Thread {

    private String id;
    private Zonas zonas;
    private Portal[] portales;//se crea un array de tipo Portal para que cada uno sea independiente de los otros
    //por ejemplo, el portal Bosque no interfiere con el portal Alcantarillado

    public Nino(int num, Zonas zonas, Portal[] portales) {
        this.id = String.format("N%04d", num);
        this.zonas = zonas;
        this.portales = portales;
    }

    @Override
    public void run(){
         while (true){
            try {
                //comprobar pausa del boton
                zonas.comprobarPausa();
                //va al sótano
                zonas.moverASotanoByers(id);
                sleep(randomEntre(1000, 2000));
                //elige portal
                int destino = randomEntre(0, 4);
                portales[destino].cruzarAZona(id);//(permiso)
                //entra al portal (ha pasado el permiso)
                zonas.cambiarBaseZona(id, destino);
                
                int explorar = randomEntre(3000, 5000);
                if (zonas.tormentaActiva()) {
                    explorar *= 2;
                }
                sleep(explorar);
                //verifica que un demogorgon no lo haya capturado
                boolean libre = zonas.salirDeZona(id);
                //si fue capturado, la función se encarga de llevarlo, no es necesario que lo haga el niño
                
                if (!libre) {
                    // Capturado: espera en la colmena hasta que Eleven lo rescate
                    zonas.verificarEstadoCaptura(id);
                    // Eleven ya lo añadió a callePrincipal, solo deambula
                    sleep(randomEntre(3000, 5000));
                    continue;
                }

                
                //vuelve a radio
                portales[destino].cruzarABase(id);//(permiso)
                zonas.cambiarZonaBase(id);//(pase)
                zonas.depositarSangre(id);//deja sangre
                sleep(randomEntre(2000, 4000));
                zonas.moverACalle(id);//dentro del if para que solo los que NO han sido capturados lo ejecuten
                sleep(randomEntre(3000, 5000)); // Deambular por la calle
                

            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private int randomEntre(int min, int max) {
        return min + (int)(Math.random() * (max - min));
    }
}

