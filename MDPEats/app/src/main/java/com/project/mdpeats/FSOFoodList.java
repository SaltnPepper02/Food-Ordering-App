package com.project.mdpeats;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.mdpeats.Common.Common;
import com.project.mdpeats.Interface.ItemClickListener;
import com.project.mdpeats.Models.Food;
import com.project.mdpeats.ViewHolder.FoodViewHolder;
import com.project.mdpeats.ViewHolder.MenuViewHolder;
import com.project.mdpeats.databinding.ActivityFsofoodListBinding;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class FSOFoodList extends AppCompatActivity { // this class is to show food menu for fso

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    String foodstallID = "";

    EditText edtName, edtDescription, edtPrice;

    Button btnSelect;

    FloatingActionButton fab;

    Food newFood;

    Uri saveUri = Uri.parse("https://slideplayer.com/2/754604/big_thumb.jpg");
    private final int PICK_IMAGE_REQUEST = 1;

    CoordinatorLayout coordinatorLayout;

    ActivityFsofoodListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFsofoodListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set bottom navigation item
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.nav_menu){
                Intent menuIntent = new Intent(FSOFoodList.this, FSOHome.class);
                startActivity(menuIntent);
            } else if (id == R.id.nav_orders) {
                Intent orderIntent = new Intent(FSOFoodList.this, FSOOrder.class);
                startActivity(orderIntent);
            }
            return true;
        });

        //initialize database and storage
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("uploads");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.rootLayoutFSO);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.currentFSO != null && Common.currentFSO.getFoodstall() != null) {
                    if (!foodstallID.equals(Common.currentFSO.getFoodstall())) {
                        Toast.makeText(FSOFoodList.this, "You do not have access to this", Toast.LENGTH_SHORT).show();
                    } else {
                        showAddFoodDialog();
                    }
                } else {
                    System.out.println("null value");
                }
            }
        });

        //Get Intent here
        if (getIntent() != null)
            foodstallID = getIntent().getStringExtra("FoodStallID");
        if (foodstallID != null && !foodstallID.isEmpty()) {
            loadListFood(foodstallID);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE) && foodstallID.equals(Common.currentFSO.getFoodstall())){
            showUpdateFoodDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));

        } else if (item.getTitle().equals(Common.DELETE) && foodstallID.equals(Common.currentFSO.getFoodstall())) {
            deleteFood(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    // loads all the food from database
    private void loadListFood(String foodstallID) {
        Query query = foodList.orderByChild("menuId").equalTo(foodstallID);

        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(query, Food.class).build();
        try {
            adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                    Log.d("FoodList", "Binding data - Name: " + model.getName() + ", Image: " + model.getImage());
                    holder.food_price.setText("RM " + model.getPrice());
                    holder.food_name.setText(model.getName());
                    Picasso.get().load(model.getImage()).into(holder.food_image);// not used until images are uploaded to database
                    final Food local = model;
                    holder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongCLick) {
                            //start new activity
                            Intent foodDetail = new Intent(FSOFoodList.this, FSOFoodDetail.class);
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
            Log.d("TAG", "" + adapter.getItemCount());
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //shows the add food dialog on screen
    private void showAddFoodDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(FSOFoodList.this);
        alert.setTitle("Add new Food");
        alert.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_fooditem_layout = inflater.inflate(R.layout.add_new_food_item, null);

        edtName = add_fooditem_layout.findViewById(R.id.edtName);
        edtDescription = add_fooditem_layout.findViewById(R.id.edtDescription);
        edtPrice = add_fooditem_layout.findViewById(R.id.edtPrice);

        btnSelect = add_fooditem_layout.findViewById(R.id.btnSelect);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();// this function lets you choose an image from ur food and save it
            }
        });


        alert.setView(add_fooditem_layout);
        alert.setIcon(R.drawable.baseline_shopping_cart_24);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                newFood = new Food(edtName.getText().toString(),
                        saveUri.toString(),
                        edtDescription.getText().toString(),
                        edtPrice.getText().toString(),
                        foodstallID);

                if(newFood != null){
                    foodList.push().setValue(newFood);
                }
                Snackbar.make(coordinatorLayout, "New Food " +newFood.getName()+ " was added", Snackbar.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    // update food
    private void showUpdateFoodDialog(String key, @NonNull Food item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(FSOFoodList.this);
        alert.setTitle("Edit Food");
        alert.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_fooditem_layout = inflater.inflate(R.layout.add_new_food_item, null);

        edtName = add_fooditem_layout.findViewById(R.id.edtName);
        edtDescription = add_fooditem_layout.findViewById(R.id.edtDescription);
        edtPrice = add_fooditem_layout.findViewById(R.id.edtPrice);

        //Original value
        edtName.setText(item.getName());
        edtDescription.setText(item.getDescription());
        edtPrice.setText(item.getPrice());


        btnSelect = add_fooditem_layout.findViewById(R.id.btnSelect);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); // this function lets you choose an image from ur food and save it
            }
        });


        alert.setView(add_fooditem_layout);
        alert.setIcon(R.drawable.baseline_shopping_cart_24);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setName(edtName.getText().toString());
                item.setDescription(edtDescription.getText().toString());
                item.setPrice(edtPrice.getText().toString());
                item.setImage(saveUri.toString());


                foodList.child(key).setValue(item);

                Snackbar.make(coordinatorLayout, "Food " +item.getName()+ " was changed", Snackbar.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void deleteFood(String key) {
        foodList.child(key).removeValue();
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    // add to firebase storage
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null){
            ProgressDialog pDialog = new ProgressDialog(FSOFoodList.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            saveUri = data.getData();
            if (saveUri != null){
                StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                        + "." + getFileExtension(saveUri));

                fileReference.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                saveUri = uri;
                                Toast.makeText(FSOFoodList.this, "Upload Success", Toast.LENGTH_SHORT).show();
                                btnSelect.setText("Image Selected");
                                pDialog.dismiss();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FSOFoodList.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });


            }
            else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }

    }

    // chose image from phone gallery
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }//will be added in the future

}