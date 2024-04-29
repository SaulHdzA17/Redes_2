import java.net.*;
import java.io.*;

public class SHola {
    public static void main(String[] args) {
        try {
            ServerSocket s = new ServerSocket(1234);//Creamos un socket llamdo "s" y lo inicializamos con el valor del puerto, en este caso
            System.out.println("Esperando cliente...");//1234 es el puerto, la creación del socket del server es dentro de un try catch
            //Para la escucha del socket debemmos hacero dentro de un ciclo for infinito y espera la silicitud de conexión del cliente
            for(;;) {
                Socket cl = s.accept();//"s" acepta la conexión con accept() y devuelve un objeto Socket "cl" que representa la conexión
                System.out.println("Conexion establecida desde: " + cl.getInetAddress() + ":" + cl.getPort());//Imprime la dirección IP y
                //puerto del cliente que se ha conectado al servidor
                String mensaje = "Nombre completo: Hernandez Alonso Saul. Grupo: 6CM4. Materia: Aplicaciones para Comunicaciones en Red.";//Creamos el mensaje que enviaremos al cliente.
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));//Usamos PrintWriter para un flujo de salida,
                pw.println(mensaje);//en este caso lo vamos a ligar al mensaje que escribimos en el Servidor para enviarlo y mostrarlo 
                pw.flush();//en la consola del Cliente, limpiamos el flujo.
                
                //Parte modificada del programa para poder recibir el mensaje de vuelta:
                BufferedReader br = new BufferedReader(new InputStreamReader(cl.getInputStream()));//Creamos un flujo de entrada BufferedReader
                //para leer los datos que vienen que envia el cliente a través del InputStream del socket cliente que está concetado con SHola
                String mensajeCliente = br.readLine();//Leemos el mensaje que nos envia el cliente
                System.out.println("Mensaje recibido del Cliente: " + mensajeCliente);//Imprimimos el mensaje enviado por el cliente
                
                pw.close();//Cerramos el objeto pw.
                cl.close();//Cerramos el socket del Cliente creado previamentex
            }//Cerramos bucle for
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
