package com.project.mdpeats.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.mdpeats.Common.Common;
import com.project.mdpeats.Interface.ItemClickListener;
import com.project.mdpeats.R;

public class FSOOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{ // this class is the view holder for orders on fso side
    public TextView txtOrderID, txtOrderStatus, txtOrderStudentID;

    private ItemClickListener itemClickListener;



    public FSOOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderID = (TextView) itemView.findViewById(R.id.order_item_id);
        txtOrderStatus = (TextView) itemView.findViewById(R.id.order_item_status);
        txtOrderStudentID = (TextView) itemView.findViewById(R.id.order_item_studentID);


        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getBindingAdapterPosition(), false);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select Action");

        menu.add(0,0,getBindingAdapterPosition(), Common.UPDATE);
        menu.add(0,0,getBindingAdapterPosition(), Common.DELETE);

    }
}
