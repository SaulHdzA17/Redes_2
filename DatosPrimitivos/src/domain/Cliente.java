package domain;

import java.net.*;
import java.io.*;

public class Cliente {
    public static void main(String args []){
        try{
            int pto = 2000;//Definimos el puerto de destino
            InetAddress dst = InetAddress.getByName("127.0.0.1");//Definimos la dirección IP de destino (localhost)
            DatagramSocket cl = new DatagramSocket();//Creamos un socket de datagrama
            ByteArrayOutputStream baos = new ByteArrayOutputStream();//Preparamos un flujo de bytes de salida
            DataOutputStream dos = new DataOutputStream(baos);//Asociamos el flujo de bytes de salida con un flujo de datos
            dos.writeInt(4);//Escribimos un entero en el flujo de datos
            dos.flush();
            dos.writeFloat(4.1f);//Escribimos un flotante en el flujo de datos
            dos.flush();
            dos.writeLong(72);//Escribimos un entero largo en el flujo de datos
            dos.flush();
            byte[] b = baos.toByteArray();//Convertimos el flujo de bytes en un array de bytes
            DatagramPacket p = new DatagramPacket(b,b.length,dst,pto);//Creamos un datagrama con los datos, la longitud, la dirección y el puerto de destino
            cl.send(p);//Enviamos el datagrama a través del socket
            cl.close();//Cerramos el socket
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main
}
