package carrito.mx;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Servidor {
    public static void main(String[] args) {
        
        String scan = new String();
        Scanner sc = new Scanner(System.in);
        
        try {
            
            System.out.println("*****Iniciando servidor*****");
            System.out.println("Ingrese el puerto a utilizar:");
            scan = sc.nextLine();
            System.out.println("Puerto "+ scan +" ingresado");
            System.out.println("Validando conexion");
            
            ServerSocket s = new ServerSocket(Integer.parseInt(scan));//Creamos un socket llamdo "s" y lo inicializamos con el valor del puerto, en este caso
            System.out.println("Esperando cliente...");//6040 es el puerto, la creación del socket del server es dentro de un try catch
            //Para la escucha del socket debemmos hacero dentro de un ciclo for infinito y espera la silicitud de conexión del cliente
            for(;;) {
                Socket cl = s.accept();//"s" acepta la conexión con accept() y devuelve un objeto Socket "cl" que representa la conexión
                System.out.println("Conexion establecida desde: " + cl.getInetAddress() + ":" + cl.getPort());//Imprime la dirección IP y
                //puerto del cliente que se ha conectado al servidor
                String mensaje = "Nombre completo: Hernandez Alonso Saul. Grupo: 6CM4. Materia: Aplicaciones para Comunicaciones en Red.";//Creamos el mensaje que enviaremos al cliente.
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));//Usamos PrintWriter para un flujo de salida,
                pw.println(mensaje);//en este caso lo vamos a ligar al mensaje que escribimos en el Servidor para enviarlo y mostrarlo 
                pw.flush();//en la consola del Cliente, limpiamos el flujo.

                pw.close();//Cerramos el objeto pw.
                cl.close();//Cerramos el socket del Cliente creado previamentex
            }//Cerramos bucle for
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
