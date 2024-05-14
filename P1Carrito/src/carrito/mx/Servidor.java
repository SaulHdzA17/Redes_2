package carrito.mx;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Servidor {
    
   public static void main(String[] args) {
    String scan;
    Scanner sc = new Scanner(System.in);
    Catalogo producto;
    ArrayList<Catalogo> catalogo;

    try {
        System.out.println("*****Iniciando servidor*****");
        System.out.println("Ingrese el puerto a utilizar:");
        scan = sc.nextLine();
        System.out.println("Puerto " + scan + " ingresado");
        System.out.println("Validando conexión");

        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(scan))) {
            System.out.println("Esperando cliente...");

            // Mantener el servidor en un bucle infinito para recibir múltiples conexiones de clientes
            while (true) {
                try (Socket cl = serverSocket.accept()) {
                    System.out.println("Conexion establecida desde: " + cl.getInetAddress() + ":" + cl.getPort());
                    
                    String rutaArchivo = "C:\\Users\\raygu\\OneDrive\\Desktop\\Redes2\\Redes_2\\P1Carrito\\src\\carrito\\archivos\\productos.txt";
                    //String rutaArchivo = "C:\\Users\\dra55\\Documents\\GitHub\\Redes_2\\P1Carrito\\src\\carrito\\archivos\\productos.txt";
                    ArrayList<String> atributos = Servidor.obtenerContenidoTxt(rutaArchivo);

                    catalogo = new ArrayList<>();
                    for (String atributo : atributos) {
                        producto = Catalogo.obtnerAtributos(atributo);
                        catalogo.add(producto);
                    }

                    byte[] arg = Catalogo.serializarLista(catalogo);

                    // Obtener flujo de salida del socket y enviar los datos serializados al cliente
                    try (OutputStream outputStream = cl.getOutputStream()) {
                        outputStream.write(arg);
                        outputStream.flush();
                    }

                    // Esperar a que el cliente envíe los datos actualizados
                    try (InputStream inputStream = cl.getInputStream()) {
                        arg = inputStream.readAllBytes();
                    }

                    // Limpiar y actualizar el catálogo
                    catalogo.clear();
                    catalogo = Catalogo.deserializarLista(arg);
                    Servidor.guardadArchivo(rutaArchivo, catalogo);
                }
                
                
                
                
                //Evitar cerrar el proceso:
                serverSocket.close();
            }
        }

    } catch (Exception e) {
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
    
    public static void guardadArchivo ( String rutaArchivo, ArrayList <Catalogo> carrito ){
        
         try {
            // Abre el archivo en modo de escritura
            FileWriter writer = new FileWriter(rutaArchivo);
            
            // Sobrescribe el archivo con un archivo vacío
            writer.write("");
            
            
            for( Catalogo obj: carrito )
                writer.write( obj.getNombre() + "," + obj.getCantidad() + "," + obj.getPrecio() + " " );
            
            // Cierra el escritor
            writer.close();
            
            System.out.println("Contenido del archivo limpiado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al limpiar el archivo: " + e.getMessage());
        }
        
    }
    
    
}
