import java.io.*;

public class ObjetoPrueba implements Serializable {
    //Atributos:
    int atributo1;
    transient String atributo2;
    double atributo3;
    transient boolean atributo4;
    char atributo5;
    transient long atributo6;
    
    public ObjetoPrueba() {
        
    }
    
    public ObjetoPrueba( int atri1, String atri2, double atri3, boolean atri4, char atri5, long atri6 ) {
        
        this.atributo1 = atri1;
        this.atributo2 = atri2;
        this.atributo3 = atri3;
        this.atributo4 = atri4;
        this.atributo5 = atri5;
        this.atributo6 = atri6;
    }
    
    
    // Método para imprimir todos los atributos del objeto
    public void imprimirAtributos() {
        System.out.println("Atributo1: " + this.atributo1);
        System.out.println("Atributo2: " + this.atributo2);
        System.out.println("Atributo3: " + this.atributo3);
        System.out.println("Atributo4: " + this.atributo4);
        System.out.println("Atributo5: " + this.atributo5);
        System.out.println("Atributo6: " + this.atributo6);
    }
    
    public void serializacion(ObjetoPrueba objeto) {
        // Serialización
        String archivoSerializado = "C:\\Users\\raygu\\OneDrive\\Desktop\\Redes2\\Serialización\\src\\Archivos/objetoprueba1.txt";

        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(archivoSerializado))) {
            salida.writeObject(objeto);
            System.out.println("Objeto serializado correctamente en " + archivoSerializado);
        } catch (IOException e) {
            System.out.println("Error al serializar el objeto: " + e.getMessage());
        }
    }
    
    public ObjetoPrueba deserializacion(){        
        // Deserialización
        ObjetoPrueba objetoDeserializado = null;
        
        String archivoSerializado = "C:\\Users\\raygu\\OneDrive\\Desktop\\Redes2\\Serialización\\src\\Archivos/objetoprueba1.txt";
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivoSerializado))) {
            objetoDeserializado = (ObjetoPrueba) entrada.readObject();
            System.out.println("Objeto deserializado correctamente:");
            System.out.println(objetoDeserializado);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al deserializar el objeto: " + e.getMessage());
        }
        
        return objetoDeserializado;
    }
    
    public void leerArchivoSerializado() {
         // Ruta del archivo donde se guardaron los datos serializados
        String archivoSerializado = "C:\\Users\\raygu\\OneDrive\\Desktop\\Redes2\\Serialización\\src\\Archivos/objetoprueba1.txt";

        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(archivoSerializado))) {
            // Lee el objeto serializado del archivo
            ObjetoPrueba objetoDeserializado = (ObjetoPrueba) entrada.readObject();   
            // Imprime los valores de los atributos del objeto deserializado
            System.out.println("Valores de atributos serializados:");
            System.out.println("Atributo1: " + objetoDeserializado.atributo1);
            System.out.println("Atributo2: " + objetoDeserializado.atributo2);
            System.out.println("Atributo3: " + objetoDeserializado.atributo3);
            System.out.println("Atributo4: " + objetoDeserializado.atributo4);
            System.out.println("Atributo5: " + objetoDeserializado.atributo5);
            System.out.println("Atributo6: " + objetoDeserializado.atributo6);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al leer el archivo serializado: " + e.getMessage());
        }
    }
    
}