package com.example.josedaniel.medidorvelocidad;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ConfigBasicaActivity extends AppCompatActivity {

    private TextView txtmasa;
    private  TextView txtlongitud;
    private  TextView txtdistancia;
    public static final String PREFS_NAME = "configuracionbasica";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_basica);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String masa = settings.getString("masa","");
        String longirutd =settings.getString("longitud","");
        String distancia = settings.getString("distancia","");

        //setSilent(silent);


        txtdistancia= (TextView)findViewById(R.id.txtdistancia);
        txtlongitud = (TextView)findViewById(R.id.txtlongitud);
        txtmasa = (TextView)findViewById(R.id.txtmasa);


        txtdistancia.setText(distancia);
        txtmasa.setText(masa);
        txtlongitud.setText(longirutd);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String masa;
                String longitud;
                String distancia;

                masa = txtmasa.getText().toString();
                longitud = txtlongitud.getText().toString();
                distancia = txtdistancia.getText().toString();

                if ( masa.equals("")  || longitud.equals("") || distancia.equals("") )
                {

                    Toast.makeText(getApplication(),"Todos los campos deben ser diligenciados",Toast.LENGTH_LONG).show();

                }
                else
                {

                    // guado en preferenciaz
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putString("masa", masa);
                    editor.putString("longitud", longitud);
                    editor.putString("distancia", distancia);

                    // Commit the edits!
                    if (editor.commit())
                    {
                        Toast.makeText(getApplication(),"Configuración Aceptada",Toast.LENGTH_LONG).show();
                    }

                }


            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
