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
    private static final String[] PROTOCOLOS = new String[] {"TLSv1.3"};
    public static SSLServerSocket serverSocket;
    public static SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
    public static Socket socket = null;
    public HiloServer(){}



    @Override
    public void run() {
        try{

            LoginServerSocket.logicaServidor(socket);

        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException{
        System.setProperty("javax.net.ssl.keyStore", "../../keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "equipo2");
        serverSocket = (SSLServerSocket) socketFactory.createServerSocket(10000);
        serverSocket.setEnabledProtocols(PROTOCOLOS);
    while(true){
        System.err.println("Waiting for connection...");
        socket = serverSocket.accept();
        System.out.println("hitler");

        Thread thread = new Thread(new HiloServer());
        thread.start();
    }

    }
}
