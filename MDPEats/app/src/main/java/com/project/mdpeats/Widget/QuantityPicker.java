package com.project.mdpeats.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
public class QuantityPicker extends LinearLayout{ // widget to get quantity of food
    private TextView quantityText;
    private int quantity = 1;

    public QuantityPicker(Context context) {
        super(context);
        init();
    }

    public QuantityPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        // Decrement Button
        Button decrementButton = new Button(getContext());
        decrementButton.setText("-");
        decrementButton.setTextSize(20); // Set text size
        decrementButton.setTextColor(getResources().getColor(android.R.color.black)); // Set text color
        decrementButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        decrementButton.setOnClickListener(v -> decrementQuantity());

        // Quantity Text
        quantityText = new TextView(getContext());
        quantityText.setText(String.valueOf(quantity));
        quantityText.setTextSize(18); // Set text size
        quantityText.setTextColor(getResources().getColor(android.R.color.black)); // Set text color
        quantityText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        // Increment Button
        Button incrementButton = new Button(getContext());
        incrementButton.setText("+");
        incrementButton.setTextSize(20); // Set text size
        incrementButton.setTextColor(getResources().getColor(android.R.color.black)); // Set text color
        incrementButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        incrementButton.setOnClickListener(v -> incrementQuantity());

        // Add views to the layout
        addView(decrementButton);
        addView(quantityText);
        addView(incrementButton);
    }

    private void incrementQuantity() {
        if (quantity < 20) {
            quantity++;
            updateQuantityText();
        }
    }

    private void decrementQuantity() {
        if (quantity > 1) {
            quantity--;
            updateQuantityText();
        }
    }

    private void updateQuantityText() {
        quantityText.setText(String.valueOf(quantity));
        // TODO: Implement any additional logic or listener callbacks as needed
    }

    // Getter for the current quantity
    public String getQuantity() {
        return String.valueOf(quantity);
    }

    // Override onMeasure to provide proper measurements
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), quantityText.getMeasuredHeight());
    }
}
