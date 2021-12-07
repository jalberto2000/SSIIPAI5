package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


public class MainActivity extends AppCompatActivity {

    // Setup Server information
    protected static String server = "192.168.0.109";
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
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {




                                // Catch ok button and send information
                                public void onClick(DialogInterface dialog, int whichButton) {


                                    // 1. Extraer los datos de la vista
                                    EditText sillas = (EditText) findViewById(R.id.cantidadSillas);
                                    String numeroSillas = sillas.getText().toString();

                                    // 2. Firmar los datos

                                    // 3. Enviar los datos
                                    try  {
                                        //System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\alber\\Desktop\\PAI5\\SSIIPAI5\\Cliente\\MyApplication\\app\\src\\main\\res\\raw\\keystore.jks");
                                        //System.setProperty("javax.net.ssl.trustStorePassword", "equipo2");
                                        //System.setProperty("javax.net.debug", "all");

                                        // create SSLSocket from factory
                                        SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                                        SSLSocket socket = (SSLSocket) socketFactory.createSocket(server, 10000);
                                        Toast.makeText(getApplicationContext(), "ha llegado aqui", Toast.LENGTH_SHORT).show();
                                        System.out.println(socket.getSession().getProtocol());
                                        // create PrintWriter for sending login to server
                                        PrintWriter output = new PrintWriter(new OutputStreamWriter(
                                                socket.getOutputStream()));

                                        String mensaje = numeroSillas;
                                        // create BufferedReader for reading server response
                                        BufferedReader input = new BufferedReader(new InputStreamReader(
                                                socket.getInputStream()));
                                        String respuesta = input.readLine();
                                        // clean up streams and Socket
                                        output.close();
                                        input.close();
                                        socket.close();

                                    } // end try

                                    // handle exception communicating with server
                                    catch (IOException ioException) {
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
