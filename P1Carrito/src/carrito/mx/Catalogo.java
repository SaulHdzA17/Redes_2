package carrito.mx;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class Catalogo implements Serializable{
    //Atributos:
    private String nombre;
    private int cantidad;
    private double precio;
    
    //Constructor por omisión:
    public Catalogo() {
        
    }

    public Catalogo( String name, int can, double pre ) {

        this.nombre = name;
        this.cantidad = can;
        this.precio = pre;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    //Método para ir reconociendo los elementos del archivo y obtenernlos como atributos del objeto:
    public static Catalogo obtnerAtributos(String cadenaArchivo) {
        
        String nombre = "";
        int catidad = 0;
        double precio = 0;
        char c;
        int estado  = 0;
        String lexema = "";
                
        for(int i = 0; i < cadenaArchivo.length(); i++){

            c = cadenaArchivo.charAt(i);
            
            switch (estado) {
                
                case 0:
                    //Detecta si es una letra o un digito
                    if(Character.isLetter(c)){
                        
                        estado = 1;
                        lexema += c;
                    
                    }else if(Character.isDigit(c)){
                        
                        estado = 2;
                        lexema += c;
                        
                    }else{
                        
                        System.out.println("Error al cargar atributos del objeto");
                        
                    }                    
                break;
                
                case 1:                    
                    //Cuando es una letra
                    if(Character.isLetterOrDigit(c) || c == ' ' ){
                        
                        estado = 1;
                        lexema += c;
                    
                    }else if( (c == ',') || (c == '\n') || (i == (cadenaArchivo.length())) ){
                    
                        nombre = lexema;
                        //System.out.println(lexema + " " + estado);
                        lexema = "";
                        estado = 0;                       
                    }                        
                break;
                
                case 2:
                    
                    //Cuando es digito
                    if( Character.isDigit(c) ){
                        
                        estado = 2;
                        lexema += c;
                    
                    }else if( c == '.' ){
                    
                        //Es precio y no cantidad
                        estado = 3;
                        lexema += c;
                    
                    }else if( (c == ',') || (c == '\n') || (i == (cadenaArchivo.length())) ){
                    
                        //Termina de leer un atributo
                        catidad = Integer.parseInt(lexema);
                        //System.out.println(lexema + " " + estado);
                        estado = 0;
                        lexema = "";
                        
                    }
                    
                break;
                
                case 3:
                    
                    //Es un precio
                    
                    if( Character.isDigit(c) ){
                        
                        estado = 3;
                        lexema += c;
                        //System.out.println(lexema + " " + estado);
                                            
                    }else{
                    
                        //Termina de leer un atributo
                        precio = Double.parseDouble(lexema);
                        //System.out.println(lexema + " " + estado + "++++++++++++++++++++");
                        estado = 0;
                        lexema = "";
                        
                    }
                    
                break;
                
                default:
                throw new AssertionError();
            }

            
        }

        return new Catalogo(nombre, catidad, precio);
    }
    
    public static void imprimirAtributos (Catalogo obj){

        System.out.println("Nombre: " + obj.getNombre() + 
                            "\nCantidad: " + obj.getCantidad() + 
                            "\nPrecio: $" + obj.getPrecio() );

    }

    
    public static void serializarObjetos(List<Catalogo> objetos) {//Método para serialziar los objetos del ArrayList que recibe como parámetro
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("objetos.ser"))) {
            outputStream.writeObject(objetos);
            System.out.println("Array de objetos serializado correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Catalogo> deserializarObjetos() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("objetos.ser"))) {
            return (List<Catalogo>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
