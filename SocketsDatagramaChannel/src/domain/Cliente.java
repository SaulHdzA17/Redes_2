package domain;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Cliente {
    public final static int PUERTO = 7;//Puerto del servidor
    private final static int LIMITE = 100;// Número máximo de paquetes a enviar
    
    public static void main(String[] args) {
        boolean bandera=false;
        SocketAddress remoto = new InetSocketAddress("127.0.0.1",PUERTO);//Definimos la dirección del servidor
    
    try{
        DatagramChannel canal = DatagramChannel.open();//Abrimos un canal de datagrama
        canal.configureBlocking(false);//Configuramos el canal en modo no bloqueante
        canal.connect(remoto);//Conectamos el canal a la dirección del servidor
        Selector selector = Selector.open();//Abrimos un selector
        canal.register(selector,SelectionKey.OP_WRITE);//Registramos el canal con el selector para operaciones de escritura
        ByteBuffer buffer = ByteBuffer.allocateDirect(4);//Creamos un buffer directo para enviar datos
        int n = 0;
        
        while(true){
            selector.select(5000); //Espera 5 segundos por la conexión
            Set sk = selector.selectedKeys();//Obtenemos el conjunto de claves seleccionadas
            
            if(sk.isEmpty() && n == LIMITE || bandera){//Verificamos si no hay claves y se alcanzó el límite, o la bandera está activa
                canal.close();//Cerramos el canal
                break;
            }else{
                Iterator it = sk.iterator();
                
                while(it.hasNext()){
                    SelectionKey key = (SelectionKey)it.next();
                    it.remove();//Eliminamos la clave del conjunto
                    
                    if(key.isWritable()){//Verificamos si la clave está lista para escritura
                        buffer.clear();//Limpiamos el buffer
                        buffer.putInt(n);//Escribimos un entero en el buffer
                        buffer.flip();//Preparamos el buffer para lectura
                        canal.write(buffer);//Escribe el buffer en el canal
                        System.out.println("Escribiendo el dato: "+n);
                        n++;
                        
                        if(n==LIMITE){//Verificamos si se alcanzó el límite de paquetes
                            //todos los paquetes han sido escritos;
                            //Escribimos el número 1000 para indicar el fin de la comunicación
                            buffer.clear();
                            buffer.putInt(1000);
                            buffer.flip();
                            canal.write(buffer);
                            bandera = true;//Activamos la bandera para cerrar el canal
                            key.interestOps(SelectionKey.OP_READ);//Cambia la operación de la clave a lectura
                            break;
                        }//if
                    }//if
                }//while
            }//else
        }//while
    }catch(Exception e){
    System.err.println(e);
    }//catch
    }//main
}
