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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.project.mdpeats.Common.Common;
import com.project.mdpeats.Interface.ItemClickListener;
import com.project.mdpeats.Models.Food;
import com.project.mdpeats.ViewHolder.FoodViewHolder;
import com.project.mdpeats.databinding.ActivityFoodListBinding;
import com.project.mdpeats.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity { // this class is to show food menu to students
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;



    String foodstallID = "";

    ActivityFoodListBinding binding;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set bottom navigation item
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.nav_menu){
                Intent menuIntent = new Intent(FoodList.this, StudentHome.class);
                startActivity(menuIntent);
            } else if (id == R.id.nav_cart) {
                Intent cartIntent = new Intent(FoodList.this, Cart.class);
                startActivity(cartIntent);
            } else if (id == R.id.nav_orders) {
                Intent orderIntent = new Intent(FoodList.this, StudentOrder.class);
                startActivity(orderIntent);
            }
            return true;
        });

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        //Get Intent here
        if (getIntent() != null)
            foodstallID = getIntent().getStringExtra("FoodStallID");

        if (!foodstallID.isEmpty() && foodstallID != null) {
            if(Common.isConnectedToInternet(getBaseContext())){
                loadListFood(foodstallID);
            }
            else {
                Toast.makeText(FoodList.this, "Please check your internet", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    // load food stall menu into the screen
    private void loadListFood(String foodstallID) {
        Query query = foodList.orderByChild("menuId").equalTo(foodstallID);

        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(query, Food.class).build();
        try {
            adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                    //Log.d("FoodList", "Binding data - Name: " + model.getName() + ", Image: " + model.getImage());
                    holder.food_name.setText(model.getName());
                    holder.food_price.setText("RM " + model.getPrice());
                    Picasso.get().load(model.getImage()).into(holder.food_image);// not used until images are uploaded to database
                    final Food local = model;
                    holder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongCLick) {
                            //start new activity
                            Intent foodDetail = new Intent(FoodList.this, StudentFoodDetail.class);
                            foodDetail.putExtra("FoodId", adapter.getRef(position).getKey());
                            startActivity(foodDetail);
                        }
                    });
                }

                @NonNull
                @Override
                public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
                    return new FoodViewHolder(view);
                }

            };
            //Log.d("TAG", "" + adapter.getItemCount());
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}