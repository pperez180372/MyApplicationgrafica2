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
import com.imgtec.flow.client.users.DataStore;
import com.imgtec.flow.client.users.Device;
import com.imgtec.flow.client.users.User;
import com.imgtec.flow.client.core.Setting;
import com.imgtec.flow.client.core.Settings;
import com.imgtec.flow.client.core.SettingsHelper;
import com.imgtec.flow.client.core.SettingHelper;




import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

      float [] buffer=new float[3600];
    int puntero_vector=0;
    Canvas grafica_1;
    Canvas grafica_2;
    int canvas_visible;


    Paint paintred;
    Paint paintwhite;
    BitmapDrawable BitmapD_1;
    BitmapDrawable BitmapD_2;


    int mx,Mx,my,My;

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

        paintred = new Paint();
        paintred.setColor(Color.parseColor("#CD5C5C"));
        paintwhite = new Paint();
        paintwhite.setColor(Color.parseColor("#FFFFFF"));


        Bitmap bg_1 = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
        Bitmap bg_2 = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
        grafica_1 = new Canvas(bg_1);
        grafica_2 = new Canvas(bg_2);
        BitmapD_1=new BitmapDrawable(getResources(),bg_1);
        BitmapD_2=new BitmapDrawable(getResources(),bg_2);
        canvas_visible=1;
        FrameLayout ll = (FrameLayout) findViewById(R.id.Grafica);
        ll.setBackgroundDrawable(BitmapD_1);


        my=15; // temperatura minima;
        My=40; //temperatura máxima;
        mx=0;  // tiempo minimo; en segundos // dependerá del numero de muestras
        Mx=3600;

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


                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String cad =sdf.format(new Date());
                imprimirln("Ejecución: "+cad);

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

                if (result)
                {
                    result=createDeviceDataStore("almacenamiento_1");
                }

                if (result) {
                    result=createDataStore("ALMACENAMIENTO_USUARIO");
                }

                if (result) {
                    result = getDeviceSettings();
                }

                if (result) {
                    imprimirln("Settings retrieval succeeded");
                } else {
                    imprimirln("Settings retrieval failed");
                }



                if (result) {
                    result = DevicestoreKeyValue("CLAVE", "SOL"); //key, value
                }

                if (result) {
                    result = getDeviceSettings();
                }

                if (result) {
                    imprimirln("Settings retrieval succeeded");
                } else {
                    imprimirln("Settings retrieval failed");
                }
                /*if (result) {
                    result = removeKeyValueEntry(key);
                }*/


            }
        }
        ).start();

        // crear un hilo que invoque a nuevamuestra para representar
        new Thread(new Runnable() {
            public void run() {int x=0;
                imprimir("Hilo de dibujo Arrancado\r\n");

                while(true){ nuevamuestra((float) (Math.random()*10.0+25.0+10.0*Math.sin(2.0*Math.PI*(float)(x%360)/360.0)));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                x++;
                }
            }
        }).start();
    }


     public void nuevamuestra(float dato)
     {
  /*       int w,h;


         /*FrameLayout ll = (FrameLayout) findViewById(R.id.Grafica);
         w=ll.getWidth();
         h=ll.getHeight();
*/

         Canvas grafica;
         canvas_visible=(canvas_visible+1)%2;
            if (canvas_visible==1)
               grafica=grafica_1;
            else
                grafica=grafica_2;
            // borrar esta escalado.
         grafica.drawRect(0, 0, 480, 800, paintwhite);
         float sy= (float) (800.0/(My-my)); // en grados
         float sx=(float) 480.0/(Mx-mx);   // en segundos

         //añadir dato
         buffer[puntero_vector++]=dato;
         if ((puntero_vector>=3600))
         {
                //imprimirln("Ha finalidado el la peusta de datos");
                puntero_vector=0;
         }

      //   if ((puntero_vector%10)==0)
         {
             // grafica de lineas no se escala la x
         int t;
         for (t=0;t<3600-1;t++)
         {
            float va,va1;
             va=buffer[t];
             if (va>My) va=My;
             if (va<my) va=my;
             va1=buffer[t+1];
             if (va1>My) va1=My;
             if (va1<my) va1=my;
             float x1,y1,x2,y2;

             x1=t*sx;
             y1=800-(va-my)*sy;
             x2=(t+1)*sx;
             y2=800-(va1-my)*sy;

             grafica.drawLine(x1,y1,x2,y2, paintred);

         }

              if (canvas_visible == 1) {
                  runOnUiThread(new Runnable() {
                      public void run() {
                          FrameLayout ll = (FrameLayout) findViewById(R.id.Grafica);
                          ll.setBackgroundDrawable(BitmapD_1);
                          // ll.postInvalidate();

                      }
                  });

              } else {
                  runOnUiThread(new Runnable() {
                      public void run() {
                          FrameLayout ll = (FrameLayout) findViewById(R.id.Grafica);
                          ll.setBackgroundDrawable(BitmapD_2);

//                     ll.postInvalidate();

                      }
                  });
              }
          }



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



    public boolean createDeviceDataStore(String newDataStoreName) {

        boolean result = false;

        try {
            Device device = Core.getDefaultClient().getLoggedInDevice();
            String dataStoreName = newDataStoreName;
            DataStore dataStore = device.getDataStore(dataStoreName);
            if (dataStore != null) {
                imprimirln("La base de datos: \""+newDataStoreName+"\" se ha creado con exito");

                result = true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());


            result = false;
        }

        if (!result) {
            imprimirln("La base de datos: \""+newDataStoreName+"\" NO se ha podido crear");
        }
        return result;
    }



    public boolean createDataStore(String dataStoreName) {

        boolean result = false;

        try {
            User user = Core.getDefaultClient().getLoggedInUser();
            DataStore myDataStore= user.getDataStore(dataStoreName);
            if (myDataStore != null){
                result = true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }

        if (!result) {
            imprimirln("La base de datos de USUARIO: \""+dataStoreName+"\" NO se ha podido crear");
        }
        else
            imprimirln("La base de datos de USUARIO: \""+dataStoreName+"\" se ha creado con exito");
        return result;
    }


    public  boolean getDeviceSettings() {

        boolean result = true;
        try {
            Device device = Core.getDefaultClient().getLoggedInDevice();
            if (device.hasSettings()) {
                Settings deviceSettings = device.getSettings();
                imprimirln("\n--Device settings list\n");
                for (int index = 0; index < deviceSettings.size(); index++) {
                    Setting setting = deviceSettings.get(index);
                    if (setting.hasKey() && setting.hasValue()) {
                        String key = setting.getKey();
                        String value = setting.getValue();
                        System.out.println(index+": Key = "+key+"  Value = "+value);
                    }
                }
            } else {
                imprimirln("Device has not settings root");
                result = false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        return result;
    }

    public boolean DevicestoreKeyValue(String key, String value) {

        boolean result = true;
        try {
            Device device = Core.getDefaultClient().getLoggedInDevice();
            if (device.hasSettings()) {
                Settings deviceSettings = device.getSettings();
                imprimirln("\n--Device settings list\n");

                Setting newEntry = SettingHelper.newSetting(Core.getDefaultClient());
                newEntry.setKey(key);
                newEntry.setValue(value);

                Settings newSettings = SettingsHelper.newSettings(Core.getDefaultClient());
                newSettings.add(newEntry);

                device.getSettings().doAdd(newSettings);
            } else {
                imprimirln("Device has not settings");
                result = false;
            }
        } catch (Exception e) {
            System.err.println("EXCEPTION PASUCLA");
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            imprimirln("FlowCore storing key-value failed");
        }
        return result;

    }


    public boolean storeKeyValue(String key, String value) {

        boolean result = true;
        try {
            User user = Core.getDefaultClient().getLoggedInUser();
            if (user.hasSettings()) {
                Setting newEntry = SettingHelper.newSetting(Core.getDefaultClient());
                newEntry.setKey(key);
                newEntry.setValue(value);

                Settings newSettings = SettingsHelper.newSettings(Core.getDefaultClient());
                newSettings.add(newEntry);

                user.getSettings().doAdd(newSettings);
            } else {
                imprimirln("User has not settings");
                result = false;
            }
        } catch (Exception e) {
            System.err.println("EXCEPTION PASUCLA");
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            imprimirln("FlowCore storing key-value failed");
        }
        return result;

    }


     public boolean retrieveKeyValue(String key) {

        boolean result = true;
        try {
            User user = Core.getDefaultClient().getLoggedInUser();
            if (user.hasSettings()) {
                Setting setting = user.getSetting(key);
                String value = setting.getValue();
                imprimirln("Retrieved Value: "+value);
            } else {
                imprimirln("User has no settings");
                result = false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            imprimirln("FlowCore retrieval of key-value failed");
        }
        return result;

    }


    public boolean removeKeyValueEntry(String key){

        boolean result = true;
        try {
            User user = Core.getDefaultClient().getLoggedInUser();
            if (user.hasSettings()) {
                Settings userSettings = user.getSettings();
                userSettings.removeSetting(key);
            } else {
                imprimirln("ERROR: User has no settings root");
                result = false;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            result = false;
        }
        if (!result) {
            imprimirln("FlowCore key-value deletion failed");
        }
        return result;

    }




}
