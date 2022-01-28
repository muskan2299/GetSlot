package com.hackslash.getslot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hackslash.getslot.Model.Districts;
import com.hackslash.getslot.Model.Hospital;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class SlotsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    HospitalAdapter adapter;
    ArrayList<Hospital> hospitals=new ArrayList<>();
    ImageView noSlotImage;
    TextView noSlotText;
    private FirebaseAnalytics firebaseAnalytics;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slots);
        Intent data=getIntent();
        firebaseAnalytics=FirebaseAnalytics.getInstance(this);
        noSlotImage=(ImageView) findViewById(R.id.no_vaccine);
        noSlotText=(TextView) findViewById(R.id.no_slot_title);
        int state=data.getIntExtra("state",0);
        int district=data.getIntExtra("district",0);
        int age=data.getIntExtra("age",0);
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
        Date currentDate=new Date();
        String date=sdf.format(currentDate);
        recyclerView=(RecyclerView) findViewById(R.id.hospitals);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        if(state>0 && district>0) {
            fetchHospitals(state,district,date,age);
        }
    }

    private void fetchHospitals(int state,int district,String date,int age) {
        String centerURL="https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id="+district+"&date="+date;
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, centerURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray districtList=response.getJSONArray("centers");
                    for(int i=0;i<districtList.length();i++) {
                        JSONObject centerHospital=districtList.getJSONObject(i);
                        JSONArray sessionsArray=centerHospital.getJSONArray("sessions");
                        if(age==(sessionsArray.getJSONObject(0).getInt("min_age_limit")) || age==0) {
                            String name = centerHospital.getString("name");
                            String fee_type = centerHospital.getString("fee_type");
                            String address = centerHospital.getString("address");
                            boolean exist = false;
                            String vaccine = sessionsArray.getJSONObject(0).getString("vaccine");
                            String sesseions = "Date:             Slot:" + "\n";
                            for (int j = 0; j < sessionsArray.length(); j++) {
                                JSONObject object = sessionsArray.getJSONObject(j);
                                int capacity = Integer.parseInt(object.getString("available_capacity"));
                                if (capacity > 0) {
                                    exist = true;
                                }
                                sesseions = sesseions + object.getString("date") + "      " + object.getString("available_capacity") + "\n";
                            }
                            if (exist) {
                                hospitals.add(new Hospital(name, address, fee_type, sesseions, vaccine));
                            }
                        }
                    }
                    if(hospitals.size()==0)
                    {
                        noSlotImage.setVisibility(View.VISIBLE);
                        noSlotText.setVisibility(View.VISIBLE);
                    }
                    adapter=new HospitalAdapter(hospitals);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SlotsActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQue(request);
    }
}