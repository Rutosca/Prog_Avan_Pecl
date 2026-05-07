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
    int[] getConteosPortales() throws RemoteException; // Array de 4 enteros
    int[] getConteosNinosUpsideDown() throws RemoteException;//igual
    int[] getConteosDemosUpsideDown() throws RemoteException;//igual
    int getNinosColmena() throws RemoteException;
    Map<String, Integer> getRankingDemogorgons() throws RemoteException;
    String getNombreEvento() throws RemoteException;
    int getTiempoRestanteEvento() throws RemoteException;
    void pausarReanudarPrograma() throws RemoteException;
}
