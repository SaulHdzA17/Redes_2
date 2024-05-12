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
                 if (carrito.isEmpty()) {
                    System.out.println("El carrito está vacío. No hay nada que pagar.");
                } else {
                    // Calcular el total de la compra
                    double totalCompra = 0;
                    for (Catalogo producto : carrito) {
                        totalCompra += producto.getPrecio() * producto.getCantidad();
                    }

                    // Mostrar el total de la compra
                    System.out.println("Total de la compra: $" + totalCompra);

                    // Procesar la forma de pago (simplemente imprimir un mensaje de confirmación)
                    System.out.println("¡Pago realizado con éxito! Gracias por su compra.");

                    // Limpiar el carrito después de pagar
                    carrito.clear();
                }
            break;

            
            case 4:                
                //Salir
                System.out.println("Saliendo...");
                return; // Esto terminará el método opcionesMenu y, por lo tanto, el cliente saldrá del bucle do-while y finalizará su ejecución

            
            default:
                //Opcion no valida
                System.out.println("Ingrese una opción valida, del 1 al 4");
                
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

    private static void agregarAlCarrito(ArrayList<Catalogo> listaRecibida, ArrayList<Catalogo> carrito) {
    Scanner scan = new Scanner(System.in);
    mostrarProducto(listaRecibida);
    System.out.println("Ingrese el número del producto que desea agregar al carrito:");
    int opcion = scan.nextInt() - 1;

    if (opcion < 0 || opcion >= listaRecibida.size()) {
        System.out.println("Opción inválida.");
        return;
    }

    Catalogo productoSeleccionado = listaRecibida.get(opcion);

    System.out.println("Ingrese la cantidad que desea agregar:");
    int cantidad = scan.nextInt();

    if (cantidad <= 0 || cantidad > productoSeleccionado.getCantidad()) {
        System.out.println("Cantidad inválida.");
        return;
    }

    // Verificar si el producto ya está en el carrito
    boolean encontrado = false;
    for (Catalogo item : carrito) {
        if (item.getNombre().equals(productoSeleccionado.getNombre())) {
            // Actualizar la cantidad si ya está en el carrito
            item.setCantidad(item.getCantidad() + cantidad);
            encontrado = true;
            break;
        }
    }

    // Si el producto no está en el carrito, agregarlo
    if (!encontrado) {
        Catalogo nuevoProducto = new Catalogo(productoSeleccionado.getNombre(), cantidad, productoSeleccionado.getPrecio());
        carrito.add(nuevoProducto);
    }

    // Actualizar la cantidad disponible en el catálogo
    productoSeleccionado.setCantidad(productoSeleccionado.getCantidad() - cantidad);

    // Limpia el catálogo después de agregar productos al carrito
    limpiarCatalgo(listaRecibida);
}


    public static void limpiarCatalgo( ArrayList <Catalogo> listaRecibida ){

        for( int x = 0; x < listaRecibida.size(); x++ ){

            if( listaRecibida.get(x).getCantidad() == 0 ){
                
                listaRecibida.remove(x);

            }

        }

    }
    
}