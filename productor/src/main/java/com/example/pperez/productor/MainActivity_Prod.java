package com.example.pperez.productor;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;


public class MainActivity_Prod extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity__prod);

        final Button led1=(Button) findViewById(R.id.LED1);
        final Button led2=(Button) findViewById(R.id.LED2);
        final Button led3=(Button) findViewById(R.id.LED3);
        final Button led4=(Button) findViewById(R.id.LED4);
        final SeekBar PotAD=(SeekBar) findViewById(R.id.Potenciometro);

        led1.setOnClickListener(new Button.OnClickListener() {
                                    public void onClick (View arg0) {
                                        led1.setText("ajaja!");
                                    }
                                }
        );
        led2.setOnClickListener(new Button.OnClickListener() {
                                    public void onClick (View arg0) {
                                        led1.setText("ajaja!");
                                    }
                                }
        );
        led3.setOnClickListener(new Button.OnClickListener() {
                                    public void onClick (View arg0) {
                                        led1.setText("ajaja!");
                                    }
                                }
        );
        led4.setOnClickListener(new Button.OnClickListener() {
                                    public void onClick (View arg0) {
                                        led1.setText("ajaja!");
                                    }
                                }
        );
        PotAD.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progressValue;

            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser){

                progressValue = progress;



            }
            public void onStartTrackingTouch(SeekBar seekBar){            }
            public void onStopTrackingTouch(SeekBar seekBar){
                Toast.makeText(getApplicationContext(), "Changing seekbar's progress: "+progressValue, Toast.LENGTH_SHORT).show();}
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity__prod, menu);
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
}
