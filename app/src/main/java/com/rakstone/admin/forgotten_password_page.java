package com.rakstone.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class forgotten_password_page extends AppCompatActivity {


    EditText edit_email,edit_mobile;
    Button button_check_password;
    TextView show_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgotten_password_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edit_email=findViewById(R.id.edit_email);
        edit_mobile=findViewById(R.id.edit_mobile);
        button_check_password=findViewById(R.id.button_check_password);
        show_password=findViewById(R.id.show_password);



        button_check_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=edit_email.getText().toString();
                String mobile=edit_mobile.getText().toString();


                if(email.length()>0 && mobile.length()>0){
                    String url="http://172.20.10.10/apps/admin_forget_password_page.php?email="+email+"&mobile="+mobile;

                    StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.trim().equals("not found")){

                                new AlertDialog.Builder(forgotten_password_page.this)
                                        .setTitle("Not match")
                                        .setMessage("user not found")
                                        .show();
                            }else{

                                show_password.setText(response);

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            button_check_password.setText("not"+error.getMessage());
                        }
                    });

                    RequestQueue requestQueue= Volley.newRequestQueue(forgotten_password_page.this);
                    requestQueue.add(stringRequest);

                }else {
                    Toast.makeText(forgotten_password_page.this,"all box required",Toast.LENGTH_LONG).show();
                }


            }
        });
    }
}