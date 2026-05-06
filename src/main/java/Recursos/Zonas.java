/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Recursos;


import Recursos.Proteccion;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rutos
 */
public class Zonas {

    // Zonas de Hawkins
    private List<String> callePrincipal = new ArrayList<>();
    private List<String> sotanoByers = new ArrayList<>();
    private List<String> radioWSQK = new ArrayList<>();

    // Zonas del Upside Down manejadas con lista de listas
    
    private List<List<String>> listaZNinos;
    private List<List<String>> listaZDemos;
    private List<String> colmena = new ArrayList<>();
    //para llevar conteo de capturas por demogorgon, lo mejor es un map
    private Map<String, Integer> capturasPorDemo;
    private int capturasGlobales=0;
    
    private int sangreRecolectada = 0;
    //para evitar condiciones de carrera, un List para "retener" a niños que hayan sido marcados como objetivo de ataque
    List<String> bajoAtaque=new ArrayList<>();
    
    private final String[] nombreZonas = {"Bosque", "Laboratorio", "Centro Comercial", "Alcantarillado"};
    
    private boolean apagon=false;
    private boolean tormenta = false;
    private boolean eleven = false;
    private boolean redMental = false;
    
    public Zonas() {
        listaZNinos = new ArrayList<>();
        listaZDemos = new ArrayList<>();
        
        capturasPorDemo = new HashMap<>();
        
        for (int i = 0; i < 4; i++) {
            listaZNinos.add(new ArrayList<>());
            listaZDemos.add(new ArrayList<>());
        }
    }
    
    public synchronized void moverASotanoByers(String idNino) {
        //Buscar en las dos listas sin necesidad de if
        //donde esté, se quita
        callePrincipal.remove(idNino);
        radioWSQK.remove(idNino);
        
        sotanoByers.add(idNino);
        
        Proteccion.registrarEvento("El nino "+idNino+" se ha movido hacia el sotano Byers");
    }
    //método simple para el inicio del programa: que los niños empiecen en la calle
    public synchronized void añadirACalle(String idNino) {
        callePrincipal.add(idNino);
    }
    public synchronized void moverACalle(String idNino) {
        radioWSQK.remove(idNino);
        
        callePrincipal.add(idNino);
        
        Proteccion.registrarEvento("El nino "+idNino+" se ha movido hacia la calle principal a deambular");
    }

    public synchronized void depositarSangre(String idNino) {
       
        sangreRecolectada++;
        Proteccion.registrarEvento("El nino "+idNino+ " ha depositado sangre. Total: "+ sangreRecolectada);
    }
    
    public synchronized void cambiarBaseZona(String idNino,int zonaElegida){
        sotanoByers.remove(idNino);
        listaZNinos.get(zonaElegida).add(idNino);
        Proteccion.registrarEvento("El nino "+idNino+ " ha cruzado el portal hacia el "+nombreZonas[zonaElegida]);
    }           
    public synchronized void cambiarZonaBase(String idNino){
        for (int i = 0; i < 4; i++) {
            listaZNinos.get(i).remove(idNino);
            
        }
        radioWSQK.add(idNino);
        Proteccion.registrarEvento("El nino "+idNino+ " ha cruzado el portal de vuelta y va a Radio WSQK");
    }
    
    public synchronized void cambioMidZonas(String idDemo, int zonaOrigen, int zonaDestino) {
        if (zonaOrigen != -1) {
            listaZDemos.get(zonaOrigen).remove(idDemo);
        }
        listaZDemos.get(zonaDestino).add(idDemo);
    
        Proteccion.registrarEvento("El Demogorgon " + idDemo + " se ha movido a " + nombreZonas[zonaDestino]);
    }
    //el ataque se hace en 2 operaciones atómicas
    public synchronized String iniciarAtaque(int dZona,String idDemo) {
        List<String> ninos = listaZNinos.get(dZona);
        // Solo elegir niños que no hayan sido marcados
        List<String> disponibles = new ArrayList<>();
        for (String n : ninos) {
            if (!bajoAtaque.contains(n)) disponibles.add(n);
        }
        if (disponibles.isEmpty()) return null;

        String idNino = disponibles.get((int)(Math.random() * disponibles.size()));
        bajoAtaque.add(idNino); // lo marca
        Proteccion.registrarEvento("Demogorgon " + idDemo + " inicia ataque... Objetivo: "+idNino);
        return idNino;
    }
    
