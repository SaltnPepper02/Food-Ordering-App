package com.project.mdpeats.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mdpeats.Models.CartOrder;
import com.project.mdpeats.Models.OrderRequest;
import com.project.mdpeats.R;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailViewHolder> { // this class is the view holder for order details

    List<CartOrder> fsoOrders;

    public OrderDetailAdapter(List<CartOrder> fsoOrders) {
        this.fsoOrders = fsoOrders;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item, parent, false);


        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        CartOrder orderRequest = fsoOrders.get(position);
        holder.tvName.setText(String.format("Name: %s", orderRequest.getFoodName()));
        holder.tvQuantity.setText(String.format("Quantity: %s", orderRequest.getQuantity()));
        holder.tbPrice.setText(String.format("Price: RM %s", orderRequest.getPrice()));

    }

    @Override
    public int getItemCount() {
        return fsoOrders.size();
    }
}
