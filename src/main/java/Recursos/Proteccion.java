/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Recursos;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author rutos
 */
public class Proteccion {

    private static final String RUTA_ARCHIVO = "hawkins.txt";
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    
    public static synchronized void registrarEvento(String mensaje) {

        String temporal=LocalDateTime.now().format(FORMATO);
        
        // Bloque try evita fugas de memoria o bloqueos del sistema operativo.
        try (PrintWriter out = new PrintWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            System.out.println(mensaje);
            out.println("["+temporal + "] "+ mensaje);
        } catch (IOException e) {
            System.err.println("Error crítico escribiendo en el log: " + e.getMessage());
        }
    }
}
