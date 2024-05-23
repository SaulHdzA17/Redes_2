package domain;

import java.net.*;
import java.io.*;

public class Servidor {
    public static void main(String args[]){
        try{
            DatagramSocket s = new DatagramSocket(2000);//Creamos un socket de datagrama en el puerto 2000
            System.out.println("Servidor iniciado, esperando cliente");
            for(;;){//Bucle infinito para la escucha del servidor
                DatagramPacket p = new DatagramPacket(new byte[2000],2000);//Preparamos un buffer de 2000 bytes para recibir el datagrama
                s.receive(p);//Esperamos la recepción de un datagrama
                System.out.println("Datagrama recibido desde"+p.getAddress()+":"+p.getPort());
                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(p.getData()));//Convertimos los datos del datagrama en un flujo de datos de entrada
                int x = dis.readInt();//Leemos un entero del flujo de datos
                float f = dis.readFloat();//Leemos un flotante del flujo de datos
                long z = dis.readLong();//Leemos un entero largo del flujo de datos
                System.out.println("\n\nEntero:"+ x + " Flotante:"+f+" Entero largo:"+z);// Imprime los valores leídos en la consola
            }//for
        //s.close()
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main
}