    public synchronized boolean finalizarAtaque(String idDemo, String idNino, int dZona) {
        bajoAtaque.remove(idNino); //primero quita (siempre)

        boolean capturado = (Math.random() < 0.333);
        if (capturado) {
            listaZNinos.get(dZona).remove(idNino);
            colmena.add(idNino);
            Proteccion.registrarEvento("Demogorgon " + idDemo + " captura a " + idNino);
            
        } else {
            Proteccion.registrarEvento("Nino " + idNino + " resiste el ataque de " + idDemo);
        }
        notifyAll(); // despierta al niño que estaba esperando
        return capturado;
    }
    
    // El niño llama esto cuando termina de explorar
    public synchronized boolean salirDeZona(String idNino) throws InterruptedException {
        // Espera si está bajo ataque
        while (bajoAtaque.contains(idNino)) {
            wait();
        }
        // comprueba si lo capturaron
        if (colmena.contains(idNino)) {
            return false; //capturado
        }
        // No capturado. se quita de la zona para que lo marquen mientras se mueve
        for (List<String> zona : listaZNinos) {
            zona.remove(idNino);
        }
        return true;
    }
    public synchronized void verificarEstadoCaptura(String idNino) throws InterruptedException {

        while (colmena.contains(idNino)) {
            wait(); 
        }

    }
    
    public synchronized int registrarCapturaFinalizada(String idDemo) {
        
        int capturasActuales = capturasPorDemo.getOrDefault(idDemo, 0) + 1;
        capturasPorDemo.put(idDemo, capturasActuales);
        
        capturasGlobales++;
        
        Proteccion.registrarEvento("El demogorgon " + idDemo + 
        " lleva nino a la colmena. Propias: " + capturasActuales + 
        " | Globales: " + capturasGlobales); // para debug
    
        
        return capturasGlobales; 
    }
    
    public synchronized Map<String, Integer> getRankingDemogorgons() {
        return new HashMap<>(capturasPorDemo); 
    }
    
    //funciones de eventos------------.-----------
    
    //cambiar el estado apagon (como un método setter: se usa this)
    public synchronized void setApagon(boolean estado) {
        this.apagon = estado;
        if (!estado) {
            notifyAll();
        }
    }
    //devolver estado apagon (método getter)
    public synchronized boolean siApagonActivo() {
        return apagon;
    }
    
    //cambiar tormenta
    public synchronized void setTormenta(boolean estado) {
        this.tormenta = estado;
    }
    //devolver tormenta
    public synchronized boolean tormentaActiva() {
        return tormenta;
    }
    
    //método para detener demogorgons
    public synchronized void comprobarParalisis() throws InterruptedException {
        while (eleven) {
            wait();//bloquea demogorgons
        }
    }
    //cambiar Eleven
    public synchronized void setEleven(boolean estado) {
        this.eleven = estado;
        if (!estado) {
            notifyAll(); //despierta demogorgons
        }
    }
    // rescate niños
    public synchronized void rescateEleven() {
        // cálculo
        int cantidadARescatar = Math.min(colmena.size(), sangreRecolectada);

        for (int i = 0; i < cantidadARescatar; i++) {
            // se saca el primero (cada vez que avanza se borra y se mete a CallePrincipal)
            String idNino = colmena.remove(0); 
            callePrincipal.add(idNino);//añade al niño liberado a la calle
            Proteccion.registrarEvento("Eleven ha liberado al niño " + idNino + " directo a la Calle Principal");
        }

        // despertar de la colmena
        notifyAll(); 
    }
    
    //cambiar red mental
    public synchronized void setRedMental(boolean estado) {
        this.redMental = estado;
    }
    //comprobar red mental
    public synchronized boolean redActiva() {
        return redMental;
    }
    //buscar la zona con más niños
    public synchronized int getZonaMasPoblada() {
        int maxNinos = -1;
        int zonaDestino = 0;

        //cálculo de nº de niños en zonas
        for (int i = 0; i < 4; i++) {
            int cantidadActual = listaZNinos.get(i).size();
            if (cantidadActual > maxNinos) {
                maxNinos = cantidadActual;
                zonaDestino = i;
            }
        }
        return zonaDestino;
    }
    
    //getters para la interfaz
    //extrae los datos sin interferir en la concurrencia

    public synchronized String getCallePrincipalTexto() {
        return String.join(", ", callePrincipal);
    }

    public synchronized String getSotanoByersTexto() {
        return String.join(", ", sotanoByers);
    }

    public synchronized String getRadioWSQKTexto() {
        return String.join(", ", radioWSQK);
    }

    public synchronized int getSangreDisponible() {
        return sangreRecolectada;
    }

    public synchronized int getNinosEnColmena() {
        return colmena.size();
    }

    public synchronized String getNinosEnZonaUpsideDown(int zona) {
        return String.join(", ", listaZNinos.get(zona));
    }

    public synchronized String getDemosEnZonaUpsideDown(int zona) {
        return String.join(", ", listaZDemos.get(zona));
    }
}
