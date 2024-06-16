package com.project.mdpeats;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.mdpeats.Cart;
import com.project.mdpeats.Common.Common;
import com.project.mdpeats.FoodList;
import com.project.mdpeats.Interface.ItemClickListener;
import com.project.mdpeats.Models.FoodStall;
import com.project.mdpeats.Models.Token;
import com.project.mdpeats.Notification.ListenOrder;
import com.project.mdpeats.R;
import com.project.mdpeats.ViewHolder.MenuViewHolder;
import com.project.mdpeats.databinding.ActivityStudentHomeBinding;
import com.squareup.picasso.Picasso;

public class StudentHome extends AppCompatActivity {// this class interacts with the student home page

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityStudentHomeBinding binding;

    FirebaseDatabase database;
    DatabaseReference foodstall;

    TextView txtFullName;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<FoodStall, MenuViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize viariable
        binding = ActivityStudentHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.appBarStudentHome.toolbar.setTitle("Menu");

        // add functionality to the navigation bar side
        binding.appBarStudentHome.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.nav_menu){

            } else if (id == R.id.nav_cart) {
                Intent cartIntent = new Intent(StudentHome.this, Cart.class);
                startActivity(cartIntent);
            } else if (id == R.id.nav_orders) {
                Intent orderIntent = new Intent(StudentHome.this, StudentOrder.class);
                startActivity(orderIntent);
            }
            return true;
        });

        setSupportActionBar(binding.appBarStudentHome.toolbar);

        database = FirebaseDatabase.getInstance();
        foodstall = database.getReference("FoodStalls");


        binding.appBarStudentHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(StudentHome.this, Cart.class);
                startActivity(cartIntent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_student_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // add functionality to navigation item
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.nav_menu){

            } else if (id == R.id.nav_cart) {
                Intent cartIntent = new Intent(StudentHome.this, Cart.class);
                startActivity(cartIntent);
            } else if (id == R.id.nav_orders) {
                Intent orderIntent = new Intent(StudentHome.this, StudentOrder.class);
                startActivity(orderIntent);
            } else if (id == R.id.nav_log_out) {
                Intent logoutIntent = new Intent(StudentHome.this, MainActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
            }

            // Close the drawer after handling item click
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView) headerView.findViewById(R.id.txtFullName);
        if (Common.currentStudent != null) {
            txtFullName.setText(Common.currentStudent.getName());
        } else {
            txtFullName.setText("Name");
        }

        //sets recycler view
        recycler_menu  = (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        if(Common.isConnectedToInternet(getBaseContext())){
            loadMenu();
        }
        else {
            Toast.makeText(StudentHome.this, "Please check your internet", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    // load food stalls into the screen
    private void loadMenu() {
        //load menu
        try {
            Query query = FirebaseDatabase.getInstance().getReference().child("FoodStalls");
            FirebaseRecyclerOptions<FoodStall> options = new FirebaseRecyclerOptions.Builder<FoodStall>().setQuery(query, FoodStall.class).build();

            adapter = new FirebaseRecyclerAdapter<FoodStall, MenuViewHolder> (options) {
                @Override
                protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull FoodStall model) {
                    holder.txtMenuName.setText(model.getName());
                    System.out.println(model.getImage());

                    if(!model.getImage().equals("")){
                        Picasso.get().load(model.getImage()).into(holder.imageView); // not used until images are uploaded to database
                    }

                    FoodStall clickItem = model;
                    holder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongCLick) {
                            //Get FoodStall ID and send to new Activity
                            Intent foodList = new Intent(StudentHome.this, FoodList.class);
                            //FoodStallID is key, so get key of this item
                            foodList.putExtra("FoodStallID", adapter.getRef(position).getKey());
                            startActivity(foodList);
                        }
                    });
                }

                @NonNull
                @Override
                public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    // inflate menu view holder
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
        getMenuInflater().inflate(R.menu.student_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // refresh menu
        if(item.getItemId() == R.id.Refresh){
            loadMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_student_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
