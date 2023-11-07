package com.example.infiled;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.Manifest;

public class ChooseDeviceActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);

        checkAllPermissions();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        enableBluetooth(bluetoothAdapter);
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
    

    public void onChooseDeviceClick(View view) {
        Intent intent = new Intent(this, ChooseEffectActivity.class);
        startActivity(intent);
    }
}