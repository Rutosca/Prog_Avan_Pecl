/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pl_pr_az;

import Recursos.Portal;
import Recursos.Zonas;
import hilos_y_control.Demogorgon;
import hilos_y_control.Eventos;
import hilos_y_control.Nino;

/**
 *
 * @author rutos
 */
public class PL_PR_AZ {

    public static void main(String[] args) {
        Zonas zonanio=new Zonas();
        Portal[] nexo=new Portal[4];//se crean los protales, pero vacíos
        //hay que crear cada uno individual, para que cada uno reciba su índice de cyclicbarrier
        nexo[0]=new Portal(2);
        nexo[1]=new Portal(3);
        nexo[2]=new Portal(4);
        nexo[3]=new Portal(2);
        //demogorgon alpha
        Demogorgon alpha = new Demogorgon(0, zonanio);
        Demogorgon.registrarDemogorgonInicial(); // registra el alpha en el contador global
        alpha.start();
        new Eventos(zonanio,nexo).start();
        for(int i=1;i<=1500;i++){
            Nino n = new Nino(i, zonanio, nexo);
            zonanio.añadirACalle("N" + String.format("%04d", i));
            n.start();
            try {//pausa para no saturar
                Thread.sleep((long)(Math.random() * 20 + 10));
            } catch (InterruptedException e) {
                System.err.println("Error en el arranque");
            }
        }
    }
}
