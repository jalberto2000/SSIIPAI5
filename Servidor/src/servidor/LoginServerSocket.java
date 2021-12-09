package servidor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class LoginServerSocket {

    public static void logicaServidor(Socket socket) throws IOException {
        try {

            // open BufferedReader for reading data from client
            BufferedReader input = new BufferedReader(new
                    InputStreamReader(socket.getInputStream()));

            // open PrintWriter for writing data to client
            PrintWriter output = new PrintWriter(new
                    OutputStreamWriter(socket.getOutputStream()));
            String[] message = input.readLine().split(" "); //RECIBO MENSAJE DE LA FORMA MENSAJE CLAVE FIRMA
            System.out.println(Arrays.toString(message));
            PublicKey clavePublica = null;
            if(message.length == 3){

                //TODO ALMACENAR LA CLAVE PUBLICA EN LA BASE DE DATOS
                //clavePublica = KeyFactory.getInstance("RSA"). //OBTENGO LA CLAVE PUBLICA
                        //KeyFactory.getInstance("RSA").generatePublic();

            }else if(message.length == 2){
                //TODO OBTENER LA CLAVE PUBLICA DE LA BASE DE DATOS
            }else{
                System.out.println("ERROR EN EL PAQUETE");
            }
            Signature sg = Signature.getInstance("SHA256withRSA");
            sg.initVerify(clavePublica);
            sg.update(message[0].getBytes());
            Boolean mensajeVerificado = sg.verify(message[2].getBytes()); //USO LA FIRMA PARA VERIFICAR EL MENSAJE
            if(mensajeVerificado){
                //TODO RESPONDER AL CLIENTE DICIENDO QUE SU MENSAJE SE HA VERIFICADO
            }else{
                //TODO RESPONDER AL CLIENTE DICIENDO QUE SU MENSAJE NO HA PODIDO SER VERIFICADO
            }
            System.out.println(message[0]);
            output.println("El mensaje ha sido guardado correctamente");
            output.close();
            input.close();
            socket.close();

        } // end try

        // handle exception communicating with client
        catch (IOException | NoSuchAlgorithmException | InvalidKeyException | SignatureException ioException) {
            ioException.printStackTrace();
        }

    } // end





}

