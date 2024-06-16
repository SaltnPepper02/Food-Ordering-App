package com.project.mdpeats.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.mdpeats.Common.Common;
import com.project.mdpeats.Database.Database;
import com.project.mdpeats.Interface.ItemClickListener;
import com.project.mdpeats.Models.CartOrder;
import com.project.mdpeats.R;

import java.util.ArrayList;
import java.util.List;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{ // this class is the view holder for cart

    public TextView txt_cart_name, txt_price;
    public ImageView img_cart_count;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    private ItemClickListener itemClickListener;

//    public FloatingActionButton btnDeleteCart; Work in progress
//
//    List<CartOrder> cart = new ArrayList<>();

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cart_name = (TextView) itemView.findViewById(R.id.cart_item_name);
        txt_price = (TextView) itemView.findViewById(R.id.cart_item_price);
        img_cart_count = (ImageView) itemView.findViewById(R.id.cart_item_count);
//        btnDeleteCart = (FloatingActionButton) itemView.findViewById(R.id.fabDeleteCart);

        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select action");
        menu.add(0,0, getBindingAdapterPosition(), Common.DELETE);
    }
}
