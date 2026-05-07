/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hilos_y_control;

import Recursos.Proteccion;
import Recursos.Zonas;

/**
 *
 * @author rutos
 */
public class Demogorgon extends Thread {

    private String id;
    private Zonas zonas;
    //guarda la zona actual para que el cambio sea más directo, en vez de averiguar
    private int zonaActual;
    
    // de aquí salen los Ids de nuevos demogorgons. Empieza en 1
    private static int generadorIds = 0;
    private static int totalDemogorgons = 0;

    public Demogorgon(int num, Zonas zonas) {
        this.id = String.format("D%04d", num);
        this.zonas = zonas;
        this.zonaActual = -1; //aún no ha ido a ninguna zona cuando se crea
    }

    
    public static synchronized void registrarDemogorgonInicial() {
        totalDemogorgons++;
        // Asegura que generadorIds esté siempre por encima del último usado
        generadorIds = Math.max(generadorIds, totalDemogorgons);
    }

    @Override
    public void run() {
        while (true) {
            try {
                //comprobar pausa del boton
                zonas.comprobarPausa();
                //comprobar evento Eleven al principio (ponerlo entre medias puede causar corrupción de datos)
                zonas.comprobarParalisis();
                
                //elige moverse (si puede)
                if (!zonas.siApagonActivo()) {
                    int nuevaZona;
                    if (zonas.redActiva()) {
                        //hay red mental, eligen la más poblada
                        nuevaZona = zonas.getZonaMasPoblada();
                    } else {
                        //comportamiento normal
                        nuevaZona = randomEntre(0, 4);
                        //seguro para que cambie de zona sí o sí
                        //por estadística, el random podría ser la misma zona en la que estaba
                        while (nuevaZona == zonaActual && zonaActual != -1) {
                            nuevaZona = randomEntre(0, 4);
                        }
                    }
                    
                    // cambia de zona, y establece nueva actual
                    //solo cambia si es necesario (si hay red mental y ya estaba en la más poblada, no tiene que cambiar)
                    if (nuevaZona != zonaActual) {
                        zonas.cambioMidZonas(id, zonaActual, nuevaZona);
                        zonaActual = nuevaZona;
                    }
                }
                //comprobar pausa del boton (varias veces)
                zonas.comprobarPausa();
                //si acaba de nacer y hay apagon, no tiene zona a la que ir, no puede cazar
                if (zonaActual != -1) {
                    String presa = zonas.iniciarAtaque(zonaActual, id);

                    if (presa == null) {
                        sleepConPausa(randomEntre(4000, 5000)); // sin presa
                        //comprobar pausa del boton (varias veces)
                        zonas.comprobarPausa();
                    } else {
                        //hay presa
                        int caza = randomEntre(500, 1500);
                        if (zonas.tormentaActiva()) caza /= 2;
                        sleepConPausa(caza);
                        
                        //comprobar pausa del boton (varias veces)
                        zonas.comprobarPausa();
                        if (zonas.finalizarAtaque(id, presa, zonaActual)) {
                            //Traslado a colmena
                            sleepConPausa(randomEntre(500, 1000)); 
                            //comprobar pausa del boton (varias veces)
                            zonas.comprobarPausa();
                            //contador global
                            int capturasGlobales = zonas.registrarCapturaFinalizada(id);
                            //multiplo de 8 caputras, nuevo demogorgon
                            if (capturasGlobales % 8 == 0) {
                                invocarNuevoDemogorgon();
                            }
                        }
                    }        
                }
            } catch (InterruptedException e) {
                System.err.println("Demogorgon " + id + " interrumpido.");
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    //sincronizado por si pudieran repetirse IDs o superarse el límite
    private void invocarNuevoDemogorgon() {
        int nuevoNum = -1;
        synchronized (Demogorgon.class) {
            
            nuevoNum = generadorIds++;
            totalDemogorgons++;
        }
        Demogorgon nuevoDemo = new Demogorgon(nuevoNum, zonas);
        nuevoDemo.start(); // se lanza el hilo desde aquí
        Proteccion.registrarEvento("VECNA crea nuevo Demogorgon: " + nuevoDemo.id + " (total: " + totalDemogorgons + ")");
    }

    private int randomEntre(int min, int max) {
        return min + (int)(Math.random() * (max - min));
    }
    private void sleepConPausa(int ms) throws InterruptedException {
        int restante = ms;
        while (restante > 0) {
            zonas.comprobarPausa();
            int trozo = Math.min(100, restante);
            sleep(trozo);
            restante -= trozo;
        }
    }
}
