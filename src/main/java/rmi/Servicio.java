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
        return zonas.getConteoHawkinsTotal();
    }

    @Override
    public int[] getConteosPortales() throws RemoteException {
        return new int[] {
            portales[0].getConteoTotalPortal(),
            portales[1].getConteoTotalPortal(),
            portales[2].getConteoTotalPortal(),
            portales[3].getConteoTotalPortal()
        };
    }

    @Override
    public int[] getConteosNinosUpsideDown() throws RemoteException {
        return new int[] {
            zonas.getConteoNinosZona(0), zonas.getConteoNinosZona(1),
            zonas.getConteoNinosZona(2), zonas.getConteoNinosZona(3)
        };
    }

    @Override
    public int[] getConteosDemosUpsideDown() throws RemoteException {
        return new int[] {
            zonas.getConteoDemosZona(0), zonas.getConteoDemosZona(1),
            zonas.getConteoDemosZona(2), zonas.getConteoDemosZona(3)
        };
    }
    @Override
    public Map<String, Integer> getRankingDemogorgons() throws RemoteException {
        // Borra la línea del throw new UnsupportedOperationException
        return zonas.getRankingDemogorgons(); 
    }

    @Override
    public int getNinosColmena() throws RemoteException {
        return zonas.getNinosEnColmena();
    }

    @Override
    public String getNombreEvento() throws RemoteException {
        return zonas.getNombreEvento();
    }

    @Override
    public int getTiempoRestanteEvento() throws RemoteException {
        return zonas.getSegundosRestantesEvento();
    }

    @Override
    public void pausarReanudarPrograma() throws RemoteException {
        zonas.botonPausa();
    }

    
}
