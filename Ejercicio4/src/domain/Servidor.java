package domain;

import java.net.*;
import java.io.*;

public class Servidor {
    public static void main(String[] args){
        try{
            DatagramSocket s = new DatagramSocket(3000);
            System.out.println("Servidor iniciado, esperando cliente");
            
            // Bucle infinito para recibir mensajes continuamente
            while (true) {
                // Recibir mensaje del cliente
                DatagramPacket p = new DatagramPacket(new byte[3000],3000);
                s.receive(p);
                System.out.println("Datagrama recibido desde "+p.getAddress()+":"+p.getPort());
                String msj = new String(p.getData(),0,p.getLength());
                System.out.println("Con el mensaje: "+ msj);
                
                // Leer mensaje del servidor para enviar al cliente
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Escriba un mensaje para el cliente: ");
                String mensaje = br.readLine();
                
                // Enviar mensaje al cliente
                byte[] b = mensaje.getBytes();
                DatagramPacket respuesta = new DatagramPacket(b, b.length, p.getAddress(), p.getPort());
                s.send(respuesta);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
