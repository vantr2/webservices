package com.trongvan.webservices;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SinhVienAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SinhVien> stList;

    //dialog view
    private TextView tvTitleDialog;
    private EditText etName,etAge,etAddress;
    private Button btnConfirm,btnCancel;

    private String id;


    public SinhVienAdapter(Context context, ArrayList<SinhVien> stList) {
        this.context = context;
        this.stList = stList;
    }

    @Override
    public int getCount() {
        return (stList == null ? 0 : stList.size());
    }

    @Override
    public Object getItem(int position) {
        return stList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_sinhvien,parent,false);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tvAge = (TextView) convertView.findViewById(R.id.tv_age);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            holder.ivEdit = (ImageView) convertView.findViewById(R.id.iv_edit);
            holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        SinhVien sv = stList.get(position);
        holder.tvName.setText(sv.getmName());
        holder.tvAge.setText(sv.getmAge() + "");
        holder.tvAddress.setText(sv.getmAddress());

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SinhVien sv = stList.get(position);
                showDialogUpdate(sv);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete: " + stList.get(position).getmID(), Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    private void showDialogUpdate(SinhVien sv) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_add_item);

        tvTitleDialog = (TextView) dialog.findViewById(R.id.tv_titile_dialog);
        etName = (EditText) dialog.findViewById(R.id.et_name);
        etAge = (EditText) dialog.findViewById(R.id.et_age);
        etAddress = (EditText) dialog.findViewById(R.id.et_address);
        btnConfirm = (Button) dialog.findViewById(R.id.btn_confirm);
        btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

        tvTitleDialog.setText("CẬP NHẬT THÔNG TIN");
        btnConfirm.setText("Sửa");

        id = String.valueOf(sv.getmID());
        etName.setText(sv.getmName());
        etAge.setText(sv.getmAge() + "");
        etAddress.setText(sv.getmAddress());


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString().trim();
                String age = etAge.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                if(name.isEmpty() || age.isEmpty() || address.isEmpty()){
                    Toast.makeText(context, "Bạn chưa nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                }else{
                    updateData(id,name,age,address);
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

    private void updateData(final String id, final String name, final String age, final String address) {
        String url = URLInteraction.URL_UPDATE_DATA;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            Log.e(MainActivity.LOG_KEY_INTERACTION_DATA,"Sửa thành công");
                        }else{
                            Log.e(MainActivity.LOG_KEY_INTERACTION_DATA,"Sửa không thành công");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(MainActivity.LOG_KEY_INTERACTION_DATA,"Lỗi request: " + error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put(MainActivity.KEY_ID_REQUEST,id);
                map.put(MainActivity.KEY_NAME_REQUEST,name);
                map.put(MainActivity.KEY_AGE_REQUEST,age);
                map.put(MainActivity.KEY_ADDRESS_REQUEST,address);
                return map ;
            }
        };
        queue.add(request);
    }

    private class ViewHolder{
        TextView tvName,tvAge,tvAddress;
        ImageView ivEdit,ivDelete;
    }
}
