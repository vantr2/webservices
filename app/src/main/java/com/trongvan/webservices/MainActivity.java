package com.trongvan.webservices;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ID_REQUEST = "key_id";
    public static final String KEY_NAME_REQUEST = "key_name";
    public static final String KEY_AGE_REQUEST = "key_age";
    public static final String KEY_ADDRESS_REQUEST = "key_address";
    public static final String LOG_KEY_INTERACTION_DATA = "LOG";

    private ListView lvStInfo;

    //dialog view
    private TextView tvTitleDialog;
    private EditText etName,etAge,etAddress;
    private Button btnConfirm,btnCancel;

    private ArrayList<SinhVien> stList;
    private SinhVienAdapter svAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialView();

        stList = new ArrayList<>();

        svAdapter = new SinhVienAdapter(MainActivity.this,stList);
        lvStInfo.setAdapter(svAdapter);
        fillData();

    }

    private void initialView() {
        lvStInfo = (ListView) findViewById(R.id.lv_st_info);
    }

    private void fillData(){
        String url = URLInteraction.URL_GET_DATA;
        stList.clear();
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                stList.add(new SinhVien(
                                        object.getInt("ID"),
                                        object.getString("HoTen"),
                                        object.getInt("Tuoi"),
                                        object.getString("DiaChi")
                                ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        svAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_KEY_INTERACTION_DATA,"Lỗi: " + error.toString());
                    }
                });
        queue.add(arrayRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        showDialog();
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_add_item);

        tvTitleDialog = (TextView) dialog.findViewById(R.id.tv_titile_dialog);
        etName = (EditText) dialog.findViewById(R.id.et_name);
        etAge = (EditText) dialog.findViewById(R.id.et_age);
        etAddress = (EditText) dialog.findViewById(R.id.et_address);
        btnConfirm = (Button) dialog.findViewById(R.id.btn_confirm);
        btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

        tvTitleDialog.setText("THÊM THÔNG TIN");
        btnConfirm.setText("Thêm");

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String age = etAge.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                if(name.isEmpty() || age.isEmpty() || address.isEmpty()){
                    Toast.makeText(MainActivity.this, "Bạn chưa nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                }else{
                    insertData(name,age,address);
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                    dialog.dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void insertData(final String iName, final String iAge, final String iAddress){
        String url = URLInteraction.URL_INSERT_DATA;
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){
                            Log.e(LOG_KEY_INTERACTION_DATA,"Thêm thành công");
                        }else{
                            Log.e(LOG_KEY_INTERACTION_DATA,"Thêm lỗi");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_KEY_INTERACTION_DATA,"Lỗi request: " + error.toString() );
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map  = new HashMap<>();
                map.put(KEY_NAME_REQUEST,iName);
                map.put(KEY_AGE_REQUEST,iAge);
                map.put(KEY_ADDRESS_REQUEST,iAddress);
                return map;
            }
        };
        queue.add(stringRequest);
    }


}
