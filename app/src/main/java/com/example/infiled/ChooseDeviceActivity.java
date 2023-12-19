package com.example.infiled;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.Manifest;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChooseDeviceActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> availableInfiLedDevicesList = new ArrayList<>();
    String[] availableInfiLedDevicesStrings = new String[3];

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private BluetoothDevice targetDevice;
    private BluetoothSocket bluetoothSocket;
    private UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    private final BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(checkCustomPermission(Manifest.permission.BLUETOOTH_CONNECT) && device.getName() != null)
                    if(device.getName().startsWith(getString(R.string.app_name))) {
                        Log.d(TAG, "Found device: " + device.getName() + " - " + device.getAddress());
                        availableInfiLedDevicesList.add(device);
                        availableInfiLedDevicesStrings[0] = availableInfiLedDevicesList.get(0).getName();
                        updateListOfDevices();
                    }
                // You can add the discovered device to a list or adapter for display.
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);

        Button reloadButton = findViewById(R.id.reloadButton);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReloadButtonClick();
            }
        });

        textView1 = findViewById(R.id.textViewDevice1);
        textView2 = findViewById(R.id.textViewDevice2);
        textView3 = findViewById(R.id.textViewDevice3);

        checkAllPermissions();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        enableBluetooth(bluetoothAdapter);

        discoverDevices(bluetoothAdapter);
    }

    private void onReloadButtonClick(){
        Log.d(TAG, "Reload bluetooth devices list requested!");
        availableInfiLedDevicesList.clear();


        deleteListOfDevices();
        discoverDevices(bluetoothAdapter);
    }

    private void deleteListOfDevices(){
        textView1.setText(" ");
        textView2.setText(" ");
        textView3.setText(" ");

        availableInfiLedDevicesStrings[0] = " ";
        availableInfiLedDevicesStrings[1] = " ";
        availableInfiLedDevicesStrings[2] = " ";

    }

    private void updateListOfDevices(){
        textView1.setText(availableInfiLedDevicesStrings[0]);
    }


    private void discoverDevices(BluetoothAdapter bluetoothAdapter) {
        if(checkCustomPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
        }

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryReceiver, filter);

        // Start discovery.
        bluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the broadcast receiver when the activity is destroyed.
        unregisterReceiver(discoveryReceiver);
    }

    private void enableBluetooth(BluetoothAdapter bluetoothAdapter){
        if(bluetoothAdapter == null){
            System.out.println("Problem with setting up Bluetooth!");
        }
        else{
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                System.out.println("Bluetooth was enabled successfully");
                            } else {
                                System.out.println("Bluetooth enabling failed.");
                            }
                        });
                launcher.launch(enableBtIntent);
            }
            else{
                System.out.println("Bluetooth already enabled");
            }
        }
    }

    private boolean checkCustomPermission(String permission){
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }


    private void checkAllPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            System.out.println("Permissions granting requested!");

            // Request permissions
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
        }
        else{
            System.out.println("Permissions already granted!");
        }
    }

    public void onDevice1Click(View view){
        BluetoothDevice device = availableInfiLedDevicesList.get(0);
        new ConnectThread(device).start();
    }

    public void onDevice2Click(View view){

    }

    public void onDevice3Click(View view){

    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                if(checkCustomPermission(Manifest.permission.BLUETOOTH_CONNECT))
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            if(checkCustomPermission(Manifest.permission.BLUETOOTH_SCAN))
                bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();

            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            Log.d(TAG, "Connection attempt succeeded!");
            runOnUiThread(() -> {
                // Move to the ChooseEffectActivity after successful connection.
                Intent intent = new Intent(ChooseDeviceActivity.this, ChooseEffectActivity.class);
                startActivity(intent);

                // Close the current activity (optional, depending on your requirements).
                finish();
            });
            //manageMyConnectedSocket(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    public void onChooseDeviceClick(View view) {
        Intent intent = new Intent(this, ChooseEffectActivity.class);
        startActivity(intent);
    }
}