package com.example.josedaniel.medidorvelocidad;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.bluetooth.*;
import android.widget.TextView;
import android.widget.Toast;

import com.example.josedaniel.medidorvelocidad.Adaptadores.Recycler_View_Adapter;
import com.example.josedaniel.medidorvelocidad.dto.medidorVelocidadDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class ConfigBluetActivity extends AppCompatActivity {

    private int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBluetoothAdapter;
    private ProgressDialog mProgressDlg;
    private int REQUEST_ACCESS_COARSE_LOCATION = 1;


    RecyclerView recyclerView;
    private Recycler_View_Adapter adapter;






    public List<medidorVelocidadDTO> fill_with_data() {

        List<medidorVelocidadDTO> data = new ArrayList<>();
/*
        data.add(new medidorVelocidadDTO("Batman vs Superman", "Following the destruction of Metropolis, Batman embarks on a personal vendetta against Superman ", R.drawable.ic_menu_send));
        data.add(new medidorVelocidadDTO("X-Men: Apocalypse", "X-Men: Apocalypse is an upcoming American superhero film based on the X-Men characters that appear in Marvel Comics ", R.drawable.ic_menu_send));
        data.add(new medidorVelocidadDTO("Captain America: Civil War", "A feud between Captain America and Iron Man leaves the Avengers in turmoil.  ", R.drawable.ic_menu_send));
        data.add(new medidorVelocidadDTO("Kung Fu Panda 3", "After reuniting with his long-lost father, Po  must train a village of pandas", R.drawable.ic_menu_send));
        data.add(new medidorVelocidadDTO("Warcraft", "Fleeing their dying home to colonize another, fearsome orc warriors invade the peaceful realm of Azeroth. ", R.drawable.ic_menu_send));
        data.add(new medidorVelocidadDTO("Alice in Wonderland", "Alice in Wonderland: Through the Looking Glass ", R.drawable.ic_menu_send));
*/
        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {




        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_bluet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProgressDlg 		= new ProgressDialog(this);


        List<medidorVelocidadDTO> data = fill_with_data();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview1);
        adapter = new Recycler_View_Adapter(data, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();
                ObtenerBluetooth();
            }
        });
        mProgressDlg.setMessage("Escaneando...");

        mProgressDlg.setCancelable(false);
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                mBluetoothAdapter.cancelDiscovery();
            }
        });


        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }



        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // Only ask for these permissions on runtime when running Android 6.0 or higher
            switch (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                case PackageManager.PERMISSION_DENIED:
                    ((TextView) new AlertDialog.Builder(this)
                            .setTitle("Se necesitan permisos")
                            .setMessage(Html.fromHtml("<p>Para encontrar dispositivos bluetooth cercanos, haga clic  " +
                                    " \"Permitir\" En la ventana emergente.</p>" +
                                    "<p>para información <a href=\"http://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-hardware-id\">aqui</a>.</p>"))
                            .setNeutralButton("Permitir", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(ConfigBluetActivity.this  ,
                                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                                REQUEST_ACCESS_COARSE_LOCATION);
                                    }
                                }
                            })
                            .show()
                            .findViewById(android.R.id.message))
                            .setMovementMethod(LinkMovementMethod.getInstance());       // Make the link clickable. Needs to be called after show(), in order to generate hyperlinks
                    break;
                case PackageManager.PERMISSION_GRANTED:
                    break;
            }
        }


        consultarDispositivosEmparejados();

    }

    private void consultarDispositivosEmparejados() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
// If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
               //showMessage(device.getName() + "\n" + device.getAddress());
                adapter.insert(0, new medidorVelocidadDTO(device.getName(), device.getAddress(), R.drawable.ic_menu_share));
            }
        }
    }

    public void ObtenerBluetooth() {



      //  pairDevice();



        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();

        } else {

        /*    int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);*/



            if (mBluetoothAdapter.startDiscovery()) {
               // Toast.makeText(this, "S", Toast.LENGTH_LONG).show();
            }
        }

    }

    private  void showMessage(String context) {

        Toast.makeText(this, "" + context, Toast.LENGTH_LONG).show();

    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                   // showToast("Enabled");

                   // showEnabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
               // mDeviceList = new ArrayList<BluetoothDevice>();

                mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mProgressDlg.dismiss();

               // Intent newIntent = new Intent(MainActivity.this, DeviceListActivity.class);
               // mBluetoothAdapter.startDiscovery();
               // newIntent.putParcelableArrayListExtra("device.list", mDeviceList);

               // startActivity(newIntent);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

             //   mDeviceList.add(device);

               // showMessage("Se encontro dispositivo  " + device.getName());
                adapter.insert(0, new medidorVelocidadDTO(device.getName(), device.getAddress(), R.drawable.ic_menu_share));
            }
        }
    };

    @Override
    protected void onDestroy() {

        mBluetoothAdapter.cancelDiscovery();
        unregisterReceiver(mReceiver);

        super.onDestroy();


    }

    public void pairDevice(final BluetoothDevice device) {

        String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
        Intent intent = new Intent(ACTION_PAIRING_REQUEST);
        String EXTRA_DEVICE = "android.bluetooth.device.extra.DEVICE";
        intent.putExtra(EXTRA_DEVICE, device);
        String EXTRA_PAIRING_VARIANT = "android.bluetooth.device.extra.PAIRING_VARIANT";
        int PAIRING_VARIANT_PIN = 0;
        intent.putExtra(EXTRA_PAIRING_VARIANT, PAIRING_VARIANT_PIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}





