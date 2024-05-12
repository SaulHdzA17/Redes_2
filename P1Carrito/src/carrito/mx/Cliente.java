package carrito.mx;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente {
    
    static Scanner scan = new Scanner(System.in);
    
     public static void main(String[] args) {
         
         ArrayList<Catalogo> listaRecibida = null;
         ArrayList<Catalogo> carrito = new ArrayList<>();
         
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

            //Leemos los datos serializados:
            byte[] arg = inputStream.readAllBytes();
                    
            /*for( int x = 0; x < arg.length; x++ ){

                System.out.println("arg [" + x + "] = " + arg[ x ] );

            }*/

            // Deserializar los datos recibidos
            listaRecibida = Catalogo.deserializarLista(arg);
            
            /*for( Catalogo obj: listaRecibida ){

                    obj.imprimirAtributos(obj);

            }*/
            

            int dec = 0;

            do{
                
                Cliente.limpiarCatalgo(listaRecibida);
                
                System.out.println("¿Que desea hacer?");
                System.out.println("1.-Mostar productos");
                System.out.println("2.-Ver Carrito de compras");
                System.out.println("3.-Pagar");
                System.out.println("4.-Salir");
                dec = Integer.parseInt(br1.readLine());
                Cliente.opcionesMenu(dec, listaRecibida, carrito);
                
            }while(dec != 4);
            

            inputStream.close();
            br1.close();
            cl.close();//Cerramos el socket cliente
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
     
     
    private static void opcionesMenu( int dec, ArrayList<Catalogo> listaRecibida, ArrayList<Catalogo> carrito ){
                
        int x = 0;

        switch(dec){
        
            case 1:
                
                //Mostar 
                Cliente.mostrarProducto(listaRecibida);
                System.out.println("¿Deseas agregar algo al tu carrito?\n1.-Si\t2.-No");
                x = scan.nextInt();
                if( x == 1 ){

                    Cliente.agregarAlCarrito(listaRecibida, carrito);
                    
                }
                
            break;

            
            case 2:
                
                //Ver carrito
                if ( Cliente.imprimirCarrito(carrito) ) {
                
                    System.out.println("¿Quieres quitar algo del carrito?\n1.-Si\t2.-No");
                    x = scan.nextInt();
                    if (x == 1) {

                        
                        
                    }

                }

                
                
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
        
            System.out.println( (x + 1) + ".- " + listaRecibida.get(x).getNombre() + 
            " " + listaRecibida.get(x).getCantidad() + " $" + listaRecibida.get(x).getPrecio());
            
        }
        
    }

    private static void quitarDelCarrito( ArrayList <Catalogo> carrito ){



    }

    private static boolean imprimirCarrito( ArrayList <Catalogo> carrito ){

        int x = 0;
        if (carrito.isEmpty()) {

            System.out.println("No se a agregado nada al carrito");
            
            return false;

        }else{

            for( x = 0; x < carrito.size(); x++ ){

                System.out.println( (x + 1) + ".- " + carrito.get(x).getNombre() + 
                " " + carrito.get(x).getCantidad() + " $" + carrito.get(x).getPrecio());
            
            }

            return true;
        }

    }

    private static void agregarAlCarrito( ArrayList <Catalogo> listaRecibida, ArrayList <Catalogo> carrito ){

        //Agregamos al carrito 

        int x = 0, cant = 0;
        
        Cliente.mostrarProducto(listaRecibida);
        
        System.out.println("Ingrese una opcion");
        x = scan.nextInt() - 1;
        
        //Verificamos que haya stock
        System.out.println("¿Cuantos deseas agregar?");
        cant = scan.nextInt();

        System.out.println("Cantidad del producto = " + listaRecibida.get(x).getCantidad()
                           + "\nx = " + x 
                           + "\ncant = " + cant );

        while( cant > listaRecibida.get(x).getCantidad() ){

            System.out.println("Ingrese una cantida adecuada");
            cant = scan.nextInt();

        }
        
        if ( carrito.contains(listaRecibida.get(x)) == true ) {
            
            System.out.println("***************1*****************");
            //Si se encuentra en el carrito, silo debemos de alter la cantidad
            //Alteramos la catidad
            carrito.get(carrito.indexOf(listaRecibida.get(x))).setCantidad( carrito.get(x).getCantidad() + cant );
            //Por cada objeto en el carrito, se quita uno en el catalogo
            listaRecibida.get(x).setCantidad( listaRecibida.get(x).getCantidad() - cant );

        }else{

            System.out.println("---------------2---------------");
            //Si no se encuentra en el carrito tenemos que agregarlo y alterar cantidad
            carrito.add(listaRecibida.get(x));//Primero agregamos el objeto al carrito y despues alteramos su cantidad
            //Aletramos la cantidad
            carrito.get( carrito.indexOf( listaRecibida.get(x) ) ).setCantidad(cant);
            //Por cada objeto en el carrito, se quita uno en el catalogo
            listaRecibida.get(x).setCantidad( listaRecibida.get(x).getCantidad() - cant );

        }

    }

    public static void limpiarCatalgo( ArrayList <Catalogo> listaRecibida ){

        for( int x = 0; x < listaRecibida.size(); x++ ){

            if( listaRecibida.get(x).getCantidad() == 0 ){
                
                listaRecibida.remove(x);

            }

        }

    }
    
}