package com.loginworks.royaldines.databasehandler;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.loginworks.royaldines.extras.AppOnLeaseApplication;
import com.loginworks.royaldines.models.AddOn;
import com.loginworks.royaldines.models.Address;
import com.loginworks.royaldines.models.BranchParser;
import com.loginworks.royaldines.models.Food;
import com.loginworks.royaldines.models.OrderHistory;

import java.util.ArrayList;


public class DataHandlerDB {

    private static DataHandlerDB dataHandlerDB = null;


    private DataHandlerDB() {
    }

    private static SQLiteDatabase getReadableDB() {
        TLUserDBHelper sqlLiteUtil = TLUserDBHelper.getHelper(AppOnLeaseApplication.getApplicationCtx());
        return sqlLiteUtil.getReadableDatabase();
    }

    private static SQLiteDatabase getWritableDB() {
        TLUserDBHelper sqlLiteUtil = TLUserDBHelper.getHelper(AppOnLeaseApplication.getApplicationCtx());
        return sqlLiteUtil.getWritableDatabase();
    }

    public static DataHandlerDB getInstance() {
        if (dataHandlerDB == null) {
            dataHandlerDB = new DataHandlerDB();
        }
        return dataHandlerDB;
    }

    public static void clearFourthTable() {
        getWritableDB().delete(TLUserDBHelper.TABLE_FAVOURITE, null, null);

    }

