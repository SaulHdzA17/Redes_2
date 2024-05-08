package carrito.mx;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cliente {
    
    
     public static void main(String[] args) {
         
         ArrayList<Catalogo> listaRecibida;
         
         try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));//Creamos un objeto BufferedReader para poder leer 
            //la entrada que nos de el usuario desde la consola, alternativa a scanner.
            System.out.println("Escriba la direccion del servidor: ");//Proporcionamos la dirección IP o el nombre del servidor, debe ser la 
            String host = br1.readLine();//dirección en donde se está ejecutando el programa del Servidor, en est caso localhost
            System.out.println("Escriba el puerto: ");//Escribimos el puerto al que deseamos ingresar desde la entrada estandar como la entrada
            int pto = Integer.parseInt(br1.readLine());//anterior, en este caso 1234 es el puerto que está escuchando el servidor
            Socket cl = new Socket(host, pto);//Creamos un objeto Socket llamado "cl" que vamos a conectar al servidor especificado por la 
            
            System.out.println("Bienvenido a la tiendita 5 peso");//
            
            //Flujo de datos que entra con el array serialziado desde el Servidor:
            InputStream inputStream = cl.getInputStream();
            //Leemos lsod atos serializados:
            byte[] arg = inputStream.readAllBytes();
            
            // Deserializar los datos recibidos
            listaRecibida = Catalogo.deserializarLista(arg);

//            for( Catalogo obj: listaRecibida ){
//
//                    obj.imprimirAtributos(obj);
//
//            }
            

            int dec = 0;

            do{
                
                System.out.println("¿Que desea hacer?");
                System.out.println("1.-Mostar productos");
                System.out.println("2.-Ver Carrito de compras");
                System.out.println("3.-Pagar");
                System.out.println("4.-Salir");
                dec = Integer.parseInt(br1.readLine());
                Cliente.opcionesMenu(dec, listaRecibida);
                
            }while(dec != 4);
            

            br1.close();
            cl.close();//Cerramos el socket cliente
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
     
     
    private static void opcionesMenu( int dec, ArrayList<Catalogo> listaRecibida ){
        
        Scanner scan = new Scanner(System.in);
        
        switch(dec){
        
            case 1:
                
                //Mostar 
                int x = 0;
                Cliente.mostrarProducto(listaRecibida);
                System.out.println("¿Deseas comprar algo?\n1.-Si\t2.-No");
                x = Integer.parseInt(scan.nextLine());
                if( x == 1){
                    
                  Cliente.mostrarProducto(listaRecibida);
                  System.out.println("Ingrese una opcion");
                  x = Integer.parseInt(scan.nextLine());
                    
                }
                
            break;

            
            case 2:
                
                //Ver carrito
                
            break;

            
            case 3:
                
                //Pagar
                
            break;

            
            case 4:
                
                //Salir
                
            break;

            
            default:
                
                //Opcion no valida
                
            break;
        
        }
        
    }
    
    private static void mostrarProducto( ArrayList<Catalogo> listaRecibida ){
        
        
        for( int x = 0; x < listaRecibida.size(); x++ ){
            
            if( listaRecibida.get(x).getCantidad() == 0){
                
                x--;
            
            }else{
                
                System.out.println( (x + 1) + ".- " + listaRecibida.get(x).getNombre() + " " + listaRecibida.get(x).getPrecio());
            
            }
            
        }
        
    }
    
}