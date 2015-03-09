package com.example.pperez.myapplicationgrafica2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.imgtec.flow.client.core.Client;
import com.imgtec.flow.client.core.Core;
import com.imgtec.flow.client.flowmessaging.FlowMessagingAddress;
import com.imgtec.flow.client.users.Device;


public class MainActivity extends ActionBarActivity {

      int [] buffer=new int[1024];
    int puntero_vector=0;
    Canvas grafica;
    Paint paint;

    static {
        System.loadLibrary("flowcore");
        System.loadLibrary("flowsip");
        System.loadLibrary("flow");

    }

    synchronized void imprimir(final String cad) {
        final String g = cad;

        runOnUiThread(new Runnable() {
            public void run() {
                TextView ll = (TextView) findViewById(R.id.TextViewEstado);
                ll.append(cad);
                ScrollView ll1 = (ScrollView) findViewById(R.id.ScrollViewEstado);
                ll1.fullScroll(View.FOCUS_DOWN);

            }
        });
    }
    void imprimirln(final String cad) {
        imprimir(cad+"\r\n");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* crea el canvas para poder dibujar la gráfica */

        paint = new Paint();
        paint.setColor(Color.parseColor("#CD5C5C"));
        Bitmap bg = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
        grafica = new Canvas(bg);
        FrameLayout ll = (FrameLayout) findViewById(R.id.Grafica);
        ll.setBackgroundDrawable(new BitmapDrawable(bg));


        // imprimir("Hola Cha");


        // crear un hilo para registrarse
        new Thread(new Runnable() {
            public void run() {

                   /* registro1*/

                String FLOW_SERVER_URL="http://ws-uat.flowworld.com";
                String FLOW_SERVER_KEY="Ph3bY5kkU4P6vmtT";
                String FLOW_SERVER_SECRET="Sd1SVBfYtGfQvUCR";

                String hardwareType 			= "Android";
                String macAddress 				= "446D6CDAE96C";
                String serialNumber 			= "40";
                String deviceId 				="";
                String softwareVersion 			= "40";
                String deviceName 				= "XXPascualetAndroid";
                String DeviceRegistrationKey 	= "NXAYIMLCGH";

                  boolean result = false;

                result = initFlowCore();
                if (result) {
                    result = setServerDetails(FLOW_SERVER_URL, FLOW_SERVER_KEY, FLOW_SERVER_SECRET);
                }

                if (result) {
                    result = registerDevice(hardwareType, macAddress, serialNumber,deviceId , softwareVersion, deviceName, DeviceRegistrationKey);
                }


                if (result) {
                    result = getDeviceAoR();
                }



            }
        }
        ).start();




        // crear un hilo que invoque a nuevamuestra para representar
        new Thread(new Runnable() {
            public void run() {int x=0;
                imprimir("Hilo de dibujo Arrancado\r\n");

                while(true){ nuevamuestra(x+45);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                x++;
                }
            }
        }).start();

    }


     public void nuevamuestra(int dato)
     {
         buffer[puntero_vector++]=dato;
         if (puntero_vector==1024) puntero_vector=0;

         grafica.drawRect(puntero_vector, dato, puntero_vector+10,dato+10, paint);

         imprimirln("nueva muestra "+puntero_vector+","+dato);

         runOnUiThread(new Runnable() {
             public void run() {
                 FrameLayout ll = (FrameLayout) findViewById(R.id.Grafica);

                 ll.invalidate();

             }
         });





     }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /// Flow core

    boolean initFlowCore() {




        boolean result = false;

        try {
            result = Core.init();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            imprimirln("FlowCore Init failed");
        }
        else
            imprimirln("FlowCore Init Success");

        return result;
    }


    boolean setServerDetails(String server, String oAuth, String secret) {

        boolean result = false;

        try {
            Client cli = Core.getDefaultClient();
            cli.setServer(server, oAuth, secret);
            result = true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            imprimirln("FlowCore setServer failed");
        }
        else
            imprimirln("FlowCore setServer works!");

        return result;
    }

    boolean registerDevice(String hardwareType, String macAddress, String serialNumber,
                           String deviceId, String softwareVersion,
                           String deviceName, String DeviceRegistrationKey) {

        boolean result = false;

        try {
            Client cli = Core.getDefaultClient();
            cli.loginAsDevice(hardwareType, macAddress, serialNumber, deviceId, softwareVersion, deviceName, DeviceRegistrationKey);
            cli.getLoggedInDevice();
            imprimirln("Device registered");
            result=true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            imprimirln("Error in Device registration");
            result = false;
        }

        return result;
    }



    boolean getDeviceAoR() {

        boolean result = true;
        try {
            Device device = Core.getDefaultClient().getLoggedInDevice();
            if (device != null && device.hasFlowMessagingAddress()) {
                FlowMessagingAddress addr = device.getFlowMessagingAddress();
                if (addr != null) {
                    String aor = addr.getAddress();
                    imprimirln("Device AoR: "+ aor);
                } else {
                    result = false;
                }
            } else {
                imprimirln("Device has no AoR");
                result = false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        return result;
    }


}
