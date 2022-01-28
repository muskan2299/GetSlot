package com.hackslash.getslot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hackslash.getslot.Model.Districts;
import com.hackslash.getslot.Model.States;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    Spinner stateSpinner, districtSpinner;
    Button search;
    int age=0;
    static int state,district;
    ArrayList<String> stateName=new ArrayList<>();
    static ArrayList<States> states=new ArrayList<>();
    ArrayList<String> districtName=new ArrayList<>();
    static ArrayList<Districts> districts=new ArrayList<>();
    private FirebaseAnalytics firebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAnalytics=FirebaseAnalytics.getInstance(this);
        stateSpinner =(Spinner) findViewById(R.id.state);
        districtSpinner =(Spinner) findViewById(R.id.district);
        search=(Button) findViewById(R.id.search);
        stateName.add("None");
        states.add(new States(0,"None"));
        districtName.add("None");
        districts.add(new Districts(0,"None"));
        fetchStates();
        if(state>0)
        {
            fetchDistrict();
        }
        search.setOnClickListener(v -> {
            if(state>0 && district>0)
            {
                Intent trasfer=new Intent(MainActivity.this, SlotsActivity.class);
                trasfer.putExtra("state",state);
                trasfer.putExtra("district",district);
                trasfer.putExtra("age",age);
                startActivity(trasfer);
            }
        });

    }

    private void fetchDistrict() {
        if(districts.size()>1)
        {
            districts.clear();
            districts.add(new Districts(0,"None"));
            districtName.clear();
            districtName.add("None");
        }
        String districtURL="https://cdn-api.co-vin.in/api/v2/admin/location/districts/"+state;
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, districtURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray districtList=response.getJSONArray("districts");
                    for(int i=0;i<districtList.length();i++) {
                        JSONObject stateObject=districtList.getJSONObject(i);
                        String name=stateObject.getString("district_name");
                        int id=stateObject.getInt("district_id");
                        districts.add(new Districts(id,name));
                        districtName.add(name);
                    }
                    setupDistrictSpinner(districtSpinner);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQue(request);
    }

    private void setupDistrictSpinner(Spinner districtSpinner) {
        ArrayAdapter<String> districtAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, districtName);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(districtAdapter);
        districtSpinner.setOnItemSelectedListener(new DistrictSpinner());
    }

    private void fetchStates() {
        String stateURL="https://cdn-api.co-vin.in/api/v2/admin/location/states";
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, stateURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray stateList=response.getJSONArray("states");
                    for(int i=0;i<stateList.length();i++) {
                        JSONObject stateObject=stateList.getJSONObject(i);
                        String name=stateObject.getString("state_name");
                        int id=stateObject.getInt("state_id");
                        states.add(new States(id,name));
                        stateName.add(name);
                    }
                    setupStateSpinner(stateSpinner);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQue(request);
    }

    private void setupStateSpinner(Spinner stateSpinner) {
        ArrayAdapter<String> stateAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateName);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);
        stateSpinner.setOnItemSelectedListener(new StateSpinner());

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    class StateSpinner implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            state=states.get(position).getState_id();
            fetchDistrict();
            System.out.println(state);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            state=states.get(0).getState_id();
        }
    }
    class DistrictSpinner implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            district=districts.get(position).getDistrict_id();
            System.out.println(district);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            district=districts.get(0).getDistrict_id();
        }
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio18:
                if (checked)
                    age=18;
                    break;
            case R.id.radio45:
                if (checked)
                    age=45;
                    break;
        }
    }
}
