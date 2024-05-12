package carrito.mx;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Servidor {
    public static void main(String[] args) {
        
        String scan = new String();
        Scanner sc = new Scanner(System.in);
        Catalogo producto = new Catalogo();
        ArrayList <Catalogo> catalogo = new ArrayList();
        
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
                
                //String rutaArchivo = "C:\\Users\\raygu\\OneDrive\\Desktop\\Redes2\\Redes_2\\P1Carrito\\src\\carrito\\archivos\\productos.txt";
                String rutaArchivo = "C:\\Users\\dra55\\Documents\\GitHub\\Redes_2\\P1Carrito\\src\\carrito\\archivos\\productos.txt";
                ArrayList <String> atributos = obtenerContenidoTxt(rutaArchivo);
               
                for(int x = 0; x < atributos.size(); x++){
                    
                    //Apasamos toda la información del documento un objeto

                    //System.out.println(atributos.get(x));
                    producto = Catalogo.obtnerAtributos(atributos.get(x));
                    //Catalogo.imprimirAtributos(producto);
                    catalogo.add(producto);

                }

                /*for( Catalogo obj: catalogo ){

                    Catalogo.imprimirAtributos(obj);

                }*/
                
                //Serializamos el array de objetos tipo catalogo
                byte [] arg = Catalogo.serializarLista(catalogo);
                
                /*for( int x = 0; x < arg.length; x++ ){

                    System.out.println("arg [" + x + "] = " + arg[ x ] );

                }*/
                
                // Obtener flujo de salida del socket
                OutputStream outputStream = cl.getOutputStream();

                // Enviar los datos serializados al cliente
                outputStream.write(arg);
                outputStream.flush();
                                
                cl.close();//Cerramos el socket del Cliente creado previamentex
            }//Cerramos bucle for
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static ArrayList <String> obtenerContenidoTxt(String ruta) {//Con este método obtenemos el contenido del txt y guardamos línea por línea
        File archivo = new File(ruta);//el contenido en un elemento de
        ArrayList <String> arg = new ArrayList();
        if (archivo.exists()) {//devuelve 
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                    arg.add(line);
                }
                //System.out.println("Contenido del archivo:");
                //System.out.println(content.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("El archivo no existe.");
        }

        return arg;
         
    }
    
    
}
