/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hilos_y_control;

import Recursos.Portal;
import Recursos.Proteccion;
import Recursos.Zonas;

/**
 *
 * @author rutos
 */
public class Eventos extends Thread {
    private Zonas zonas;
    private Portal[] portales;

    public Eventos(Zonas zonas, Portal[] portales) {
        this.zonas = zonas;
        this.portales = portales;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //comprobar pausa del boton
                zonas.comprobarPausa();
                
                sleep(randomEntre(30000, 60000));

                int tipoEvento = randomEntre(0, 4);

                switch (tipoEvento) {
                    case 0:
                        ejecutarApagon();
                        break;
                    case 1:
                        ejecutarTormenta();
                        
                        break;
                    case 2:
                        ejecutarEleven();
                        break;
                    case 3:
                        ejecutarRedMental();
                        break;
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void ejecutarApagon() throws InterruptedException {
        Proteccion.registrarEvento("Evento: Apagón del laboratorio");

        //hay apagon
        //true para zonas (afecta a Demos)
        zonas.setApagon(true);

        //true para portales (afecta a niños)
        for (int i = 0; i < 4; i++) {
            portales[i].setApagon(true);
        }
        sleep(randomEntre(5000, 10000)); 

        //termina. se pone a false
        zonas.setApagon(false);
        for (int i = 0; i < 4; i++) {
            portales[i].setApagon(false);
        }

        Proteccion.registrarEvento("Fin del evento: apagón del laboratorio");
    }
    private void ejecutarTormenta() throws InterruptedException {
        Proteccion.registrarEvento("Evento: tormenta del UpsideDown");
        //hay tormenta
        zonas.setTormenta(true);
        sleep(randomEntre(5000, 10000)); 
        //termina. se pone a false
        zonas.setTormenta(false);
        
        Proteccion.registrarEvento("Fin del evento: tormenta del UpsideDown");
    }
    
    private void ejecutarEleven() throws InterruptedException {
        Proteccion.registrarEvento("Evento: intervención de Eleven");

        //parálisis
        zonas.setEleven(true);
        //liberación niños
        zonas.rescateEleven();
        //duracion
        sleep(randomEntre(5000, 10000)); 
        // fin efecto en demogorgons
        zonas.setEleven(false);

        Proteccion.registrarEvento("Fin del evento: intervención de Eleven");
    }
    private void ejecutarRedMental() throws InterruptedException {
        Proteccion.registrarEvento("Evento: red mental de Vecna");

        zonas.setRedMental(true);

        // Duración del evento (5 a 10 segundos)
        sleep(randomEntre(5000, 10000)); 

        zonas.setRedMental(false);

        Proteccion.registrarEvento("Fin del evento: red mental de Vecna");
    }
    private int randomEntre(int min, int max) {
        return min + (int)(Math.random() * (max - min));
    }
}