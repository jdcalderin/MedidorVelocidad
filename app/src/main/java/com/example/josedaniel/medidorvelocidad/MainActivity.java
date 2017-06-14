package com.example.josedaniel.medidorvelocidad;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.anastr.speedviewlib.base.Speedometer;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SensorEventListener {

    BluetoothAdapter adaptdorBlue;
    private SensorManager mSensorManager;
    private  boolean BANDERA_SENSOR = false;
    public static final String PREFS_NAME = "configuracionbasica";
    private static Date  tinicial = new Date();
    private static Date  tfinal  = new Date()  ;
    private static String masa = "";
    private static String  longirutd ="";
    private static String  distancia = "";
    Speedometer speedometer;


    private Sensor mSensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.tinicial = new Date();
        this.tfinal = new Date();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                     //   .setAction("Action", null).show();
                if (BANDERA_SENSOR)
                {
                    BANDERA_SENSOR = false;
                    Toast.makeText(getApplication(), "El sensor se desactivo: ", Toast.LENGTH_LONG).show();
                    speedometer.speedTo(0);
                }
                else
                {
                    BANDERA_SENSOR = true   ;
                    Toast.makeText(getApplication(), "El sensor se activo: ", Toast.LENGTH_LONG).show();
                    speedometer.speedTo(0);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        adaptdorBlue = BluetoothAdapter.getDefaultAdapter();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // obtener informacion basica

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String masa = settings.getString("masa","");
        String longirutd =settings.getString("longitud","");
        String distancia = settings.getString("distancia","");

        speedometer = (Speedometer)findViewById(R.id.speedView);

        speedometer.speedTo(0);

        this.masa  = masa;
        this.longirutd = longirutd;
        this.distancia = distancia;
       // Toast.makeText(this, "masa: " + masa +"\n" , Toast.LENGTH_LONG).show();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, ConfiguracionActivity.class);
            String message = "";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    public  void bluethod_control (View v)
    {
        int REQUEST_ENABLE_BT = 1;
        if(!adaptdorBlue.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);

        }

    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onResume() {

        super.onResume();
        this.tinicial = new Date();
        this.tfinal = new Date();

        mSensorManager.registerListener(this, mSensor,
        SensorManager.SENSOR_DELAY_NORMAL);

    }
    @Override
    protected void onPause() {

        super.onPause();
    this.tinicial = new Date();
        this.tfinal = new Date();

      mSensorManager.unregisterListener(this);

    }


    public void onSensorChanged(SensorEvent event) {
        // Date tinicial  ;
       // Date tfinal  ;
        Calendar c = Calendar.getInstance();
       // int seconds = c.get(Calendar.SECOND);






        if (BANDERA_SENSOR == true) {
            if (event.values[0] == 0) {

               // oast.makeText(this, "Sensor activo", Toast.LENGTH_LONG).show();
                tinicial = c.getTime();
               // Toast.makeText(this, "Sensor activo" +  c.getTime() , Toast.LENGTH_LONG).show();

            } else {

                tfinal = c.getTime();
                DecimalFormat df = new DecimalFormat("#.####");
                long diffInMs = tfinal.getTime() - tinicial.getTime();
                double seconds = diffInMs / 1000.0;
                Double velocidad =  (Double)Double.parseDouble(longirutd)/seconds;


                speedometer.speedTo(Float.parseFloat(velocidad.toString()) );
                        //long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
                Toast.makeText(this, "la velocidad es  " + String.format("%.2f", velocidad)  , Toast.LENGTH_LONG).show();

            }
        }
    }

}
