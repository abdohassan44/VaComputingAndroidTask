package com.example.vacomputingtask;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Calculator mCalculator;

    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            setAddress(mLastLocation);

        }
    };
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    EditText ET_number1, ET_number2, ET_deley;
    String s1, s2, s3;
    double num1, num2;
    Button sum, sub, mul, div;
    String operation;
    int operationId = 0;
    int deley;
    RecyclerView pending, solved;
    EquationAdapter pendingEquationAdapter, slovedEquationAdapter;
    private EquationViewModel equationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCalculator = new Calculator();
        equationViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EquationViewModel.class);
        if(!isMyServiceRunning(CalculationService.class))
        {
           Intent myIntent = new Intent(MainActivity.this, CalculationService.class);
            startService(myIntent);
        }
        init();
    }

    public void init() {
        ET_number1 = findViewById(R.id.num1);
        ET_number2 = findViewById(R.id.num2);
        ET_deley = findViewById(R.id.deley);
        sum = findViewById(R.id.sum);
        sub = findViewById(R.id.sub);
        mul = findViewById(R.id.mul);
        div = findViewById(R.id.div);
        pending = findViewById(R.id.RV_pending);
        solved = findViewById(R.id.RV_solved);

        pendingEquationAdapter = new EquationAdapter(new EquationAdapter.WordDiff());
        slovedEquationAdapter = new EquationAdapter(new EquationAdapter.WordDiff());
        pending.setAdapter(pendingEquationAdapter);
        pending.setLayoutManager(new LinearLayoutManager(this));
        solved.setAdapter(slovedEquationAdapter);
        solved.setLayoutManager(new LinearLayoutManager(this));
        equationViewModel.getAllpendingEquation().observe(this, equations -> {
            // Update the cached copy of the words in the adapter.
            pendingEquationAdapter.submitList(equations);

        });
        equationViewModel.getAllslovedEquation().observe(this, equations -> {
            // Update the cached copy of the words in the adapter.
            slovedEquationAdapter.submitList(equations);

        });
    }

    public boolean getNumbers() {
        s1 = ET_number1.getText().toString();
        s2 = ET_number2.getText().toString();
        s3 = ET_deley.getText().toString();

        if ((s1.equals("") || s2.equals("")) || s3.equals("") || operationId == 0) {
            Toast.makeText(MainActivity.this, "please enter full equation and delay", Toast.LENGTH_LONG).show();
            return false;
        } else {
            num1 = Double.parseDouble(ET_number1.getText().toString());
            num2 = Double.parseDouble(ET_number2.getText().toString());
            deley = Integer.parseInt(ET_deley.getText().toString());
        }

        return true;
    }

    public void doSum(View v) {
        sum.setBackgroundColor(getResources().getColor(R.color.black));
        sub.setBackgroundColor(getResources().getColor(R.color.red));
        mul.setBackgroundColor(getResources().getColor(R.color.red));
        div.setBackgroundColor(getResources().getColor(R.color.red));
        operation = "+";
        operationId = 1;

    }

    public void doSub(View v) {
        sum.setBackgroundColor(getResources().getColor(R.color.red));
        sub.setBackgroundColor(getResources().getColor(R.color.black));
        mul.setBackgroundColor(getResources().getColor(R.color.red));
        div.setBackgroundColor(getResources().getColor(R.color.red));
        operation = "-";
        operationId = 2;

    }

    public void doMul(View v) {
        sum.setBackgroundColor(getResources().getColor(R.color.red));
        sub.setBackgroundColor(getResources().getColor(R.color.red));
        mul.setBackgroundColor(getResources().getColor(R.color.black));
        div.setBackgroundColor(getResources().getColor(R.color.red));
        operation = "*";
        operationId = 3;

    }

    public void doDiv(View v) {
        sum.setBackgroundColor(getResources().getColor(R.color.red));
        sub.setBackgroundColor(getResources().getColor(R.color.red));
        mul.setBackgroundColor(getResources().getColor(R.color.red));
        div.setBackgroundColor(getResources().getColor(R.color.black));
        operation = "/";
        operationId = 4;

    }

    public void calculate(View v) {
        if (getNumbers()) {
            Equation equation = new Equation();
            equation.setDeley(deley);
            equation.setNumber1(num1);
            equation.setNumber2(num2);
            equation.setOperation(operation);
            equation.setOperationId(operationId);
            equation.setStatus(0);
            equation.setId((int) equationViewModel.insert(equation));
            clearData();
            Intent intent = new Intent();
            intent.setAction("sendEquation");
            intent.putExtra("equation", equation);
            sendBroadcast(intent);


        }
    }

    public void clearData() {
        ET_number1.setText("");
        ET_number2.setText("");
        ET_deley.setText("");
        operationId = 0;
        operation = "";
        sum.setBackgroundColor(getResources().getColor(R.color.red));
        sub.setBackgroundColor(getResources().getColor(R.color.red));
        mul.setBackgroundColor(getResources().getColor(R.color.red));
        div.setBackgroundColor(getResources().getColor(R.color.red));
    }

    public void requestLoction(View v) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
    }

    private void setAddress(Location location) {
        Geocoder geocoder;
        TextView textView = findViewById(R.id.TV_location);
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            textView.setVisibility(View.VISIBLE);
            textView.setText(address);// Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {

            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            setAddress(location);
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {

            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}