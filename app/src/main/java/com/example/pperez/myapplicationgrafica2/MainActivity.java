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

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    MainActivity myself;
    FlowCloud fc;


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
    public void imprimirln(final String cad) {
        imprimir(cad+"\r\n");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myself=this;


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
                String macAddress 				= "0092C90943B1";
                String serialNumber 			= "59";
                String deviceId 				="";
                String softwareVersion 			= "41";
                String deviceName 				= "TabletaA13";
                String DeviceRegistrationKey 	= "GPMWB7QJ6H";

                  boolean result = false;


                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String cad =sdf.format(new Date());
                fc.imprimirln("Ejecución: "+cad);



                fc=new FlowCloud(myself);




                result = fc.initFlowCore();
                if (result) {
                    result = fc.setServerDetails(FLOW_SERVER_URL, FLOW_SERVER_KEY, FLOW_SERVER_SECRET);
                }

                if (result) {
                    result = fc.registerDevice(hardwareType, macAddress, serialNumber,deviceId , softwareVersion, deviceName, DeviceRegistrationKey);
                }

                if (result) {
                    result = fc.getFlowAoROfUser();
                }

                if (result) {
                    result = fc.getDeviceAoR();
                }

                if (result)
                {
                    result=fc.createDeviceDataStore("almacenamiento_1");
                }

                if (result) {
                    result=fc.createDataStore("ALMACENAMIENTO_USUARIO");
                }

                if (result) {
                    result = fc.getDeviceSettings();
                }

                if (result) {
                    fc.imprimirln("Settings retrieval succeeded");
                } else {
                    fc.imprimirln("Settings retrieval failed");
                }



                if (result) {
                    result =fc. addSettingToDevice("CLAVE", "SOL"); //key, value
                }

                if (result) {
                    result = fc.getDeviceSettings();
                }

                if (result) {
                    fc.imprimirln("Settings retrieval succeeded");
                } else {
                    fc.imprimirln("Settings retrieval failed");
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
                fc.imprimirln("Hilo de dibujo Arrancado\r\n");

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



}
