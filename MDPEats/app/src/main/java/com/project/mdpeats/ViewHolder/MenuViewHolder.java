package com.project.mdpeats.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mdpeats.Interface.ItemClickListener;
import com.project.mdpeats.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {// this class is the view holder for menu

    public TextView txtMenuName;
    public ImageView imageView;

    private ItemClickListener itemClickListener;
    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        txtMenuName = (TextView) itemView.findViewById(R.id.menu_name);
        imageView = (ImageView) itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getBindingAdapterPosition(), false); //maybe change to getAdapterPosition()
    }
}
