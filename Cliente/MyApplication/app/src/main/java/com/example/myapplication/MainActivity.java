package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


public class MainActivity extends AppCompatActivity {
    // Setup Server information

    String StringPublica = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIROXAY7AHngexLXCSE29xWPe1t0mlw8o7p2lq3/FskzHSQDSwDIhX6DYY2pQTi79b+uRKybQ2xCKCpqjUvEOd0CAwEAAQ==";

    String StringPrivada = "MIIBOwIBAAJBAIROXAY7AHngexLXCSE29xWPe1t0mlw8o7p2lq3/FskzHSQDSwDIhX6DYY2pQTi79b+uRKybQ2xCKCpqjUvEOd0CAwEAAQJBAIBJYp1/9FQ4v91iuC2GmEpFl7zz8QBio/cXKb+IylVHXhG2wFdMuZxQTOmuRhRcrbu9w6kXvBMeFS+v/LA4d0ECIQDKE6Qgmz5PnHVWznIFffllkqly599MEy7dw6j6EblC6QIhAKecg7ygMRIMdcPNwx9j1Av9DG2CheHCZ+no6i6A3l7VAiBDEfHPwLcVxWBMx4igugck52DGep9qqJNNl7tmBKvwwQIhAJNG0fLCh5umWyxL9vH0E/TcyzjGgcGXwxsjz/JAxiRpAiBI1K6bWaAGLNXTIyHhDdh/SvsrzO77H5bHZQ3GxGjbsQ==";
    protected static String server = "10.100.184.105";
    protected static int port = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThreadPolicy plc = new ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(plc);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Capturamos el boton de Enviar
        View button = findViewById(R.id.button_send);

        // Llama al listener del boton Enviar
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    // Creación de un cuadro de dialogo para confirmar pedido
    private void showDialog() throws Resources.NotFoundException {

        EditText sillas = (EditText) findViewById(R.id.cantidadSillas);
        String numeroSillas = sillas.getText().toString();
        EditText mesas = (EditText) findViewById(R.id.cantidadMesas);
        String numeroMesas = mesas.getText().toString();
        EditText armarios = (EditText) findViewById(R.id.cantidadArmarios);
        String numeroArmarios = armarios.getText().toString();
        EditText estanterias = (EditText) findViewById(R.id.cantidadEstanterias);
        String numeroEstanterias = estanterias.getText().toString();
        EditText usuario = (EditText) findViewById(R.id.nombreUsuario);
        String numeroUsuario = usuario.getText().toString();

        if (numeroSillas.matches("")&
                numeroMesas.matches("")&
                numeroArmarios.matches("")&
                numeroEstanterias.matches("")) {
            // Mostramos un mensaje emergente;
            Toast.makeText(getApplicationContext(), "Introduce al menos un mueble!", Toast.LENGTH_SHORT).show();
        } else if(Integer.parseInt(numeroSillas)>=300|
                Integer.parseInt(numeroMesas)>=300|
                Integer.parseInt(numeroArmarios)>=300|
                Integer.parseInt(numeroEstanterias)>=300) {
            Toast.makeText(getApplicationContext(), "No puedes pedir más de de 300 muebles!", Toast.LENGTH_SHORT).show();
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Enviar")
                    .setMessage("Se va a proceder al envio")
                    .setIcon(R.drawable.ic_launcher_foreground)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {




                                // Catch ok button and send information
                                public void onClick(DialogInterface dialog, int whichButton) {


                                    // 1. Extraer los datos de la vista


                                    // 2. Firmar los datos
                                    KeyPairGenerator kgen = null;
                                    try {
                                        kgen = KeyPairGenerator.getInstance("RSA");
                                    } catch (NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                    }
                                    kgen.initialize(256);

                                    KeyPair keys = kgen.genKeyPair();
                                    PublicKey publica = keys.getPublic();
                                    PrivateKey privada = keys.getPrivate();
                                    byte[] clavePublica = Base64.encode(publica.getEncoded(), 0);
                                    byte[] clavePrivada = Base64.encode(privada.getEncoded(), 0);

                                    // 3. Enviar los datos
                                    try  {
                                        KeyStore trusted = KeyStore.getInstance("BKS");
                                        InputStream in = getResources().openRawResource(R.raw.keystorebks);
                                        try{
                                            trusted.load(in, "equipo2".toCharArray());
                                        } catch (CertificateException e) {
                                            e.printStackTrace();
                                        } catch (NoSuchAlgorithmException e) {
                                            e.printStackTrace();
                                        } finally{
                                            in.close();
                                        }
                                        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                                        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                                        tmf.init(trusted);
                                        //System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\alber\\Desktop\\PAI5\\SSIIPAI5\\Cliente\\MyApplication\\app\\src\\main\\res\\raw\\keystore.jks");
                                        //System.setProperty("javax.net.ssl.trustStorePassword", "equipo2");
                                        //System.setProperty("javax.net.debug", "all");

                                        // create SSLSocket from factory

                                        Socket socket = new Socket(server, 10000);
                                        Toast.makeText(getApplicationContext(), "ha llegado aqui", Toast.LENGTH_SHORT).show();
                                        // create PrintWriter for sending login to server
                                        PrintWriter output = new PrintWriter(new OutputStreamWriter(
                                                socket.getOutputStream()));


                                        // create BufferedReader for reading server response
                                        BufferedReader input = new BufferedReader(new InputStreamReader(
                                                socket.getInputStream()));
                                        // clean up streams and Socket
                                        String mensaje = numeroSillas + "," +
                                                numeroMesas+ "," +
                                                numeroArmarios+ "," +
                                                numeroEstanterias + "," +
                                                numeroUsuario + "," +
                                                StringPublica + "," +
                                                StringPrivada;




                                        output.println(mensaje);
                                        output.close();
                                        input.close();
                                        socket.close();

                                    } // end try

                                    // handle exception communicating with server
                                    catch (IOException | KeyStoreException | NoSuchAlgorithmException ioException) {
                                        ioException.printStackTrace();
                                        Toast.makeText(getApplicationContext(), ioException.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    Toast.makeText(MainActivity.this, "Mesaje enviado", Toast.LENGTH_SHORT).show();
                                }
                            }


                    )
                    .

                            setNegativeButton(android.R.string.no, null)

                    .

                            show();
        }
    }


}
