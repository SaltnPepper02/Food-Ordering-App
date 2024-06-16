package com.project.mdpeats.ViewHolder;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.project.mdpeats.Database.Database;
import com.project.mdpeats.Models.CartOrder;
import com.project.mdpeats.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{ // this class is the adapter for Cart

    private List<CartOrder> listData = new ArrayList<>();
    private Context context;

    // Create Constructor
    public CartAdapter(List<CartOrder> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    List<CartOrder> cart = new ArrayList<>();

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.cart_item,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder().buildRound(" " +
                listData.get(position).getQuantity(), Color.BLUE);
        holder.img_cart_count.setImageDrawable(drawable);

        holder.txt_cart_name.setText(listData.get(position).getFoodName());

        Locale locale = new Locale("ms", "MY");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice()))*
                (Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_price.setText(format.format(price));

//        holder.btnDeleteCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteCart(position);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
