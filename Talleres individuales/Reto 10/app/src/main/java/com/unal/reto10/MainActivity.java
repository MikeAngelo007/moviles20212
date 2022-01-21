package com.unal.reto10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner departamenos_spinner;
    private ArrayList<String> departamentoss;
    private ArrayAdapter<String> departamenos_adapter;
    private Spinner municipios_spinner;
    private ArrayList<String> municipioss;
    private ArrayAdapter<String> municipios_adapter;
    private ListView list;
    private ArrayList<String> peajes;
    private ArrayAdapter<String> cod_adapter;
    private Context context = this;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        departamentoss = new ArrayList<>();
        municipioss = new ArrayList<>();
        peajes = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        String url = "https://www.datos.gov.co/resource/xdk5-pm3f.json?$select=distinct%20departamento&$order=departamento%20ASC";
        departamenos_spinner = (Spinner) findViewById(R.id.departamentos);
        municipios_spinner = (Spinner) findViewById(R.id.municipios);
        list = findViewById(R.id.list);
        JsonArrayRequest departamentos = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject tmp = null;
                            try {
                                tmp = response.getJSONObject(i);
                                departamentoss.add(tmp.getString("departamento"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        departamenos_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, departamentoss);
                        departamenos_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        departamenos_spinner.setAdapter(departamenos_adapter);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        departamenos_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                municipioss.clear();
                String tmp = (String) parent.getItemAtPosition(pos);
                String url = "https://www.datos.gov.co/resource/xdk5-pm3f.json?$select=distinct%20municipio&departamento="+ tmp + "&$order=municipio%20ASC";
                JsonArrayRequest municipios = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject tmp = null;
                                    try {
                                        tmp = response.getJSONObject(i);
                                        municipioss.add(tmp.getString("municipio"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                municipios_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, municipioss);
                                municipios_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                municipios_spinner.setAdapter(municipios_adapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                queue.add(municipios);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        municipios_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                peajes.clear();
                final String tmp = (String) parent.getItemAtPosition(pos);
                String url = "https://www.datos.gov.co/resource/xdk5-pm3f.json?municipio=" + tmp;
                JsonArrayRequest codes = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject tmp = null;
                                    try {
                                        tmp = response.getJSONObject(i);
                                        String tmp2 = "Region: " + tmp.getString("region") + "\n";
                                        tmp2 += "Codigo del departamento: " + tmp.getString("c_digo_dane_del_departamento") + "\n";
                                        tmp2 += "Departamento: " + tmp.getString("departamento") + "\n";
                                        tmp2 += "Codigo del municipio: " + tmp.getString("c_digo_dane_del_municipio") + "\n";
                                        tmp2 += "Municipio: " + tmp.getString("municipio") + "\n";
                                        peajes.add(tmp2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                cod_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, peajes);
                                list.setAdapter(cod_adapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("REQ", "bad");
                            }
                        });
                queue.add(codes);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        queue.add(departamentos);

    }
}
