/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rmi;

/**
 *
 * @author rutos
 */


import Recursos.Portal;
import Recursos.Zonas;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class Servicio extends UnicastRemoteObject implements InfServicio {

    private Zonas zonas;
    private Portal[] portales;

    public Servicio(Zonas zonas, Portal[] portales) throws RemoteException {
        super();
        this.zonas = zonas;
        this.portales = portales;
    }

    @Override
    public int getTotalNinosHawkins() throws RemoteException {
        // Sumas el tamaño de las listas Calle, Sotano y Radio de tu clase Zonas
        return zonas.getCallePrincipalTexto().split(", ").length + 
               zonas.getSotanoByersTexto().split(", ").length + 
               zonas.getRadioWSQKTexto().split(", ").length; 
    }

    @Override
    public String getEstadoPortales() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append("Portal ").append(i+1).append(": ")
              .append(portales[i].getIdaTexto()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getEstadoUpsideDown() throws RemoteException {
        return "Colmena: " + zonas.getNinosEnColmena() + " capturados";
    }

    @Override
    public Map<String, Integer> getRankingDemogorgons() throws RemoteException {
        return zonas.getRankingDemogorgons();
    }

    @Override
    public String getEventoActivo() throws RemoteException {
        if (zonas.siApagonActivo()) return "Apagón del Laboratorio";
        if (zonas.tormentaActiva()) return "Tormenta Upside Down";
        if (zonas.redActiva()) return "Red Mental";
        return "Sin evento activo";
    }

    @Override
    public void pausarReanudarPrograma() throws RemoteException {
        zonas.botonPausa();
    }
}