    //-------------------------------Branch Table Operation-----------------------------------------
    public void insertBranchData(BranchParser branch) {

        SQLiteDatabase sdb = DataHandlerDB.getWritableDB();
        Cursor cursor = getReadableDB().rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_BRANCH
                + " WHERE " + TLUserDBHelper.BRANCH_ID + "=?", new String[]{branch.getBranch_id()});
        if (cursor.getCount() == 0) {
            try {
                ContentValues values = new ContentValues();
                values.put(TLUserDBHelper.BRANCH_ID, branch.getBranch_id());
                values.put(TLUserDBHelper.BRANCH_LOCATION_NAME, branch.getLocation_name());
                values.put(TLUserDBHelper.BRANCH_ADDRESS, branch.getAddress());
                values.put(TLUserDBHelper.BRANCH_CITY, branch.getCity());
                values.put(TLUserDBHelper.BRANCH_STATE, branch.getState());
                values.put(TLUserDBHelper.BRANCH_COUNTRY, branch.getCountry());
                values.put(TLUserDBHelper.BRANCH_ZIPCODE, branch.getZipcode());
                values.put(TLUserDBHelper.BRANCH_MOBILE, branch.getMobile());
                values.put(TLUserDBHelper.BRANCH_PHONE, branch.getPhone());
                values.put(TLUserDBHelper.BRANCH_LONGITUDE, branch.getLongitude());
                values.put(TLUserDBHelper.BRANCH_LATITUDE, branch.getLatitude());
                values.put(TLUserDBHelper.BRANCH_EMAIL, branch.getEmail());
                values.put(TLUserDBHelper.BRANCH_APP_ID, branch.getApp_id());
                values.put(TLUserDBHelper.BRANCH_USER_ID, branch.getUser_id());
                try {
                    sdb.insertWithOnConflict(TLUserDBHelper.TABLE_BRANCH, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Log.e("BRANCH INSERTED",
                        "Location Name:: " + branch.getLocation_name() +
                                " Branch Id::" + branch.getBranch_id());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void clearBranch() {
        SQLiteDatabase db = DataHandlerDB.getWritableDB();
        db.delete(TLUserDBHelper.TABLE_BRANCH, null, null);
        db.close();
    }

    public void clearAllTables() {
        SQLiteDatabase db = DataHandlerDB.getWritableDB();
        //db.delete(TLUserDBHelper.TABLE_BRANCH, null, null);
        db.delete(TLUserDBHelper.TABLE_ADDON, null, null);
        db.delete(TLUserDBHelper.TABLE_MANAGE_ADDON, null, null);
        db.delete(TLUserDBHelper.TABLE_CART, null, null);

        db.close();
    }


    public int getBranchCount() {
        int rowCount = 0;
        String query = "SELECT COUNT(*) FROM " + TLUserDBHelper.TABLE_BRANCH;

        Cursor cursor = getReadableDB().rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            rowCount = cursor.getInt(0);
        }
        return rowCount;
    }

    public ArrayList<BranchParser> getBranchData() {
        ArrayList<BranchParser> tl_branch_List = new ArrayList<>();

        Cursor cursor = getReadableDB().rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_BRANCH, null);
        while (cursor.moveToNext()) {
            tl_branch_List.add(new BranchParser(
                    cursor.getString(cursor.getColumnIndex(TLUserDBHelper.BRANCH_ID))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.BRANCH_LOCATION_NAME))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.BRANCH_ADDRESS))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.BRANCH_CITY))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.BRANCH_STATE))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.BRANCH_COUNTRY))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.BRANCH_ZIPCODE))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.BRANCH_MOBILE))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.BRANCH_PHONE))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.BRANCH_LONGITUDE))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.BRANCH_LATITUDE))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.BRANCH_EMAIL))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.BRANCH_APP_ID))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.BRANCH_USER_ID))
            ));
        }
        cursor.close();
        return tl_branch_List;
    }

    //--------------------------------Favourite Table Operation-------------------------------------

    //Inserting data in TL_FAOURITE table
    public String insertFavouriteData(Food food) {

        SQLiteDatabase sdb = DataHandlerDB.getWritableDB();
        try {
            Cursor cursor = sdb.rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_FAVOURITE
                    + " WHERE " + TLUserDBHelper.FAVOURITE_PRODUCT_ID + "=\"" + food.getProduct_id() + "\"", null);

            if (cursor.getCount() == 0) {

                ContentValues values = new ContentValues();
                //values.put(ID1, call_history_mst.ID1);
                values.put(TLUserDBHelper.FAVOURITE_PRODUCT_ID, food.getProduct_id());
                values.put(TLUserDBHelper.FAVOURITE_CATEGORY_ID, food.getCategory_id());
                values.put(TLUserDBHelper.FAVOURITE_PRODUCT_NAME, food.getProduct_name());
                values.put(TLUserDBHelper.FAVOURITE_PRODUCT_IMAGE, food.getProduct_img());
                values.put(TLUserDBHelper.FAVOURITE_DISCRIPTION, food.getDiscription());
                values.put(TLUserDBHelper.FAVOURITE_PRODUCT_TYPE, food.getProduct_type());
                values.put(TLUserDBHelper.FAVOURITE_AMOUNT, food.getAmount());
                values.put(TLUserDBHelper.FAVOURITE_DISCOUNT, food.getDiscount());
                values.put(TLUserDBHelper.FAVOURITE_CGST, food.getCgst_tax());
                values.put(TLUserDBHelper.FAVOURITE_SGST, food.getSgst_tax());
                values.put(TLUserDBHelper.FAVOURITE_SWACHCHH_BHARAT_TAX, food.getSwach_bharat_tax());
                try {
                    sdb.insertWithOnConflict(TLUserDBHelper.TABLE_FAVOURITE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Log.e("FAVOURITE INSERTED", "::: "
                        + "\n FAVOURITE_PRODUCT_ID= " + food.getProduct_id()
                        + "\n FAVOURITE_PRODUCT_NAME= " + food.getProduct_name()
                );
                cursor.close();
                return "inserted";
            } else {
                deleteFavouriteData(food.getProduct_id());
                Log.e("REMOVED", "::: " + "\nFAVOURITE_ID= " + food.getProduct_id()
                        + "\n FAVOURITE_PRODUCT_NAME= " + food.getProduct_name()
                );
                cursor.close();
                return "removed";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

//       public String getAllInsertion(int status) {
//        String countQuery = "SELECT  * FROM " + TLUserDBHelper.TABLE_NAME3
//                + " WHERE " + TLUserDBHelper.STS_CD + " = " + "'" + 0 + "'";
//
//        Cursor cursor = getReadableDB().rawQuery(countQuery, null);
//        String cnt = String.valueOf(cursor.getCount());
//        cursor.close();
//
//        return cnt;
//    }

    //get all data from TL_FAVOURITE table
    public ArrayList<Food> getFavouriteData() {
        ArrayList<Food> tl_favourtie_List = new ArrayList<>();

        Cursor cursor = getReadableDB().rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_FAVOURITE, null);
        while (cursor.moveToNext()) {
            tl_favourtie_List.add(new Food(
                    cursor.getString(cursor.getColumnIndex(TLUserDBHelper.FAVOURITE_PRODUCT_ID))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.FAVOURITE_CATEGORY_ID))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.FAVOURITE_PRODUCT_NAME))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.FAVOURITE_PRODUCT_IMAGE))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.FAVOURITE_DISCRIPTION))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.FAVOURITE_PRODUCT_TYPE))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.FAVOURITE_AMOUNT))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.FAVOURITE_DISCOUNT))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.FAVOURITE_CGST))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.FAVOURITE_SGST))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.FAVOURITE_SWACHCHH_BHARAT_TAX))
            ));

        }
        cursor.close();
        return tl_favourtie_List;
    }

    public int getFavouriteCount() {
        int rowCount = 0;
        String query = "SELECT COUNT(*) FROM " + TLUserDBHelper.TABLE_FAVOURITE;

        Cursor cursor = getReadableDB().rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            rowCount = cursor.getInt(0);
        }
        return rowCount;
    }

    //get id from TL_Favourite table
    public ArrayList<String> getFavouriteID() {
        ArrayList<String> tl_favourtieId_List = new ArrayList<>();

        Cursor cursor = getReadableDB().rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_FAVOURITE, null);
        while (cursor.moveToNext()) {
            tl_favourtieId_List.add(cursor.getString(cursor.getColumnIndex(TLUserDBHelper.FAVOURITE_PRODUCT_ID)));

        }
        cursor.close();
        return tl_favourtieId_List;
    }

    /*--------------------------------Cart Table Operation-----------------------------------*/

    public boolean isFavourite(String id) {
        String query = "SELECT * FROM " + TLUserDBHelper.TABLE_FAVOURITE
                + " WHERE " + TLUserDBHelper.FAVOURITE_PRODUCT_ID + "=\"" + id + "\"";
        //  Log.e("QUERY", query);

        Cursor cursor = getReadableDB().rawQuery(query, null);
        if (cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteFavouriteData(String id) {
        SQLiteDatabase db = DataHandlerDB.getWritableDB();
        db.delete(TLUserDBHelper.TABLE_FAVOURITE, TLUserDBHelper.FAVOURITE_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    //Inserting data in TL_CART table
    public void insertCartData(Food food, String product_qty) {

        SQLiteDatabase sdb = DataHandlerDB.getWritableDB();
        try {
            Cursor cursor = sdb.rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_CART
                    + " WHERE " + TLUserDBHelper.CART_PRODUCT_ID + "=\"" + food.getProduct_id() + "\"", null);

            if (cursor.getCount() == 0) {

                ContentValues values = new ContentValues();
                values.put(TLUserDBHelper.CART_PRODUCT_ID, food.getProduct_id());
                values.put(TLUserDBHelper.CART_CATEGORY_ID, food.getCategory_id());
                values.put(TLUserDBHelper.CART_PRODUCT_NAME, food.getProduct_name());
                values.put(TLUserDBHelper.CART_PRODUCT_IMAGE, food.getProduct_img());
                values.put(TLUserDBHelper.CART_DISCRIPTION, food.getDiscription());
                values.put(TLUserDBHelper.CART_PRODUCT_TYPE, food.getProduct_type());
                values.put(TLUserDBHelper.CART_AMOUNT, food.getAmount());
                values.put(TLUserDBHelper.CART_DISCOUNT, food.getDiscount());
                values.put(TLUserDBHelper.CART_CGST, food.getCgst_tax());
                values.put(TLUserDBHelper.CART_SGST, food.getSgst_tax());
                values.put(TLUserDBHelper.CART_SWACHCHH_BHARAT_TAX, food.getSwach_bharat_tax());
                values.put(TLUserDBHelper.CART_PRODUCT_QTY, product_qty);

                try {
                    sdb.insertWithOnConflict(TLUserDBHelper.TABLE_CART, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Log.e("CART INSERTED", "::: "
                        + "\n CART_PRODUCT_ID= " + food.getProduct_id()
                        + "\n CART_PRODUCT_NAME= " + food.getProduct_name()
                );
            } else {
                ContentValues contentValues = new ContentValues();
                if (product_qty.equalsIgnoreCase("0")) {
                    sdb.delete(TLUserDBHelper.TABLE_CART,
                            TLUserDBHelper.CART_PRODUCT_ID + "=?", new String[]{food.getProduct_id()});
                } else {
                    contentValues.put(TLUserDBHelper.CART_PRODUCT_QTY, product_qty);
                    sdb.update(TLUserDBHelper.TABLE_CART, contentValues,
                            TLUserDBHelper.CART_PRODUCT_ID + "=?", new String[]{food.getProduct_id()});
                }
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    // Inserting data in TL_CART table

    public void insertReOrderIntoCart(ArrayList<OrderHistory.OrderedProducts> orderedProductsArrayList) {

        SQLiteDatabase sdb = DataHandlerDB.getWritableDB();
        try {
            for (OrderHistory.OrderedProducts orderProduct : orderedProductsArrayList) {
                if (!orderProduct.isAddOn()) {

                    ContentValues values = new ContentValues();
                    values.put(TLUserDBHelper.CART_PRODUCT_ID, orderProduct.getProductId());
                    values.put(TLUserDBHelper.CART_CATEGORY_ID, orderProduct.getCatId());
                    values.put(TLUserDBHelper.CART_PRODUCT_NAME, orderProduct.getProductName());
                    values.put(TLUserDBHelper.CART_PRODUCT_IMAGE, orderProduct.getProductImage());
                    values.put(TLUserDBHelper.CART_DISCRIPTION, orderProduct.getDescription());
                    values.put(TLUserDBHelper.CART_PRODUCT_TYPE, orderProduct.getProductType());
                    values.put(TLUserDBHelper.CART_AMOUNT, orderProduct.getPrice());
                    values.put(TLUserDBHelper.CART_DISCOUNT, orderProduct.getDiscount());
                    values.put(TLUserDBHelper.CART_CGST, orderProduct.getCgst());
                    values.put(TLUserDBHelper.CART_SGST, orderProduct.getSgst());
                    values.put(TLUserDBHelper.CART_SWACHCHH_BHARAT_TAX, orderProduct.getSwatchBharatTax());
                    values.put(TLUserDBHelper.CART_PRODUCT_QTY, orderProduct.getProductQty());

                    try {
                        sdb.insertWithOnConflict(TLUserDBHelper.TABLE_CART, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Log.e("CART INSERTED", "::: ");

                    //cursor.close();
                } else {
                    ContentValues values = new ContentValues();
                    values.put(TLUserDBHelper.ADDON_ID, orderProduct.getProductId());
                    values.put(TLUserDBHelper.ADDON_CATEGORY_ID, orderProduct.getCatId());
                    values.put(TLUserDBHelper.ADDON_PRODUCT_NAME, orderProduct.getProductName());
                    values.put(TLUserDBHelper.ADDON_PRODUCT_IMAGE, orderProduct.getProductImage());
                    values.put(TLUserDBHelper.ADDON_DISCRIPTION, orderProduct.getDescription());
                    values.put(TLUserDBHelper.ADDON_PRODUCT_TYPE, orderProduct.getProductType());
                    values.put(TLUserDBHelper.ADDON_AMOUNT, orderProduct.getPrice());
                    values.put(TLUserDBHelper.ADDON_DISCOUNT, orderProduct.getDiscount());
                    values.put(TLUserDBHelper.ADDON_CGST, orderProduct.getCgst());
                    values.put(TLUserDBHelper.ADDON_SGST, orderProduct.getSgst());
                    values.put(TLUserDBHelper.ADDON_SWACHCHH_BHARAT_TAX, orderProduct.getSwatchBharatTax());
                    values.put(TLUserDBHelper.ADDON_MAIN_PRODUCT_ID, orderProduct.getAddOnParentProductId());
                    values.put(TLUserDBHelper.ADDON_PRODUCT_QTY, orderProduct.getProductQty());

                    try {
                        sdb.insertWithOnConflict(TLUserDBHelper.TABLE_ADDON, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Log.e("ADD ON INSERTED", "::: ");
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public boolean deleteCartProduct(String id) {
        SQLiteDatabase sdb = DataHandlerDB.getWritableDB();
        boolean result = false;
        int row = sdb.delete(TLUserDBHelper.TABLE_CART, TLUserDBHelper.CART_PRODUCT_ID + " = ?",
                new String[]{id});
        if (row > 0) {
            result = true;
        }
        return result;
    }

    //get all data from TL_Cart table
    public ArrayList<Food> getCartData() {
        ArrayList<Food> tl_cart_List = new ArrayList<>();

        Cursor cursor = getReadableDB().rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_CART, null);
        while (cursor.moveToNext()) {
            tl_cart_List.add(new Food(
                    cursor.getString(cursor.getColumnIndex(TLUserDBHelper.CART_PRODUCT_ID))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.CART_CATEGORY_ID))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.CART_PRODUCT_NAME))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.CART_PRODUCT_IMAGE))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.CART_DISCRIPTION))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.CART_PRODUCT_TYPE))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.CART_AMOUNT))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.CART_DISCOUNT))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.CART_CGST))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.CART_SGST))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.CART_SWACHCHH_BHARAT_TAX))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.CART_PRODUCT_QTY))
            ));
        }
        cursor.close();
        return tl_cart_List;
    }

    //Checking specific id existed or not
    public boolean isCart(String id) {
        String query = "SELECT * FROM " + TLUserDBHelper.TABLE_CART
                + " WHERE " + TLUserDBHelper.CART_PRODUCT_ID + "=\"" + id + "\"";
        Log.e("QUERY", query);

        Cursor cursor = getReadableDB().rawQuery(query, null);
        if (cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public int cartProductQty(String id) {
        int productQty = 0;
        String query = "SELECT " + TLUserDBHelper.CART_PRODUCT_QTY + " FROM " + TLUserDBHelper.TABLE_CART
                + " WHERE " + TLUserDBHelper.CART_PRODUCT_ID + "='" + id + "'";

        Cursor cursor = getReadableDB().rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                return productQty;
            } else {
                productQty = cursor.getInt(cursor.getColumnIndex(TLUserDBHelper.CART_PRODUCT_QTY));
            }
        }
        return productQty;
    }

    public int getTotalQty() {
        int totalQty = 0;
        SQLiteDatabase sdb = DataHandlerDB.getReadableDB();
        String addon_query = "SELECT * FROM TABLE_ADDON";
        Cursor addon_cursor = sdb.rawQuery(addon_query, null);
        if (addon_cursor.getCount() == 0) {
            totalQty = getCartCount();
            addon_cursor.close();
        } else {
            String query = "SELECT SUM(product_qty) AS TOTAL_QTY FROM (SELECT product_qty FROM TABLE_CART" +
                    " UNION ALL SELECT product_qty FROM TABLE_ADDON)";
            Cursor cursor = getReadableDB().rawQuery(query, null);
            if (cursor != null) {
                cursor.moveToFirst();
                totalQty = cursor.getInt(cursor.getColumnIndex("TOTAL_QTY"));
            }
            cursor.close();
        }
        return totalQty;
    }

    public double getTotalAmount() {
        double totalamount = 0.0;
        try {

            SQLiteDatabase sdb = DataHandlerDB.getReadableDB();
            String addon_query = "SELECT * FROM TABLE_ADDON";
            Cursor addon_cursor = sdb.rawQuery(addon_query, null);
            if (addon_cursor.getCount() == 0) {
                totalamount = getCartAmount();
                addon_cursor.close();
            } else {

                String q1 = "SELECT SUM(PRODUCT_QTY * AMOUNT) AS AMOUNT FROM TABLE_CART";
                String q2 = "SELECT SUM(PRODUCT_QTY * AMOUNT) AS AMOUNT FROM TABLE_ADDON";

                Cursor cursor1 = getReadableDB().rawQuery(q1, null);
                if (cursor1 != null) {
                    cursor1.moveToFirst();
                    totalamount = cursor1.getDouble(0);

                }

                cursor1.close();

                Cursor cursor2 = getReadableDB().rawQuery(q2, null);
                if (cursor2 != null) {
                    cursor2.moveToFirst();
                    totalamount = totalamount + cursor2.getDouble(0);
                }
                cursor2.close();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return totalamount;
    }

    //get total cart amount
    public double getCartAmount() {
        double totalprice = 0.0;
        double grandtotal = 0.0;
        try {
            String query = "SELECT " + TLUserDBHelper.CART_PRODUCT_QTY + "," + TLUserDBHelper.CART_AMOUNT + " FROM "
                    + TLUserDBHelper.TABLE_CART;

            Cursor cursor = getReadableDB().rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    double price = cursor.getDouble(cursor.getColumnIndex(TLUserDBHelper.CART_AMOUNT));
                    double qty = cursor.getDouble(cursor.getColumnIndex(TLUserDBHelper.CART_PRODUCT_QTY));
                    totalprice = price * qty;
                    grandtotal = totalprice + grandtotal;
                }
                while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return grandtotal;

    }

    /*-------------------------------AddOn Table Operation-----------------------------------*/

    //get no. of item in cart
    public int getCartCount() {
        int count = 0;
        String query = "SELECT SUM(" + TLUserDBHelper.CART_PRODUCT_QTY + ") FROM " + TLUserDBHelper.TABLE_CART;
        Log.e("Cart Count", query);
        Cursor cursor = getReadableDB().rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
        }
        return count;
    }

    //remove all
    public void deleteAllCart() {
        SQLiteDatabase db = DataHandlerDB.getWritableDB();
        db.delete(TLUserDBHelper.TABLE_CART, null, null);
        db.close();
    }

    //remove all
    public void deleteManageAddOn() {
        SQLiteDatabase db = DataHandlerDB.getWritableDB();
        db.delete(TLUserDBHelper.TABLE_MANAGE_ADDON, null, null);
        db.close();
    }

    //Inserting data in TL_CART table
    public void insertAddOnData(String cart_id, String product_qty, AddOn addOn) {

        SQLiteDatabase sdb = DataHandlerDB.getWritableDB();
        try {
            Cursor cursor = sdb.rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_ADDON
                    + " WHERE " + TLUserDBHelper.ADDON_ID + "=\"" + addOn.getAddon_id() + "\"", null);

            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(TLUserDBHelper.ADDON_ID, addOn.getAddon_id());
                values.put(TLUserDBHelper.ADDON_CATEGORY_ID, addOn.getCategory_id());
                values.put(TLUserDBHelper.ADDON_PRODUCT_NAME, addOn.getProduct_name());
                values.put(TLUserDBHelper.ADDON_PRODUCT_IMAGE, addOn.getProduct_img());
                values.put(TLUserDBHelper.ADDON_DISCRIPTION, addOn.getDiscription());
                values.put(TLUserDBHelper.ADDON_PRODUCT_TYPE, addOn.getProduct_type());
                values.put(TLUserDBHelper.ADDON_AMOUNT, addOn.getAmount());
                values.put(TLUserDBHelper.ADDON_DISCOUNT, addOn.getDiscount());
                values.put(TLUserDBHelper.ADDON_CGST, addOn.getCgst_tax());
                values.put(TLUserDBHelper.ADDON_SGST, addOn.getSgst_tax());
                values.put(TLUserDBHelper.ADDON_SWACHCHH_BHARAT_TAX, addOn.getSwach_bharat_tax());
                values.put(TLUserDBHelper.ADDON_MAIN_PRODUCT_ID, addOn.getMain_product_id());
                values.put(TLUserDBHelper.ADDON_PRODUCT_QTY, product_qty);

                try {
                    sdb.insertWithOnConflict(TLUserDBHelper.TABLE_ADDON, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Log.e("ADDON INSERTED", "::: "
                        + "\n ADDON_ID= " + addOn.getAddon_id()
                        + "\n ADDON_PRODUCT_NAME= " + addOn.getProduct_name()
                );
            } else {
                ContentValues contentValues = new ContentValues();
                if (product_qty.equalsIgnoreCase("0")) {
                    sdb.delete(TLUserDBHelper.TABLE_ADDON,
                            TLUserDBHelper.ADDON_ID + "=?", new String[]{addOn.getAddon_id()});
                } else {
                    contentValues.put(TLUserDBHelper.ADDON_PRODUCT_QTY, product_qty);
                    sdb.update(TLUserDBHelper.TABLE_ADDON, contentValues,
                            TLUserDBHelper.ADDON_ID + "=?", new String[]{addOn.getAddon_id()});
                }
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getAddOnProductQty(String id) {
        int productQty = 0;
        String query = "SELECT " + TLUserDBHelper.ADDON_PRODUCT_QTY + " FROM " + TLUserDBHelper.TABLE_ADDON
                + " WHERE " + TLUserDBHelper.ADDON_ID + "='" + id + "'";

        Cursor cursor = getReadableDB().rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                return productQty;
            } else {
                productQty = cursor.getInt(cursor.getColumnIndex(TLUserDBHelper.ADDON_PRODUCT_QTY));
            }
        }
        return productQty;
    }

    public boolean deleteAddOnProduct(String id) {
        SQLiteDatabase sdb = DataHandlerDB.getWritableDB();
        boolean result = false;
        int row = sdb.delete(TLUserDBHelper.TABLE_ADDON, TLUserDBHelper.ADDON_ID + " = ?", new String[]{id});
        if (row > 0) {
            result = true;
        }
        return result;
    }

    public int deleteAddOnProductWise(String main_product_id) {
        int row = 0;
        try {
            SQLiteDatabase sdb = DataHandlerDB.getWritableDB();
            row = sdb.delete(TLUserDBHelper.TABLE_ADDON, TLUserDBHelper.ADDON_MAIN_PRODUCT_ID
                    + " = ?", new String[]{main_product_id});
            return row;
        } catch (Exception ex) {
            ex.printStackTrace();
            return row;
        }
    }

    //get all data from TL_ADDON table
    public ArrayList<AddOn> getAddOnData() {
        ArrayList<AddOn> addOnArrayList = new ArrayList<>();

        Cursor cursor = getReadableDB().rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_ADDON, null);
        while (cursor.moveToNext()) {
            addOnArrayList.add(new AddOn(
                    cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_ID))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_CATEGORY_ID))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_PRODUCT_NAME))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_PRODUCT_IMAGE))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_DISCRIPTION))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_PRODUCT_TYPE))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_AMOUNT))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_DISCOUNT))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_CGST))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_SGST))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_SWACHCHH_BHARAT_TAX))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_MAIN_PRODUCT_ID))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_PRODUCT_QTY))
            ));

        }
        cursor.close();
        return addOnArrayList;
    }

    public ArrayList<AddOn> getCartWiseAddon(String id) {

        ArrayList<AddOn> tl_addon_List = new ArrayList<>();
        Cursor cursor = getReadableDB().rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_ADDON
                + " WHERE " + TLUserDBHelper.ADDON_MAIN_PRODUCT_ID + "=?", new String[]{id});
        while (cursor.moveToNext()) {
            tl_addon_List.add(new AddOn(
                    cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_ID))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_CATEGORY_ID))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_PRODUCT_NAME))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_PRODUCT_IMAGE))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_DISCRIPTION))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_PRODUCT_TYPE))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_AMOUNT))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_DISCOUNT))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_CGST))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_SGST))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_SWACHCHH_BHARAT_TAX))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_MAIN_PRODUCT_ID))
                    , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDON_PRODUCT_QTY))
            ));
        }
        cursor.close();
        return tl_addon_List;
    }

    public boolean addOnExists(String id) {
        boolean exists = false;
        Cursor cursor = getReadableDB().rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_ADDON + " WHERE " +
                TLUserDBHelper.ADDON_MAIN_PRODUCT_ID + "=?", new String[]{id});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                exists = true;
            }
        }
        cursor.close();
        return exists;
    }

    public void deleteAddOn() {
        SQLiteDatabase db = DataHandlerDB.getWritableDB();
        db.delete(TLUserDBHelper.TABLE_ADDON, null, null);
        db.close();
    }

    public void insertManageAddOnData(ArrayList<AddOn> addOnList) {

        SQLiteDatabase sdb = DataHandlerDB.getWritableDB();
        try {
            int addonsize = addOnList.size();

            Cursor cursor = sdb.rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_MANAGE_ADDON
                    + " WHERE " + TLUserDBHelper.MANAGE_ADDON_MAIN_PRODUCT_ID + "=?", new String[]{addOnList.get(0).getMain_product_id()});

            if (cursor.getCount() == 0) {
                for (int i = 0; i < addonsize; i++) {
                    ContentValues values = new ContentValues();

                    values.put(TLUserDBHelper.MANAGE_ADDON_ID, addOnList.get(i).getAddon_id());
                    values.put(TLUserDBHelper.MANAGE_ADDON_CATEGORY_ID, addOnList.get(i).getCategory_id());
                    values.put(TLUserDBHelper.MANAGE_ADDON_PRODUCT_NAME, addOnList.get(i).getProduct_name());
                    values.put(TLUserDBHelper.MANAGE_ADDON_PRODUCT_IMAGE, addOnList.get(i).getProduct_img());
                    values.put(TLUserDBHelper.MANAGE_ADDON_DISCRIPTION, addOnList.get(i).getDiscription());
                    values.put(TLUserDBHelper.MANAGE_ADDON_PRODUCT_TYPE, addOnList.get(i).getProduct_type());
                    values.put(TLUserDBHelper.MANAGE_ADDON_AMOUNT, addOnList.get(i).getAmount());
                    values.put(TLUserDBHelper.MANAGE_ADDON_DISCOUNT, addOnList.get(i).getDiscount());
                    values.put(TLUserDBHelper.MANAGE_ADDON_CGST, addOnList.get(i).getCgst_tax());
                    values.put(TLUserDBHelper.MANAGE_ADDON_SGST, addOnList.get(i).getSgst_tax());
                    values.put(TLUserDBHelper.MANAGE_ADDON_SWACHCHH_BHARAT_TAX, addOnList.get(i).getSwach_bharat_tax());
                    values.put(TLUserDBHelper.MANAGE_ADDON_MAIN_PRODUCT_ID, addOnList.get(i).getMain_product_id());

                    try {
                        sdb.insertWithOnConflict(TLUserDBHelper.TABLE_MANAGE_ADDON, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Log.e("MANAGE ADDON INSERTED", "::: ");
                }
            } else {
                //TODO If already exits
//                ContentValues contentValues = new ContentValues();
//                if (product_qty.equalsIgnoreCase("0")) {
//                    sdb.delete(TLUserDBHelper.TABLE_ADDON,
//                            TLUserDBHelper.ADDON_ID + "=?", new String[]{addOn.getAddon_id()});
//                } else {
//                    contentValues.put(TLUserDBHelper.ADDON_PRODUCT_QTY, product_qty);
//                    sdb.update(TLUserDBHelper.TABLE_ADDON, contentValues,
//                            TLUserDBHelper.ADDON_ID + "=?", new String[]{addOn.getAddon_id()});
//                }
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<AddOn> getManageAddon(String product_id) {
        ArrayList<AddOn> tl_manageaddon_List = new ArrayList<>();

        Cursor cursor = getReadableDB().rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_MANAGE_ADDON +
                " WHERE " + TLUserDBHelper.MANAGE_ADDON_MAIN_PRODUCT_ID + "=?", new String[]{product_id});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                tl_manageaddon_List.add(new AddOn(
                        cursor.getString(cursor.getColumnIndex(TLUserDBHelper.MANAGE_ADDON_ID))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.MANAGE_ADDON_CATEGORY_ID))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.MANAGE_ADDON_PRODUCT_NAME))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.MANAGE_ADDON_PRODUCT_IMAGE))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.MANAGE_ADDON_DISCRIPTION))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.MANAGE_ADDON_PRODUCT_TYPE))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.MANAGE_ADDON_AMOUNT))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.MANAGE_ADDON_DISCOUNT))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.MANAGE_ADDON_CGST))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.MANAGE_ADDON_SGST))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.MANAGE_ADDON_SWACHCHH_BHARAT_TAX))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.MANAGE_ADDON_MAIN_PRODUCT_ID))
                ));
            }
        }
        cursor.close();
        return tl_manageaddon_List;
    }

    public boolean manageAddOnExists(String product_id) {
        boolean exists = false;
        Cursor cursor = getReadableDB().rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_MANAGE_ADDON + " WHERE " +
                TLUserDBHelper.MANAGE_ADDON_MAIN_PRODUCT_ID + "=?", new String[]{product_id});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                exists = true;
            }
        }
        cursor.close();
        return exists;
    }


    //Inserting data in TL_ADDRESS table
    public void insertAddressData(ArrayList<Address> addressList) {

        SQLiteDatabase sdb = DataHandlerDB.getWritableDB();
        try {
            Log.e("Size", addressList.size() + "");
            for (int i = 0; i < addressList.size(); i++) {

                Address address = addressList.get(i);

                Cursor cursor = sdb.rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_ADDRESS
                        + " WHERE " + TLUserDBHelper.ADDRESS_ID + "=?", new String[]{address.getId()});

                if (cursor.getCount() == 0) {
                    ContentValues values = new ContentValues();
                    values.put(TLUserDBHelper.ADDRESS_ID, address.getId());
                    values.put(TLUserDBHelper.ADDRESS_NAME, address.getName());
                    values.put(TLUserDBHelper.ADDRESS_EMAIL, address.getEmail());
                    values.put(TLUserDBHelper.ADDRESS_DELIVERY_PHONE, address.getDelivery_phone());
                    values.put(TLUserDBHelper.ADDRESS_HOUSE_NO, address.getHouse_no());
                    values.put(TLUserDBHelper.ADDRESS_LANDMARK, address.getLandmark());
                    values.put(TLUserDBHelper.ADDRESS_CITY, address.getCity());
                    values.put(TLUserDBHelper.ADDRESS_STATE, address.getState());
                    values.put(TLUserDBHelper.ADDRESS_PIN_CODE, address.getPincode());

                    try {
                        sdb.insertWithOnConflict(TLUserDBHelper.TABLE_ADDRESS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    Log.e("ADDRESS INSERTED", "::: ");
                } else {
                    Log.e("Already exists", ":::");
                    //TODO if already exists
                }
                cursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Address> getAddress() {
        ArrayList<Address> tl_manageaddon_List = new ArrayList<>();

        Cursor cursor = getReadableDB().rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_ADDRESS, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                tl_manageaddon_List.add(new Address(
                        cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDRESS_ID))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDRESS_NAME))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDRESS_EMAIL))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDRESS_DELIVERY_PHONE))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDRESS_HOUSE_NO))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDRESS_LANDMARK))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDRESS_STREET))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDRESS_CITY))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDRESS_STATE))
                        , cursor.getString(cursor.getColumnIndex(TLUserDBHelper.ADDRESS_PIN_CODE))
                ));
            }
        }
        cursor.close();
        return tl_manageaddon_List;
    }

    public int getAddressCount() {
        try {
            Cursor cursor = getReadableDB().rawQuery("SELECT * FROM " + TLUserDBHelper.TABLE_ADDRESS, null);
            return cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean deleteAddress(String address_id) {
        SQLiteDatabase sdb = DataHandlerDB.getWritableDB();

        int row = sdb.delete(TLUserDBHelper.TABLE_ADDRESS,
                TLUserDBHelper.ADDRESS_ID + "=?", new String[]{address_id});
        if (row == 1) {
            return true;
        } else {
            return false;
        }
    }

}
