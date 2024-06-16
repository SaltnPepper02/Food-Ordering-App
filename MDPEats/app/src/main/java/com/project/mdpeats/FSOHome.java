package com.project.mdpeats;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.project.mdpeats.Common.Common;
import com.project.mdpeats.Interface.ItemClickListener;
import com.project.mdpeats.Models.FoodStall;
import com.project.mdpeats.ViewHolder.MenuViewHolder;
import com.project.mdpeats.databinding.ActivityFsoHomeBinding;
import com.squareup.picasso.Picasso;

public class FSOHome extends AppCompatActivity { // this class interacts with the fso home page

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityFsoHomeBinding binding;

    TextView txtFullName;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference foodstall;
    FirebaseRecyclerAdapter<FoodStall, MenuViewHolder> adapter;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFsoHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.appBarFsohome.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.nav_menu){

            } else if (id == R.id.nav_orders) {
                Intent orderIntent = new Intent(FSOHome.this, FSOOrder.class);
                startActivity(orderIntent);
            }
            return true;
        });

        // set uo firebase
        database = FirebaseDatabase.getInstance();
        foodstall = database.getReference("FoodStalls");

        // set up screen layout
        binding.appBarFsohome.toolbar.setTitle("Menu Management");
        setSupportActionBar(binding.appBarFsohome.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_fsohome);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.nav_menu){

            } else if (id == R.id.nav_fso_orders) {
                Intent orderIntent = new Intent(FSOHome.this, FSOOrder.class);
                startActivity(orderIntent);
            } else if (id == R.id.nav_fso_log_out) {
                Intent logoutIntent = new Intent(FSOHome.this, MainActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
            }

            // Close the drawer after handling item click
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        //Set Name for FSO
        View hView = navigationView.getHeaderView(0);
        txtFullName = (TextView) hView.findViewById(R.id.txtFullName);//might have error
        txtFullName.setText(Common.currentFSO.getName());

        recycler_menu = (RecyclerView) findViewById(R.id.recycler_fso_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadFSOMenu();
    }

    // load food stall menu
    private void loadFSOMenu() {
        try {
            Query query = FirebaseDatabase.getInstance().getReference().child("FoodStalls");
            FirebaseRecyclerOptions<FoodStall> options = new FirebaseRecyclerOptions.Builder<FoodStall>().setQuery(query, FoodStall.class).build();

            adapter = new FirebaseRecyclerAdapter<FoodStall, MenuViewHolder> (options) {
                @Override
                protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull FoodStall model) {
                    holder.txtMenuName.setText(model.getName());
                    if(!model.getImage().equals("")){
                        Picasso.get().load(model.getImage()).into(holder.imageView); // not used until images are uploaded to database
                    }
                    FoodStall clickItem = model;
                    holder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongCLick) {
                            //Get FoodStall ID and send to new Activity
                            Intent foodList = new Intent(FSOHome.this, FSOFoodList.class);
                            //FoodStallID is key, so get key of this item
                            foodList.putExtra("FoodStallID", adapter.getRef(position).getKey());
                            startActivity(foodList);
                        }
                    });
                }

                @NonNull
                @Override
                public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
                    return new MenuViewHolder(view);
                }
            };
            recycler_menu.setAdapter(adapter);
            adapter.startListening();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.f_s_o_home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_fsohome);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}