package com.project.mdpeats.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mdpeats.R;

public class OrderDetailViewHolder  extends RecyclerView.ViewHolder {// this class is the view holder for order details

    public TextView tvName, tvQuantity, tbPrice;
    public OrderDetailViewHolder(@NonNull View itemView) {
        super(itemView);

        tvName = (TextView) itemView.findViewById(R.id.detail_food_name);
        tvQuantity = (TextView) itemView.findViewById(R.id.detail_food_quantity);
        tbPrice = (TextView) itemView.findViewById(R.id.detail_food_price);

    }
}
