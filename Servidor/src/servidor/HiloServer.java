package servidor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HiloServer implements Runnable{
    public static SSLServerSocket serverSocket;
    public static SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
    public static Socket socket = null;
    public HiloServer(){}



    @Override
    public void run() {
        System.setProperty("javax.net.ssl.keyStore", "C:/Users/alber/Desktop/PAI5/SSIIPAI5/Servidor/keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "equipo2");
        try{

            LoginServerSocket.logicaServidor(socket);

        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException{

        ServerSocket serverSocket = new ServerSocket(10000);
    while(true){
        System.err.println("Waiting for connection...");
        socket = serverSocket.accept();

        Thread thread = new Thread(new HiloServer());
        thread.start();
    }

    }
}
