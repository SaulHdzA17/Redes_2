package domain;

import java.net.*;
import java.io.*;

public class Cliente {
    public static void main(String[] args){
        try{
            DatagramSocket cl = new DatagramSocket();
            System.out.println("Cliente iniciado, escriba un mensaje de saludo:");

            // Bucle infinito para enviar y recibir mensajes continuamente
            while (true) {
                // Leer mensaje del usuario
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String mensaje = br.readLine();
                
                // Enviar mensaje al servidor
                byte[] b = mensaje.getBytes();
                String dst = "127.0.0.1";
                int pto = 3000;
                DatagramPacket p = new DatagramPacket(b, b.length, InetAddress.getByName(dst), pto);
                cl.send(p);
                
                // Recibir respuesta del servidor
                DatagramPacket respuesta = new DatagramPacket(new byte[3000], 3000);
                cl.receive(respuesta);
                String msj = new String(respuesta.getData(), 0, respuesta.getLength());
                System.out.println("Respuesta del servidor: " + msj);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
