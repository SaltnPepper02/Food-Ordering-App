package com.project.mdpeats.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mdpeats.Interface.ItemClickListener;
import com.project.mdpeats.R;

public class StudentOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { // this class is the view holder for orders on student side

    public TextView txtOrderID, txtOrderStatus, txtOrderStudentID;

    private ItemClickListener itemClickListener;

    public StudentOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderID = (TextView) itemView.findViewById(R.id.order_item_id);
        txtOrderStatus = (TextView) itemView.findViewById(R.id.order_item_status);
        txtOrderStudentID = (TextView) itemView.findViewById(R.id.order_item_studentID);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getBindingAdapterPosition(), false);
    }



}
