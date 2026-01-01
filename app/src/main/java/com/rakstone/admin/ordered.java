package com.rakstone.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ordered extends AppCompatActivity {

    LinearLayout contentLayout;
    ImageView order_myprofile, order_home;
    ArrayList<HashMap<String, String>> orderList = new ArrayList<>();
    String baseUrl = "http://172.20.10.10/apps/admin_order_history.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ordered);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        contentLayout = findViewById(R.id.contentLayout);
        order_home = findViewById(R.id.order_home);
        order_myprofile = findViewById(R.id.order_myprofile);

        order_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ordered.this, home_page.class));
            }
        });



        // Load orders from server
//        String url = "http://192.168.1.102/apps/admin_order_history.php"; // শুধু সব অর্ডার আনবে
        loadOrders();
    }
    private void loadOrders() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {
                            JSONArray ordersArray = jsonObject.getJSONArray("orders");
                            orderList.clear();

                            for (int i = 0; i < ordersArray.length(); i++) {
                                JSONObject obj = ordersArray.getJSONObject(i);

                                HashMap<String, String> map = new HashMap<>();
                                map.put("id", obj.getString("id"));
                                map.put("history", obj.getString("history"));
                                map.put("cost", obj.getString("cost"));
                                map.put("item", obj.getString("item"));
                                map.put("name", obj.getString("name"));
                                map.put("mobile", obj.getString("mobile"));

                                orderList.add(map);
                            }

                            inflateOrders();

                        } else {
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Toast.makeText(this, "JSON Parse Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Volley Error", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void inflateOrders() {
        LayoutInflater inflater = LayoutInflater.from(this);
        contentLayout.removeAllViews();

        for (HashMap<String, String> order : orderList) {
            View view = inflater.inflate(R.layout.single_order, contentLayout, false);

            TextView orderName = view.findViewById(R.id.orderName);
            TextView mobile = view.findViewById(R.id.mobile);
            TextView name = view.findViewById(R.id.name);
            TextView item = view.findViewById(R.id.item);
            TextView cost = view.findViewById(R.id.cost);
            Button details = view.findViewById(R.id.details);
            Button accept = view.findViewById(R.id.button_accept);
            Button delivery = view.findViewById(R.id.button_delivery);

            orderName.setText(order.get("history"));
            name.setText(order.get("name"));
            mobile.setText(order.get("mobile"));
            item.setText(order.get("item") + " items");
            cost.setText(order.get("cost") + " tk");

            details.setOnClickListener(v ->
                    Toast.makeText(this, "Order ID: " + order.get("id"), Toast.LENGTH_SHORT).show()
            );

            accept.setOnClickListener(v -> updateOrderToPending(order.get("id")));
            delivery.setOnClickListener(v ->
                    Toast.makeText(this, "Delivery clicked for Order ID: " + order.get("id"), Toast.LENGTH_SHORT).show()
            );

            contentLayout.addView(view);
        }
    }

    private void updateOrderToPending(String orderId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        if (success) {
                            loadOrders(); // refresh list
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "JSON Parse Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Volley Error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected java.util.Map<String, String> getParams() {
                java.util.Map<String, String> params = new java.util.HashMap<>();
                params.put("action", "accept");
                params.put("id", orderId);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}