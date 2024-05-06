package carrito.mx;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
     public static void main(String[] args) {
        Catalogo producto = new Catalogo();
        ArrayList <Catalogo> catalogo = new ArrayList();
        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));//Creamos un objeto BufferedReader para poder leer 
            //la entrada que nos de el usuario desde la consola, alternativa a scanner.
            System.out.println("Escriba la direccion del servidor: ");//Proporcionamos la dirección IP o el nombre del servidor, debe ser la 
            String host = br1.readLine();//dirección en donde se está ejecutando el programa del Servidor, en est caso localhost
            System.out.println("Escriba el puerto: ");//Escribimos el puerto al que deseamos ingresar desde la entrada estandar como la entrada
            int pto = Integer.parseInt(br1.readLine());//anterior, en este caso 1234 es el puerto que está escuchando el servidor
            Socket cl = new Socket(host, pto);//Creamos un objeto Socket llamado "cl" que vamos a conectar al servidor especificado por la 
            //dirección "localhost" y el puerto "1234"
            BufferedReader br2 = new BufferedReader(new InputStreamReader(cl.getInputStream()));//Creamos un BufferedReader para leer los datos 
            //que provienen del servidor a través de un flujo de entrada 
            String mensaje = br2.readLine();//Leemos el mensaje enviado por el servidor "Hola mundo desde el servidor"
            System.out.println("Recibimos un mensaje desde el servidor :D");//
            System.out.println("Mensaje: " + mensaje);//Imprimimos en la consola el mensaje enviado por el servidor
            
           //Catalogo.deserializarObjetos(catalogo);
            if (catalogo != null) {
                for (Catalogo objeto : catalogo) {
                    // Hacer algo con cada objeto deserializado
                }
            } else {
                System.out.println("Error al deserializar los objetos.");
            }

            
            br1.close();
            br2.close();//Cerramos lo BufferedReader
            cl.close();//Cerramos el socket cliente
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}