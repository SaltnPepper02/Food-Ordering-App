package com.project.mdpeats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.mdpeats.Common.Common;
import com.project.mdpeats.Database.Database;
import com.project.mdpeats.Models.Food;
import com.project.mdpeats.Models.CartOrder;
import com.project.mdpeats.Widget.QuantityPicker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StudentFoodDetail extends AppCompatActivity {
    TextView food_name, food_price, food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    QuantityPicker numberButton;

    FirebaseDatabase database;
    DatabaseReference foodDetail;
    String foodId="";

    List<CartOrder> cart = new ArrayList<>();
    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        database = FirebaseDatabase.getInstance();
        foodDetail = database.getReference("Foods");

        numberButton = (QuantityPicker) findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if currentFood is null
                if (currentFood == null) {
                    Toast.makeText(StudentFoodDetail.this, "Food details not available", Toast.LENGTH_SHORT).show();
                    return; // Exit the method to avoid NullPointerException
                }

                // Get the foodStallId from the first item in the cart
                String foodStallId = getFoodStallIdFromCart();

                // Check if the cart is empty or if the new item has the same foodStallId
                if (cart.isEmpty() || foodStallId.equals(currentFood.getMenuId())) {
                    // Continue with adding the item to the cart
                    new Database(getBaseContext()).addToCart(new CartOrder(
                            currentFood.getMenuId(),
                            currentFood.getName(),
                            numberButton.getQuantity(),
                            currentFood.getPrice()
                    ));

                    Toast.makeText(StudentFoodDetail.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                    finish();
//                    Intent backToFoodList = new Intent(StudentFoodDetail.this, FoodList.class);
//                    startActivity(backToFoodList);

                } else {
                    // Show a message or handle the case where items belong to different food stalls
                    Toast.makeText(StudentFoodDetail.this, "Items in the cart must belong to the same food stall", Toast.LENGTH_SHORT).show();
                }
            }
        });


        food_name = (TextView) findViewById(R.id.food_name);
        food_price = (TextView) findViewById(R.id.food_price);
        food_description = (TextView) findViewById(R.id.food_description);
        food_image = (ImageView) findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsingAppbar);



        if (getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty()) {
            if(Common.isConnectedToInternet(getBaseContext())){
                loadFoodDetails(foodId);
            }
            else {
                Toast.makeText(StudentFoodDetail.this, "Please check your internet", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    private String getFoodStallIdFromCart() {
        cart = new Database(this).getCarts();
        // Get the foodStallId from the first item in the cart
        if (!cart.isEmpty()) {
            return cart.get(0).getFoodID();
        }
        return "";
    }

    // load food details into the screen
    private void loadFoodDetails(String foodId) {
        foodDetail.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentFood = snapshot.getValue(Food.class);

                    if (currentFood != null) {
                        // Put image
                        Picasso.get().load(currentFood.getImage()).into(food_image);//not added until image is put in

                        collapsingToolbarLayout.setTitle(currentFood.getName());
                        food_price.setText(currentFood.getPrice());
                        food_name.setText(currentFood.getName());
                        food_description.setText(currentFood.getDescription());
                    } else {
                        // Handle the case where the Food object is null
                        Toast.makeText(StudentFoodDetail.this, "Food details not available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle the case where the snapshot doesn't exist
                    Toast.makeText(StudentFoodDetail.this, "Food details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}