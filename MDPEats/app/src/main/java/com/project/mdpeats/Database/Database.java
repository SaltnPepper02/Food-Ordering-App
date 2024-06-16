package com.project.mdpeats.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.project.mdpeats.Models.CartOrder;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper { // this class interacts with local database
    // explanation of y SQLite is used rather than Firebase: SQLite is more dynamic and can change
    // query without needing to
    private static final String DB_NAME = "MDPEatsDB.db";
    private static final int DB_VER = 1;
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<CartOrder> getCarts(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"FoodName", "FoodID", "Quantity", "Price"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect, null, null, null, null, null);

        final List<CartOrder> result = new ArrayList<>();
        if (c.moveToFirst()){
            do {
                int foodIdIndex = c.getColumnIndex("FoodID");
                int foodNameIndex = c.getColumnIndex("FoodName");
                int quantityIndex = c.getColumnIndex("Quantity");
                int priceIndex = c.getColumnIndex("Price");
                result.add(new CartOrder(
                        c.getString(foodIdIndex),
                        c.getString(foodNameIndex),
                        c.getString(quantityIndex),
                        c.getString(priceIndex)
                ));
            }while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(CartOrder cartOrder){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(FoodID, FoodName, Quantity, Price) VALUES('%s','%s','%s','%s');",
                cartOrder.getFoodID(),
                cartOrder.getFoodName(),
                cartOrder.getQuantity(),
                cartOrder.getPrice());
        db.execSQL(query);
    }

    public void cleanCart(){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }
}
