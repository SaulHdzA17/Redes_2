package carrito.mx;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Cliente {
    
    static Scanner scan = new Scanner(System.in);
    
    public static void main(String[] args) {
        ArrayList<Catalogo> listaRecibida = null;
        ArrayList<Catalogo> carrito = new ArrayList<>();
    
        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Escriba la dirección del servidor:");
            String host = br1.readLine();
            System.out.println("Escriba el puerto:");
            int pto = Integer.parseInt(br1.readLine());
            Socket cl = new Socket(host, pto);
    
            System.out.println("Bienvenido a la tiendita 5 peso");
    
            // Flujo de datos que entra con el array serialziado desde el Servidor:
            InputStream inputStream = cl.getInputStream();
            byte[] arg = inputStream.readAllBytes();
    
            // Deserializar los datos recibidos
            listaRecibida = Catalogo.deserializarLista(arg);
    
            int dec = 0;
            do {
                Cliente.limpiarCatalogo(listaRecibida);
    
                System.out.println("¿Qué desea hacer?");
                System.out.println("1.- Mostrar productos");
                System.out.println("2.- Ver Carrito de compras");
                System.out.println("3.- Pagar");
                System.out.println("4.- Salir");
                dec = Integer.parseInt(br1.readLine());
                opcionesMenu(dec, listaRecibida, carrito);
    
                // Serializar y enviar los datos actualizados al servidor después de cada acción del usuario
                arg = Catalogo.serializarLista(listaRecibida);
                OutputStream outputStream = cl.getOutputStream();
                outputStream.write(arg);
                outputStream.flush();
                outputStream.close();
    
                // Esperar datos actualizados del servidor (opcional)
                inputStream = cl.getInputStream();
                arg = inputStream.readAllBytes();
    
            } while (dec != 4);
    
            // Cerrar flujos y sockets
            inputStream.close();
            br1.close();
            cl.close();
        } catch (Exception e) {
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
                        
                        Cliente.quitarDelCarrito( listaRecibida, carrito);
                        
                    }
                }
                
            break;

            
            case 3:
                //Pagar
                generarPDF(carrito);
                
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

    private static void quitarDelCarrito( ArrayList<Catalogo> listaRecibida, ArrayList <Catalogo> carrito ){

        Cliente.imprimirCarrito(carrito);
        System.out.println("Seleccione el producto a omitir del carrito");
        int opcion = scan.nextInt() - 1;
        int indiceLista = 0;
        
        for( int x = 0; x < listaRecibida.size(); x++ ){
            
            if( carrito.get(opcion).getNombre().equals( listaRecibida.get(x).getNombre() ) ){
                 
                indiceLista = x;
                break;
                
            }
            
        }
        
        listaRecibida.get(indiceLista).setCantidad( listaRecibida.get(indiceLista).getCantidad() + carrito.get(opcion).getCantidad() );
        carrito.remove(opcion);
        
        Cliente.imprimirCarrito(carrito);

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


    public static void limpiarCatalogo( ArrayList <Catalogo> listaRecibida ){

        for( int x = 0; x < listaRecibida.size(); x++ ){

            if( listaRecibida.get(x).getCantidad() == 0 ){
                
                listaRecibida.remove(x);

            }

        }

    }
    
    
    
    
    
    
    
    
    
    
    
    private static void generarPDF(ArrayList<Catalogo> carrito) {
        try {
            // Crear un nuevo documento PDF
            PdfWriter writer = new PdfWriter("carrito_compra.pdf");
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            // Agregar título
            document.add(new Paragraph("Carrito de Compra"));
            
            // Agregar lista de productos del carrito
            document.add(new Paragraph("Lista de Productos:"));
            for (Catalogo producto : carrito) {
                String descripcion = producto.getNombre() + " - Cantidad: " + producto.getCantidad() + " - Precio unitario: $" + producto.getPrecio();
                document.add(new Paragraph(descripcion));
            }
            
            // Calcular total a pagar
            double total = 0;
            for (Catalogo producto : carrito) {
                total += producto.getCantidad() * producto.getPrecio();
            }
            
            // Agregar total a pagar
            document.add(new Paragraph("Total a Pagar: $" + total));
            
            // Obtener fecha y hora actual
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String fechaHora = now.format(formatter);
            
            // Agregar fecha y hora
            document.add(new Paragraph("Fecha y Hora: " + fechaHora));
            
            // Cerrar documento
            document.close();
            
            System.out.println("PDF generado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al generar el PDF: " + e.getMessage());
        }
    }
    
}