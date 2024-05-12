package domain;

import java.net.*;//Importamos la librería para poder trabajar con sockets 
import java.io.*;//Importamos la librería para pdoer trabajar con entradas y salidas de datos

public class Cliente {
    public static void main(String[] args){
        try{
            DatagramSocket cl = new DatagramSocket();//Creamos un objeto de sockets de datagrama para el cliente
            System.out.println("Cliente iniciado, escriba un mensaje de saludo:");

            // Bucle infinito para enviar y recibir mensajes continuamente
            while (true) {
                // Leer mensaje del usuario
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));//Leemos la entrada del usuario en consola
                String mensaje = br.readLine();//Guardamos lo que introdujo el usuario en un string llamado "mensaje"
                
                // Enviar mensaje al servidor
                byte[] b = mensaje.getBytes();//Convertimos el mensaje en un array de bytes 
                String dst = "127.0.0.1";//Definimos la IP del servidor
                int pto = 3000;//Definimos el número de puerto del servidor
                DatagramPacket p = new DatagramPacket(b, b.length, InetAddress.getByName(dst), pto);//Creamos un paquete DatagramPacket con el mensaje, 
                cl.send(p);//la dirección IP y el puerto del servidor y lo enviamos al servidor 
                
                // Recibir respuesta del servidor
                DatagramPacket respuesta = new DatagramPacket(new byte[3000], 3000);//Creamos un paquete para recibir la respuesta del servidor
                cl.receive(respuesta);//Recibimos la respuesta del servidor
                String msj = new String(respuesta.getData(), 0, respuesta.getLength());//Convertimos los datos recibidos en una cadena de texto
                System.out.println("Respuesta del servidor: " + msj);//Imprimimos esa cadena de texto "msj"
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
