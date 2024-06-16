package com.project.mdpeats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.project.mdpeats.Common.Common;
import com.project.mdpeats.Interface.ItemClickListener;
import com.project.mdpeats.Models.FSO;
import com.project.mdpeats.Models.OrderRequest;
import com.project.mdpeats.Notification.ListenOrder;
import com.project.mdpeats.ViewHolder.FSOOrderViewHolder;
import com.project.mdpeats.databinding.ActivityFsoOrderStatusBinding;

public class FSOOrder extends AppCompatActivity { // this class interacts with the orders sent by students to the fso


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<OrderRequest, FSOOrderViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference orderRef;

    MaterialSpinner materialSpinner;

    MediaPlayer mediaPlayer;

    ActivityFsoOrderStatusBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFsoOrderStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set bottom navigation item
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.nav_menu){
                Intent menuIntent = new Intent(FSOOrder.this, FSOHome.class);
                startActivity(menuIntent);
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

        mediaPlayer = MediaPlayer.create(this, R.raw.notification_sound);

        loadOrders(Common.currentFSO.getFoodstall());

        // if there is a new order play a sound to notify the fso
        orderRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    // Check if the order is for the current food stall
                    OrderRequest newOrder = snapshot.getValue(OrderRequest.class);
                    System.out.println(Common.currentFSO.getFoodstall());
                    if (newOrder != null && newOrder.getFoodStallID() != null &&
                            newOrder.getFoodStallID().equals(Common.currentFSO.getFoodstall())) {
                        // Play the notification sound when a new order is added for the current food stall
                        playNotificationSound();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void playNotificationSound() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        // Release the MediaPlayer when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        super.onDestroy();
    }

    // load orders into the screen
    private void loadOrders(String foodstall) {
        Query query = orderRef.orderByChild("foodStallID").equalTo(foodstall);
        FirebaseRecyclerOptions<OrderRequest> options = new FirebaseRecyclerOptions.
                Builder<OrderRequest>().setQuery(query, OrderRequest.class).build();

        adapter = new FirebaseRecyclerAdapter<OrderRequest, FSOOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FSOOrderViewHolder holder, int position, @NonNull OrderRequest model) {
                // sets the items for latest order to earliest order
                int reversedPosition = getItemCount() - position - 1;
                holder.txtOrderID.setText(adapter.getRef(reversedPosition).getKey());
                holder.txtOrderStatus.setText(Common.convertToStatus(model.getStatus()));
                holder.txtOrderStudentID.setText(model.getStudentID());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongCLick) {
                        int reversedPosition = getItemCount() - position - 1;
                        Intent orderDetail = new Intent(FSOOrder.this, OrderDetail.class);
                        Common.currentOrderRequest = model;
                        orderDetail.putExtra("OrderID", adapter.getRef(reversedPosition).getKey());
                        startActivity(orderDetail);
                    }
                });
            }

            @NonNull
            @Override
            public FSOOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new FSOOrderViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE)){
            updateOrder(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));

        } else if (item.getTitle().equals(Common.DELETE)) {
            deleteOrder(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteOrder(String key) {
        orderRef.child(key).removeValue();
    }

    private void updateOrder(String key, OrderRequest item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FSOOrder.this);
        alertDialog.setTitle("Order Update?");
        alertDialog.setMessage("Choose Status");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.order_update, null);

        materialSpinner = (MaterialSpinner) view.findViewById(R.id.statusUpdate);
        materialSpinner.setItems("In Kitchen", "Ready for Pick Up", "Order Picked Up");

        alertDialog.setView(view);

        String localKey = key;
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(materialSpinner.getSelectedIndex()));

                orderRef.child(localKey).setValue(item);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }


}