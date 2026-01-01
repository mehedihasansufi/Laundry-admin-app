package com.rakstone.admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    TextView sing_up, forgot_password;
    EditText edit_email, edit_password;
    Button button_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        sing_up = findViewById(R.id.sing_up);
        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        button_login = findViewById(R.id.button_login);
        forgot_password = findViewById(R.id.forgot_password);




        // Forgot password
        forgot_password.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, forgotten_password_page.class))
        );


        // Login button click
        button_login.setOnClickListener(view -> {
            String email = edit_email.getText().toString().trim();
            String password = edit_password.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
                return;
            }

            // তোমার লোকাল সার্ভার IP এখানে দাও
            String url = "http://172.20.10.10/apps/admin_log_in_page.php?email=" + email + "&password=" + password;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");

                                if (status.equals("found")) {
                                    // ডাটাবেস থেকে পাওয়া তথ্য
                                    String name = jsonObject.getString("name");
                                    String mobile = jsonObject.getString("mobile");
                                    String address = jsonObject.getString("address");

                                    // home_page এ পাঠানোর জন্য static variable সেট করা
//
                                    home_page.home_email = email;
//                                    Cart.email=email;
//                                    Order_history.email=email;
//                                    My_profile.my_profile_email=email;
//                                    My_profile.my_profile_phone=mobile;
//                                    My_profile.my_profile_name=name;
//                                    if(address.length()>0){
//                                        My_profile.my_profile_address=address;
//                                    }

                                    startActivity(new Intent(MainActivity.this, home_page.class));
                                } else {
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle("Login Failed")
                                            .setMessage("Email or password not correct.")
                                            .show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
        });

        // Sign up click
        setSignUp();

    }

//    ========================================================
    private void setSignUp() {
        sing_up.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, SIgn_up_page.class))
        );
    }
}