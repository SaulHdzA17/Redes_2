package domain;
import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class Servidor {
    public final static int PUERTO = 7;//Puerto en el que escuchará el servidor
    public final static int TAM_MAXIMO = 65507;//Tamaño máximo de buffer para un datagrama
    
    public static void main(String[] args) {
        int port = PUERTO;
        
        try{
            DatagramChannel canal = DatagramChannel.open();//Abrimos un canal de datagrama
            canal.configureBlocking(false);//Configuramos el canal en modo no bloqueante
            DatagramSocket socket = canal.socket();//Obtenemos el socket asociado al canal
            SocketAddress dir = new InetSocketAddress(port);//Vinculamos el socket a la dirección y puerto especificados
            socket.bind(dir);
            Selector selector = Selector.open();//Abrimos un selector
            canal.register(selector,SelectionKey.OP_READ);//Registramos el canal con el selector para operaciones de lectura
            ByteBuffer buffer = ByteBuffer.allocateDirect(TAM_MAXIMO);//Creamos un buffer para recibir datos
            
            while(true){
                selector.select(5000);//Espera eventos de E/S durante 5 segundos
                Set sk = selector.selectedKeys();//Obtenemos el conjunto de claves seleccionadas
                Iterator it = sk.iterator();
                
                while(it.hasNext()){
                    SelectionKey key = (SelectionKey)it.next();
                    it.remove();//Eliminamos la clave del conjunto
                    
                    if(key.isReadable()){//Verificamos si la clave está lista para lectura
                        buffer.clear();//Limpiamos el buffer
                        SocketAddress client = canal.receive(buffer);//Recibimos datos en el buffer
                        buffer.flip();//Preparamos el buffer para lectura
                        int eco = buffer.getInt();//Leemos un entero del buffer
                        
                        if(eco==1000){//Si el entero es 1000, cierra el canal y termina
                            canal.close();
                            System.exit(0);
                        }else{
                            System.out.println("Dato leido: "+eco);//Imprimimos el dato recibido
                            buffer.flip();//Preparamos el buffer para escritura
                            canal.send(buffer,client);//Enviamos el mismo dato de vuelta al cliente
                        }//else
                    }//if
                }//while2
            }//while
        }catch(IOException e){
        System.err.println(e);
        }//catch
    }//main
}
