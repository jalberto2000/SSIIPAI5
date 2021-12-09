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
            String[] datos = message[0].split(","); //DENTRO DEL MENSAJE APARECE NUMEROSILLAS,NUMEROMESAS,NUMEROARMARIOS,NUMEROESTANTERIAS,NOMBREUSUARIO
            System.out.println(Arrays.toString(message));
            String usuario = datos[4];
            if(baseDatosUsuarios.getKey(usuario)== null){
                baseDatosUsuarios.insertUser(usuario, message[1]);
            }else {
                if (baseDatosUsuarios.getKey(usuario).equals(message[1])) {

                    Signature sg = Signature.getInstance("SHA256withRSA");
                    PublicKey clavePublica = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(message[1].getBytes(StandardCharsets.UTF_8)));
                    sg.initVerify(clavePublica);
                    sg.update(message[0].getBytes());
                    Boolean mensajeVerificado = sg.verify(message[2].getBytes()); //USO LA FIRMA PARA VERIFICAR EL MENSAJE
                    if (mensajeVerificado) {
                        //TODO RESPONDER AL CLIENTE DICIENDO QUE SU MENSAJE SE HA VERIFICADO
                        baseDatosTransacciones.insertTransaction(message[0], Boolean.TRUE);
                    } else {
                        //TODO RESPONDER AL CLIENTE DICIENDO QUE SU MENSAJE NO HA PODIDO SER VERIFICADO
                        baseDatosTransacciones.insertTransaction(message[0], Boolean.FALSE);
                    }
                } else {
                    baseDatosTransacciones.insertTransaction(message[0], Boolean.FALSE);
                    throw new Exception("FALLO EN LA VERIFICACION DEL USUARIO");
                }
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    } // end





}

