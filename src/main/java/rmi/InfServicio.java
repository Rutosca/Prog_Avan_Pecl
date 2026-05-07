/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rmi;

/**
 *
 * @author rutos
 */


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface InfServicio extends Remote {
    int getTotalNinosHawkins() throws RemoteException;
    String getEstadoPortales() throws RemoteException;
    String getEstadoUpsideDown() throws RemoteException;
    Map<String, Integer> getRankingDemogorgons() throws RemoteException;
    String getEventoActivo() throws RemoteException;
    void pausarReanudarPrograma() throws RemoteException;
}
