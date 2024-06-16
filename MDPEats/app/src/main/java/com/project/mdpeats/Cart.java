package com.project.mdpeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.mdpeats.Common.Common;
import com.project.mdpeats.Database.Database;
import com.project.mdpeats.Models.CartOrder;
import com.project.mdpeats.Models.OrderRequest;
import com.project.mdpeats.Models.Token;
import com.project.mdpeats.ViewHolder.CartAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity { // this class is for students to use cart

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference cartRef;

    TextView tvTotalPrice;
    Button btnCart;

    List<CartOrder> cart = new ArrayList<>();

    CartAdapter cartAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Get database from Firebase
        database = FirebaseDatabase.getInstance();
        cartRef = database.getReference("Requests");

        // Initiate layout
        recyclerView = (RecyclerView) findViewById(R.id.cartList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        tvTotalPrice = (TextView) findViewById(R.id.total);
        btnCart = (Button) findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cart.size() > 0){
                    AlertDialog.Builder confirmOrder = new AlertDialog.Builder(Cart.this);
                    confirmOrder.setTitle("Order Confirmation");
                    confirmOrder.setMessage("Confirm Order?");

                    confirmOrder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                OrderRequest orderRequest = null;
                                if (Common.currentStudent != null) {
                                    orderRequest = new OrderRequest(
                                            Common.currentStudent.getStudentID(),
                                            cart.get(0).getFoodID(),
                                            Common.currentStudent.getName(),
                                            tvTotalPrice.getText().toString(),
                                            cart
                                    );


                                } else {
                                    // Handle the case where currentStudent is null (show a message, log, etc.)
                                    Log.e("Cart", "currentStudent is null");
                                }

                                //Send to Firebase and use Time as prime
                                String order_number = String.valueOf(System.currentTimeMillis());
                                cartRef.child(order_number).setValue(orderRequest);

                                new Database(getBaseContext()).cleanCart();
                                Toast.makeText(Cart.this, "Order has been placed", Toast.LENGTH_SHORT).show();
                                finish();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }


                            //sendOrderNotification(order_number); an attempt at notification
                        }
                    });
                    confirmOrder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    confirmOrder.show();
                }
                else {
                    Toast.makeText(Cart.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                }

            }
        });



        loadListCartFood();
    }

    // load items in cart
    private void loadListCartFood() {
        cart = new Database(this).getCarts();
        cartAdapter = new CartAdapter(cart, this);
        cartAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(cartAdapter);

        // Calculate total price
        int total = 0;

        for(CartOrder cartOrder : cart){
            total = total + (Integer.parseInt(cartOrder.getPrice()))*(Integer.parseInt(cartOrder.getQuantity()));
        }
        Locale locale = new Locale("ms", "MY");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);

        tvTotalPrice.setText(format.format(total));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.DELETE)){
            cart.remove(item.getOrder());
            new Database(this).cleanCart();
            for (CartOrder cartOrder: cart){
                new Database(this).addToCart(cartOrder);
            }
            loadListCartFood();
        }
        return super.onContextItemSelected(item);
    }
}