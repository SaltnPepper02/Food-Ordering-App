package com.project.mdpeats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.project.mdpeats.Common.Common;
import com.project.mdpeats.Models.OrderRequest;
import com.project.mdpeats.ViewHolder.OrderDetailAdapter;
import com.project.mdpeats.ViewHolder.OrderDetailViewHolder;

import org.w3c.dom.Text;

import java.util.List;

public class OrderDetail extends AppCompatActivity {

    TextView order_id, order_studentID, order_total, order_status;
    String order_id_value="";
    RecyclerView listFood;
    RecyclerView.LayoutManager layoutManager;

    // displays all the food items in the order
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_id = (TextView) findViewById(R.id.order_item_id);
        order_studentID = (TextView) findViewById(R.id.order_item_studentID);
        order_total = (TextView) findViewById(R.id.order_item_price);
        order_status = (TextView) findViewById(R.id.order_item_status);

        listFood = (RecyclerView) findViewById(R.id.listFood);
        //listFood.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listFood.setLayoutManager(layoutManager);

        if(getIntent() != null){
            order_id_value = getIntent().getStringExtra("OrderID");
        }

        order_id.setText(order_id_value);
        order_studentID.setText(Common.currentOrderRequest.getStudentID());
        order_total.setText(Common.currentOrderRequest.getTotal());
        order_status.setText(Common.convertToStatus(Common.currentOrderRequest.getStatus()));

        OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentOrderRequest.getFoods());
        adapter.notifyDataSetChanged();
        listFood.setAdapter(adapter);


    }
}