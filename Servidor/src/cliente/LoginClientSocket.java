package cliente;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

public class LoginClientSocket {

    public static void main(String[] args){
        try {

            // create SSLSocket from factory
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) socketFactory.createSocket("localhost", 10000);
            System.out.println(socket.getSession().getProtocol());
            // create PrintWriter for sending login to server
            PrintWriter output = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream()));

            String mensaje = JOptionPane.showInputDialog(null, "Introduce el mensaje");
            output.println(mensaje);
            output.flush();
            // create BufferedReader for reading server response
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            String respuesta = input.readLine();
            JOptionPane.showMessageDialog(null, respuesta);
            // clean up streams and Socket
            output.close();
            input.close();
            socket.close();

        } // end try

        // handle exception communicating with server
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

        // exit application
        finally {
            System.exit(0);
        }

    }
}