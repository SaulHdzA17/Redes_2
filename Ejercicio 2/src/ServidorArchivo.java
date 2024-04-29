import java.net.*;
import java.io.*;

public class ServidorArchivo {
    public static void main(String[] args) {
        try {
            ServerSocket s = new ServerSocket(1234);//Creamos un socket para server llamado "s" ligado al puerto 1234 para las conexiones entrantes
            for(;;) {//Iniciamos el ciclo infinito para la escucha de conexiones
                Socket cl = s.accept();//"s" acepta la conexión y devuelve un objeto socket "cl" que representa la conexión con el cliente
                System.out.println("Conexion establecida desde " + cl.getInetAddress() + ":" + cl.getPort());
                DataInputStream dis = new DataInputStream(cl.getInputStream());//Creamos un flujo de nivel de bits de entrada ligado al socket "cl",
                
                int numFiles = dis.readInt(); //Se leé el número de archivos 
                
                for(int i = 0; i < numFiles; i++) {//Creamos un ciclo para iterar sobre cada archivo que se va a recibir desde el cliente
                    String nombre = dis.readUTF();//Leemos el nombre del archivo enviado por el cliente
                    long tamanio = dis.readLong();//Lee el tamaño del archivo en bytes
                
                
                    try(FileOutputStream fos = new FileOutputStream(nombre)) {//Creamos un flujo de salida para escribir el contenido del archivo
                        byte[] b = new byte[1024];//Creamos un arreglo como buffer para recibir los datos enviados por el cliente
                        int bytesRead;
                        long recibidos = 0;//Preparamos los datos para recibir los paquetes de datos del archivo,
                        int porcentaje;//es decir, un calculo de porcentaje dentro del while
                        while(recibidos < tamanio && (bytesRead = dis.read(b, 0, (int) Math.min(b.length, tamanio - recibidos))) != -1) {//Dentro de este ciclo recibimos los datos del archivo que envia el cliente, leyendo estos datos en bloques de
                            //bytes, escribimos esos bloques en el archivo de salida del Servidor, muestra el progreso de la transferencia
                            fos.write(b,0,bytesRead);
                            recibidos = recibidos + bytesRead;
                            porcentaje = (int)(recibidos*100/tamanio);
                            System.out.println("Archivo recibido");
                        }
                    }
                
                    System.out.println("Recibimos el archivo: " + nombre);
                }
                
                dis.close();//Cerramoa el flujo de entarda
                cl.close();//Cerramos el socket
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
