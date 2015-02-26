package com.example.pperez.myapplicationgrafica2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

  //  int [] buffer=new int[1024];
    int puntero_vector=0;
    Canvas grafica;
    Paint paint;

    void imprimir(String cad)
    {
        TextView ll = (TextView) findViewById(R.id.TextViewEstado);
        ll.append(cad);
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

        imprimir("Hola Chacho");


        /* crear un hilo para registrarse */
        new Thread(new Runnable() {
            public void run() {

                imprimir("Hilo de Registro");
                if (BuildConfig.DEBUG) {
                    Log.d("per.com", "Hilo de registro");
                }

               /* String FLOW_SERVER_URL="http://ws-uat.flowworld.com";
                String FLOW_SERVER_KEY="Ph3bY5kkU4P6vmtT";
                String FLOW_SERVER_SECRET="Sd1SVBfYtGfQvUCR";

                String hardwareType 			= "Android";
                String macAddress 				= "446D6CDAE96C";
                String serialNumber 			= "40";
                String deviceId 				="";
                String softwareVersion 			= "40";
                String deviceName 				= "XXPascualetAndroid";
                String DeviceRegistrationKey 	= "NXAYIMLCGH";
                if (BuildConfig.DEBUG) {
                    Log.d("per.com", "Pascual está aquí");
                }
*/
                boolean result = false;
                return;

                /*result = initFlowCore();
                if (result) {
                    result = setServerDetails(server, oAuth, secret);
                }
                if (result) {
                    result = registerDevice(hardwareType, macAddress, serialNumber,deviceId , softwareVersion, deviceName, DeviceRegistrationKey);
                }

                if (result) {
                    System.out.println("Device registration succeeded");
                } else {
                    System.out.println("Device registration failed");
                }
                if (result) {
                    result = getDeviceSettings();
                }

                if (result) {
                    System.out.println("Settings retrieval succeeded");
                } else {
                    System.out.println("Settings retrieval failed");
                }
                if (result) {
                    result = getDeviceAoR();
                }

                if (result) {
                    System.out.println("AoR retrieval succeeded");
                } else {
                    System.out.println("AoR retrieval failed");
                }

*/
                //System.out.println("Fin del Hilo");


            }
        });
        /* crear un hilo que invoque a nuevamuestra para representar */
       /* new Thread(new Runnable() {
            public void run() {
                imprimir("Hilo de dibujo Arrancado");
                if (BuildConfig.DEBUG) {
                    Log.d("per.com", "Hilo de creación de muestras");
                }

                    nuevamuestra(45);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
*/


    }

  /*   void nuevamuestra(int dato)
     {
         //buffer[puntero_vector++]=dato;
         if (puntero_vector==1024) puntero_vector=0;

         grafica.drawRect(puntero_vector, 50, 10,10, paint);


     }
*/

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
/*
    boolean initFlowCore() {




        boolean result = false;

        try {
            result = Core.init();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            System.out.println("FlowCore Init failed");
        }
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
            System.out.println("FlowCore setServer failed");
        }
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
            result=true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }

        return result;
    }

    boolean getDeviceSettings() {

        boolean result = true;
        try {
            Device device = Core.getDefaultClient().getLoggedInDevice();
            if (device.hasSettings()) {
                Settings deviceSettings = device.getSettings();
                System.out.println("\n--Device settings list\n");
                for (int index = 0; index < deviceSettings.size(); index++) {
                    Setting setting = deviceSettings.get(index);
                    if (setting.hasKey() && setting.hasValue()) {
                        String key = setting.getKey();
                        String value = setting.getValue();
                        System.out.println(index+": Key = "+key+"  Value = "+value);
                    }
                }
            } else {
                System.out.println("Device has not settings root");
                result = false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
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
                    System.out.println("Device AoR: "+ aor);
                } else {
                    result = false;
                }
            } else {
                System.out.println("Device has no AoR");
                result = false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        return result;
    }

*/
}
