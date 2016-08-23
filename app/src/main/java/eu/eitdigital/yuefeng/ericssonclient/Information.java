package eu.eitdigital.yuefeng.ericssonclient;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;

public class Information extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LocationManager locationManager;
    private int postCheck = 0;
    private double eventLatitude=59.347;
    private double eventLongitude=18.069;
    private double eventRange=0.01;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Firebase.setAndroidContext(this);
        final Firebase firebase = new Firebase("https://countplat.firebaseio.com/");
        sharedPreferences = this.getSharedPreferences("UserInfo", MODE_WORLD_READABLE);
        editor = sharedPreferences.edit();
        Button button = (Button) findViewById(R.id.button);
        Button resetProfile = (Button) findViewById(R.id.resetProfile);
        final TextView locationStatus;
        locationStatus = (TextView) findViewById(R.id.locationStatus);
        final EditText editText = (EditText) findViewById(R.id.interviewerID);
        final String name = sharedPreferences.getString("name", "Yuefeng");
        final String phone = sharedPreferences.getString("phone", "0760879055");
        final String university = sharedPreferences.getString("university", "KTH");
        final String program = sharedPreferences.getString("program", "Embedded Systems");
        final String graduation = sharedPreferences.getString("graduation", "2017");
        final String email = sharedPreferences.getString("email", "yuefeng@kth.se");
        final TextView textView = (TextView) findViewById(R.id.information);
        final TextView locationIndicator = (TextView) findViewById(R.id.locationIndicator);
        final TextView visitorInfo = (TextView) findViewById(R.id.numberOfVisitors);
        textView.setText(name + '\n' + phone + '\n' + university + '\n' + program + '\n' + graduation + '\n' + email);
        final int[] numberOfVisitors = new int[1];
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location!=null)
        {
            locationIndicator.setText("Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());
            if (abs(location.getLatitude() - eventLatitude) < eventRange && abs(location.getLongitude() - eventLongitude) < eventRange) {

                if (postCheck==0)
                {
                    //Toast.makeText(Information.this,"push_id is empty, in event!",Toast.LENGTH_SHORT).show();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());
                    Map<String, String> post = new HashMap<String, String>();
                /*Map<String, String> count = new HashMap<String, String>();
                count.put("name",name);
                Firebase numCount = firebase.child("nameOfVisitors").push();
                numCount.setValue(count);
                editor.putString("count_id",numCount.getKey());*/
                    post.put("name", name);
                    post.put("phone", phone);
                    post.put("university", university);
                    post.put("program", program);
                    post.put("graduation", graduation);
                    post.put("email", email);
                    post.put("time", formatter.format(curDate));
                    Firebase newPostRef = firebase.child("visitors").push();
                    newPostRef.setValue(post);
                    editor.putString("push_id", newPostRef.getKey());
                    editor.commit();
                    locationStatus.setText("You are now in the event! " + postCheck);
                    numberOfVisitors[0]=numberOfVisitors[0]+1;
                    firebase.child("numberOfVisitors").setValue(numberOfVisitors[0]);
                    postCheck = 1;
                }
                else
                {

                    //Toast.makeText(Information.this,"push_id is not empty, in event!",Toast.LENGTH_SHORT).show();
                    locationStatus.setText("You are now in the event! " + postCheck);
                }

            }
            else
            {
                numberOfVisitors[0]=numberOfVisitors[0]-1;
                firebase.child("numberOfVisitors").setValue(numberOfVisitors[0]);
            /*firebase.child("nameOfVisitors").child(sharedPreferences.getString("count_id","")).removeValue();
            editor.remove("count_id");*/
                locationStatus.setText("You are NOT in the event " + postCheck);
            /*editor.commit();*/
                postCheck=0;
            }
        }
        else
        {
            locationStatus.setText("Location is not available!");
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 8, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationIndicator.setText("Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());

                if (abs(location.getLatitude() - eventLatitude) < eventRange && abs(location.getLongitude() - eventLongitude) < eventRange) {

                    if (postCheck==0)
                    {
                        //Toast.makeText(Information.this,"push_id is empty, in event!",Toast.LENGTH_SHORT).show();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date curDate = new Date(System.currentTimeMillis());
                        Map<String, String> post = new HashMap<String, String>();
                        /*Map<String, String> count = new HashMap<String, String>();
                        count.put("name",name);
                        Firebase numCount = firebase.child("nameOfVisitors").push();
                        numCount.setValue(count);
                        editor.putString("count_id",numCount.getKey());*/
                        post.put("name", name);
                        post.put("phone", phone);
                        post.put("university", university);
                        post.put("program", program);
                        post.put("graduation", graduation);
                        post.put("email", email);
                        post.put("time", formatter.format(curDate));
                        Firebase newPostRef = firebase.child("visitors").push();
                        newPostRef.setValue(post);
                        editor.putString("push_id", newPostRef.getKey());
                        editor.commit();
                        locationStatus.setText("You are now in the event!" + postCheck);
                        numberOfVisitors[0]=numberOfVisitors[0]+1;
                        firebase.child("numberOfVisitors").setValue(numberOfVisitors[0]);
                        postCheck=1;
                    }
                    else
                    {

                        //Toast.makeText(Information.this,"push_id is not empty, in event!",Toast.LENGTH_SHORT).show();
                        locationStatus.setText("You are now in the event!" + postCheck);
                    }

                }
                else
                {
                    numberOfVisitors[0]=numberOfVisitors[0]-1;
                    firebase.child("numberOfVisitors").setValue(numberOfVisitors[0]);
                    /*firebase.child("nameOfVisitors").child(sharedPreferences.getString("count_id","")).removeValue();
                    editor.remove("count_id");*/
                    locationStatus.setText("You are NOT in the event" + postCheck);
                    /*editor.commit();*/
                    postCheck=0;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postCheck == 1) {
                    if (!editText.getText().toString().isEmpty()) {
                        firebase.child("visitors").child(sharedPreferences.getString("push_id", "")).child("employee_id").setValue(editText.getText().toString());
                        Map<String, String> post = new HashMap<String, String>();
                        post.put("name", name);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date curDate = new Date(System.currentTimeMillis());
                        post.put("time", formatter.format(curDate));
                        post.put("employee_id", editText.getText().toString());
                        firebase.child("talkingList").push().setValue(post);
                    } else {
                        Toast.makeText(Information.this, "Employee ID is not valid!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Information.this, "You are not in the event!", Toast.LENGTH_LONG).show();
                }
            }
        });

        resetProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Information.this);
                builder.setMessage("This selection will delete all your personal information!");
                builder.setTitle("Reset yout profile");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent();
                        intent.setClass(Information.this, Initialization.class);
                        startActivity(intent);
                        Information.this.finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        firebase.child("numberOfVisitors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String temp = dataSnapshot.getValue().toString();
                numberOfVisitors[0] = Integer.parseInt(temp);
                visitorInfo.setText("Current number of visitors is " + numberOfVisitors[0]);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }



}
