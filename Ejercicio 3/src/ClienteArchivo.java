import javax.swing.JFileChooser;//Permitimos la selección de archivos a través de una interfaz gráfica
import java.net.*;
import java.io.*;
import java.io.File;
import java.io.FileWriter;

public class ClienteArchivo {
    public static void main(String[] args) {
        ObjetoPrueba op1 = new ObjetoPrueba( 5,"Saul Henandez", 2.5, true, 'f', 1l);
        try{
            System.out.println("Atributos antes de ser serialziado:");
            op1.imprimirAtributos();
            op1.serializacion(op1);
            op1.leerArchivoSerializado();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));//Permite leer la entarda del usuario desde consola
            System.out.println("Escriba la dirección del servidor: ");
            String host = br.readLine();
            System.out.println("Escriba el puerto:");
            int pto = Integer.parseInt(br.readLine());//Leemos el puerto del servidor dada por el usuario
            Socket cl = new Socket(host,pto);//Creamos un socket que se asocia con la dirección y el puerto que proporciona el usuario y se conecta al servidor
            JFileChooser  jf = new JFileChooser();//Abre un cuadro de diálogo para que el usuario seleccione un archivo   
            jf.setMultiSelectionEnabled(true); //Con este metodo podemos seleccionar varios archivos en el cuadro de dialogo
            int r = jf.showOpenDialog(null);//Muestra el diálogo de selección de archivo y espera la selección del usuario            
            if (r == JFileChooser.APPROVE_OPTION) {
                File[] arrayFiles = jf.getSelectedFiles();//Guardamos todos los archivos seleccionados por el usuario en este arreglo
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());//Flujo de salida para enviar datos de lo archivos
                dos.writeInt(arrayFiles.length);//Escribimos estos datos en el arreglo
                
                for(File f: arrayFiles) {//Con este ciclo iteramos sobre cada archivo seleccioando por el usuario
                    String nombre = f.getName();//Obtenemos el nombre del archivo
                    long tam = f.length();//Obtenemos el tamaño del archivo
                    dos.writeUTF(nombre);//Enviamos el dato del nombre a través del socket
                    dos.flush();
                    dos.writeLong(tam);//Eviamos el dato del tamaño a trvés del socket
                    dos.flush();
                    try (FileInputStream fis = new FileInputStream(f)){//Enviamos el contenido de los archivos al servidor mediante el socket
                        byte[] b = new byte[1024];//Creamos el arreglo para leer el contenido de los archivos en bloques de bytes
                        int bytesRead;
                        long enviados = 0;
                        int porcentaje;
                        while((bytesRead = fis.read(b)) != -1) {//Leemos los bloqus de los archivos enviados
                            dos.write(b, 0, bytesRead);//Envio del bloque mediante el flujo de salida
                            dos.flush();
                            enviados = enviados + bytesRead;
                            porcentaje = (int)(enviados*100/tam);
                            System.out.println("Enviado: " + porcentaje + "%\r");
                        }
                    }
                    System.out.println("\n\nArchivo:" + nombre + "enviado");
                }
                
                dos.close();
            }
            cl.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    
}