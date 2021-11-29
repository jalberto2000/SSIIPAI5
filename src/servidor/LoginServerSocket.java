package servidor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class LoginServerSocket {

    public static void logicaServidor(Socket socket) throws IOException {
        try {

            // open BufferedReader for reading data from client
            BufferedReader input = new BufferedReader(new
                    InputStreamReader(socket.getInputStream()));

            // open PrintWriter for writing data to client
            PrintWriter output = new PrintWriter(new
                    OutputStreamWriter(socket.getOutputStream()));

            String message = input.readLine();
            System.out.println(message);
            output.println("El mensaje ha sido guardado correctamente");
            output.close();
            input.close();
            socket.close();

        } // end try

        // handle exception communicating with client
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

    } // end





}

