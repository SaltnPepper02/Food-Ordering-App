package com.project.mdpeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.project.mdpeats.Common.Common;
import com.project.mdpeats.Interface.ItemClickListener;
import com.project.mdpeats.Models.OrderRequest;
import com.project.mdpeats.ViewHolder.StudentOrderViewHolder;
import com.project.mdpeats.databinding.ActivityStudentOrderBinding;

public class StudentOrder extends AppCompatActivity { // this class interacts with the orders of the students
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<OrderRequest, StudentOrderViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference orderRef;


    ActivityStudentOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.nav_menu){
                Intent menuIntent = new Intent(StudentOrder.this, StudentHome.class);
                startActivity(menuIntent);
            } else if (id == R.id.nav_cart) {
                Intent cartIntent = new Intent(StudentOrder.this, Cart.class);
                startActivity(cartIntent);
            } else if (id == R.id.nav_orders) {

            }
            return true;
        });

        database = FirebaseDatabase.getInstance();
        orderRef = database.getReference("Requests");

        recyclerView = (RecyclerView) findViewById(R.id.orderList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentStudent.getStudentID());


    }


    private void loadOrders(String studentID) {
        Query query = orderRef.orderByChild("studentID").equalTo(studentID);
        FirebaseRecyclerOptions<OrderRequest> options = new FirebaseRecyclerOptions.
                Builder<OrderRequest>().setQuery(query, OrderRequest.class).build();

        adapter = new FirebaseRecyclerAdapter<OrderRequest, StudentOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StudentOrderViewHolder holder, int position, @NonNull OrderRequest model) {
                // sets the items for latest order to earliest order
                int reversedPosition = getItemCount() - position - 1;
                holder.txtOrderID.setText(adapter.getRef(reversedPosition).getKey());
                holder.txtOrderStatus.setText(Common.convertToStatus(model.getStatus()));
                holder.txtOrderStudentID.setText(model.getStudentID());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongCLick) {
                        int reversedPosition = getItemCount() - position - 1;
                        Intent orderDetail = new Intent(StudentOrder.this, OrderDetail.class);
                        Common.currentOrderRequest = model;
                        orderDetail.putExtra("OrderID", adapter.getRef(reversedPosition).getKey());
                        startActivity(orderDetail);
                    }
                });
            }

            @NonNull
            @Override
            public StudentOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new StudentOrderViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }




}