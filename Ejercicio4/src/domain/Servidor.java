package domain;

import java.net.*;//Importamos la librería para poder trabajar con sockets 
import java.io.*;//Importamos la librería para pdoer trabajar con entradas y salidas de datos

public class Servidor {
    public static void main(String[] args){
        try{
            DatagramSocket s = new DatagramSocket(3000);//Creamos un socket de datagrama con el puerto 3000
            System.out.println("Servidor iniciado, esperando cliente");//Indicamos que el servidor se inicia y está esoerando clientes
            
            // Bucle infinito para recibir mensajes continuamente
            while (true) {
                // Recibir mensaje del cliente
                DatagramPacket p = new DatagramPacket(new byte[3000],3000);//Creamos un paquete para recibir los datos del cliente
                s.receive(p);//Recibimo los paquetes
                System.out.println("Datagrama recibido desde "+p.getAddress()+":"+p.getPort());//Imprime la dirección IP y el puerto del cliente desde 
                //donde se recibió el datagrama
                String msj = new String(p.getData(),0,p.getLength());//Convertimos los datos recibidos en una cadena de texto
                System.out.println("Con el mensaje: "+ msj);//Imprimimos la cedana de texto "msj"
                
                // Leer mensaje del servidor para enviar al cliente
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));//Crea un objeto BufferedReader para leer la entrada del usuario 
                //desde la consola
                System.out.print("Escriba un mensaje para el cliente: ");
                String mensaje = br.readLine();//Leemos el mensaje ingresado
                
                // Enviar mensaje al cliente
                byte[] b = mensaje.getBytes();//Convertimos el mensaje en un array de bytes
                DatagramPacket respuesta = new DatagramPacket(b, b.length, p.getAddress(), p.getPort());//Crea un paquete DatagramPacket con el mensaje 
                //y la dirección IP y puerto del cliente
                s.send(respuesta);//Enviamos el paquete al cliente
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
