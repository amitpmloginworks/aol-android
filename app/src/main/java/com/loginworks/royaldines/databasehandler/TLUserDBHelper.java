package com.loginworks.royaldines.databasehandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


//DB Helper class
public class TLUserDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_BRANCH = "TABLE_BRANCH";
    public static final String TABLE_FAVOURITE = "TABLE_FAVOURITE";
    public static final String TABLE_CART = "TABLE_CART";
    public static final String TABLE_ADDON = "TABLE_ADDON";
    public static final String TABLE_MANAGE_ADDON = "TABLE_MANAGE_ADDON";
    public static final String TABLE_ADDRESS = "TABLE_ADDRESS";
//    public static final String TABLE_REORDER_CART = "TABLE_REORDER_CART";
//    public static final String TABLE_REORDER_ADDON = "TABLE_REORDER_ADDON";

    //TABLE BRANCH's FIELDS
    public static final String BRANCH_ROW_ID = "row_id";
    public static final String BRANCH_ID = "branch_id";
    public static final String BRANCH_LOCATION_NAME = "location_name";
    public static final String BRANCH_ADDRESS = "address";
    public static final String BRANCH_CITY = "city";
    public static final String BRANCH_STATE = "state";
    public static final String BRANCH_COUNTRY = "country";
    public static final String BRANCH_ZIPCODE = "zipcode";
    public static final String BRANCH_MOBILE = "mobile";
    public static final String BRANCH_PHONE = "phone";
    public static final String BRANCH_LONGITUDE = "longitude";
    public static final String BRANCH_LATITUDE = "latitude";
    public static final String BRANCH_EMAIL = "email";
    public static final String BRANCH_APP_ID = "app_id";
    public static final String BRANCH_USER_ID = "user_id";

    //TABLE FAVOURITE's FIELDS
    public static final String FAVOURITE_ROW_ID = "row_id";
    public static final String FAVOURITE_PRODUCT_ID = "product_id";
    public static final String FAVOURITE_CATEGORY_ID = "category_id";
    public static final String FAVOURITE_PRODUCT_NAME = "product_name";
    public static final String FAVOURITE_PRODUCT_IMAGE = "product_image";
    public static final String FAVOURITE_DISCRIPTION = "discription";
    public static final String FAVOURITE_PRODUCT_TYPE = "product_type";
    public static final String FAVOURITE_AMOUNT = "amount";
    public static final String FAVOURITE_DISCOUNT = "discount";
    public static final String FAVOURITE_CGST = "cgst";
    public static final String FAVOURITE_SGST = "sgst";
    public static final String FAVOURITE_SWACHCHH_BHARAT_TAX = "swachchh_bharat_tax";

    //TABLE FAVOURITE's FIELDS
    public static final String CART_ROW_ID = "row_id";
    public static final String CART_PRODUCT_ID = "product_id";
    public static final String CART_CATEGORY_ID = "category_id";
    public static final String CART_PRODUCT_NAME = "product_name";
    public static final String CART_PRODUCT_IMAGE = "product_image";
    public static final String CART_DISCRIPTION = "discription";
    public static final String CART_PRODUCT_TYPE = "product_type";
    public static final String CART_AMOUNT = "amount";
    public static final String CART_DISCOUNT = "discount";
    public static final String CART_CGST = "cgst";
    public static final String CART_SGST = "sgst";
    public static final String CART_SWACHCHH_BHARAT_TAX = "swachchh_bharat_tax";
    public static final String CART_PRODUCT_QTY = "product_qty";

    //TABLE ADDON's FIELDS
    public static final String ADDON_ROW_ID = "row_id";
    public static final String ADDON_ID = "addon_id";
    public static final String ADDON_MAIN_PRODUCT_ID = "main_product_id";
    public static final String ADDON_CATEGORY_ID = "category_id";
    public static final String ADDON_PRODUCT_NAME = "product_name";
    public static final String ADDON_PRODUCT_IMAGE = "product_image";
    public static final String ADDON_DISCRIPTION = "discription";
    public static final String ADDON_PRODUCT_TYPE = "product_type";
    public static final String ADDON_AMOUNT = "amount";
    public static final String ADDON_DISCOUNT = "discount";
    public static final String ADDON_CGST = "cgst";
    public static final String ADDON_SGST = "sgst";
    public static final String ADDON_SWACHCHH_BHARAT_TAX = "swachchh_bharat_tax";
    public static final String ADDON_PRODUCT_QTY = "product_qty";

    //TABLE MANAGE ADDON's FIELDS
    public static final String MANAGE_ADDON_ROW_ID = "row_id";
    public static final String MANAGE_ADDON_ID = "addon_id";
    public static final String MANAGE_ADDON_CATEGORY_ID = "category_id";
    public static final String MANAGE_ADDON_PRODUCT_NAME = "product_name";
    public static final String MANAGE_ADDON_PRODUCT_IMAGE = "product_image";
    public static final String MANAGE_ADDON_DISCRIPTION = "discription";
    public static final String MANAGE_ADDON_PRODUCT_TYPE = "product_type";
    public static final String MANAGE_ADDON_AMOUNT = "amount";
    public static final String MANAGE_ADDON_DISCOUNT = "discount";
    public static final String MANAGE_ADDON_CGST = "cgst";
    public static final String MANAGE_ADDON_SGST = "sgst";
    public static final String MANAGE_ADDON_SWACHCHH_BHARAT_TAX = "swachchh_bharat_tax";
    public static final String MANAGE_ADDON_MAIN_PRODUCT_ID = "main_product_id";

    //TABLE ADDRESS FIELDS
    public static final String ADDRESS_ROW_ID = "row_id";
    public static final String ADDRESS_ID = "address_id";
    public static final String ADDRESS_NAME = "name";
    public static final String ADDRESS_EMAIL = "email";
    public static final String ADDRESS_DELIVERY_PHONE = "delivery_phone";
    public static final String ADDRESS_HOUSE_NO = "house_no";
    public static final String ADDRESS_LANDMARK = "address_landmark";
    public static final String ADDRESS_STREET = "street";
    public static final String ADDRESS_CITY = "city";
    public static final String ADDRESS_STATE = "state";
    public static final String ADDRESS_PIN_CODE = "pin_code";

//    /*REORDER TABLE CART's FIELDS*/
//    public static final String REORDER_CART_ROW_ID = "row_id";
//    public static final String REORDER_CART_PRODUCT_ID = "product_id";
//    public static final String REORDER_CART_CATEGORY_ID = "category_id";
//    public static final String REORDER_CART_PRODUCT_NAME = "product_name";
//    public static final String REORDER_CART_PRODUCT_IMAGE = "product_image";
//    public static final String REORDER_CART_DISCRIPTION = "discription";
//    public static final String REORDER_CART_PRODUCT_TYPE = "product_type";
//    public static final String REORDER_CART_AMOUNT = "amount";
//    public static final String REORDER_CART_DISCOUNT = "discount";
//    public static final String REORDER_CART_VAT = "vat";
//    public static final String REORDER_CART_SERVICE_TAX = "service_tax";
//    public static final String REORDER_CART_SWACHCHH_BHARAT_TAX = "swachchh_bharat_tax";
//    public static final String REORDER_CART_PRODUCT_QTY = "product_qty";
//
//    /*RE ORDER TABLE ADD ON FIELDS*/
//    public static final String REORDER_ADDON_ROW_ID = "row_id";
//    public static final String REORDER_ADDON_ID = "addon_id";
//    public static final String REORDER_ADDON_MAIN_PRODUCT_ID = "main_product_id";
//    public static final String REORDER_ADDON_CATEGORY_ID = "category_id";
//    public static final String REORDER_ADDON_PRODUCT_NAME = "product_name";
//    public static final String REORDER_ADDON_PRODUCT_IMAGE = "product_image";
//    public static final String REORDER_ADDON_DISCRIPTION = "discription";
//    public static final String REORDER_ADDON_PRODUCT_TYPE = "product_type";
//    public static final String REORDER_ADDON_AMOUNT = "amount";
//    public static final String REORDER_ADDON_DISCOUNT = "discount";
//    public static final String REORDER_ADDON_VAT = "vat";
//    public static final String REORDER_ADDON_SERVICE_TAX = "service_tax";
//    public static final String REORDER_ADDON_SWACHCHH_BHARAT_TAX = "swachchh_bharat_tax";
//    public static final String REORDER_ADDON_PRODUCT_QTY = "product_qty";

    private static final String DATABASE_NAME = "AOL.db";
    private static final int DATABASE_VERSION = 2;
    private static TLUserDBHelper dbHelper;


    public TLUserDBHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized TLUserDBHelper getHelper(Context context) {
        if (dbHelper == null)
            dbHelper = new TLUserDBHelper(context);

        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Database tables created");

        /*db.execSQL("CREATE TABLE "
                + TABLE_NAME1 + "("
                + ID1 + "  INTEGER PRIMARY KEY , "
                + USER_STATUS + " TEXT NOT NULL);");*/
        String create_table_branch = "CREATE TABLE "
                + TABLE_BRANCH + "("
                + BRANCH_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BRANCH_ID + " TEXT NOT NULL,"
                + BRANCH_LOCATION_NAME + " TEXT NOT NULL,"
                + BRANCH_ADDRESS + " TEXT NOT NULL,"
                + BRANCH_CITY + " TEXT NOT NULL,"
                + BRANCH_STATE + " TEXT NOT NULL,"
                + BRANCH_COUNTRY + " TEXT NOT NULL,"
                + BRANCH_ZIPCODE + " TEXT NOT NULL,"
                + BRANCH_MOBILE + " TEXT NOT NULL,"
                + BRANCH_PHONE + " TEXT NOT NULL,"
                + BRANCH_LONGITUDE + " TEXT NOT NULL,"
                + BRANCH_LATITUDE + " TEXT NOT NULL,"
                + BRANCH_EMAIL + " TEXT NOT NULL,"
                + BRANCH_APP_ID + " TEXT NOT NULL,"
                + BRANCH_USER_ID + " TEXT NOT NULL)";

        String create_table_favourite = "CREATE TABLE "
                + TABLE_FAVOURITE + "("
                + FAVOURITE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FAVOURITE_PRODUCT_ID + " TEXT NOT NULL,"
                + FAVOURITE_CATEGORY_ID + " TEXT NOT NULL,"
                + FAVOURITE_PRODUCT_NAME + " TEXT NOT NULL,"
                + FAVOURITE_PRODUCT_IMAGE + " TEXT NOT NULL,"
                + FAVOURITE_DISCRIPTION + " TEXT,"
                + FAVOURITE_PRODUCT_TYPE + " TEXT,"
                + FAVOURITE_AMOUNT + " TEXT NOT NULL,"
                + FAVOURITE_DISCOUNT + " TEXT,"
                + FAVOURITE_CGST + " TEXT,"
                + FAVOURITE_SGST + " TEXT,"
                + FAVOURITE_SWACHCHH_BHARAT_TAX + " TEXT)";

        Log.d("Create FAVOURITE DB", create_table_favourite);

        String create_table_cart = "CREATE TABLE "
                + TABLE_CART + "("
                + CART_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CART_PRODUCT_ID + " TEXT NOT NULL,"
                + CART_CATEGORY_ID + " TEXT NOT NULL,"
                + CART_PRODUCT_NAME + " TEXT NOT NULL,"
                + CART_PRODUCT_IMAGE + " TEXT NOT NULL,"
                + CART_DISCRIPTION + " TEXT,"
                + CART_PRODUCT_TYPE + " TEXT NOT NULL,"
                + CART_AMOUNT + " TEXT NOT NULL,"
                + CART_DISCOUNT + " TEXT,"
                + CART_CGST + " TEXT,"
                + CART_SGST + " TEXT,"
                + CART_SWACHCHH_BHARAT_TAX + " TEXT,"
                + CART_PRODUCT_QTY + " TEXT NOT NULL)";
        Log.d("Create Cart ", create_table_cart);

        String create_table_addon = "CREATE TABLE "
                + TABLE_ADDON + "("
                + ADDON_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ADDON_ID + " TEXT NOT NULL,"
                + ADDON_CATEGORY_ID + " TEXT NOT NULL,"
                + ADDON_PRODUCT_NAME + " TEXT NOT NULL,"
                + ADDON_PRODUCT_IMAGE + " TEXT NOT NULL,"
                + ADDON_DISCRIPTION + " TEXT,"
                + ADDON_PRODUCT_TYPE + " TEXT,"
                + ADDON_AMOUNT + " TEXT,"
                + ADDON_DISCOUNT + " TEXT,"
                + ADDON_CGST + " TEXT,"
                + ADDON_SGST + " TEXT,"
                + ADDON_SWACHCHH_BHARAT_TAX + " TEXT,"
                + ADDON_MAIN_PRODUCT_ID + " TEXT NOT NULL,"
                + ADDON_PRODUCT_QTY + " TEXT NOT NULL )";

        String create_table_manage_addon = "CREATE TABLE "
                + TABLE_MANAGE_ADDON + "("
                + MANAGE_ADDON_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MANAGE_ADDON_ID + " TEXT NOT NULL,"
                + MANAGE_ADDON_CATEGORY_ID + " TEXT NOT NULL,"
                + MANAGE_ADDON_PRODUCT_NAME + " TEXT NOT NULL,"
                + MANAGE_ADDON_PRODUCT_IMAGE + " TEXT NOT NULL,"
                + MANAGE_ADDON_DISCRIPTION + " TEXT,"
                + MANAGE_ADDON_PRODUCT_TYPE + " TEXT,"
                + MANAGE_ADDON_AMOUNT + " TEXT,"
                + MANAGE_ADDON_DISCOUNT + " TEXT,"
                + MANAGE_ADDON_CGST + " TEXT,"
                + MANAGE_ADDON_SGST + " TEXT,"
                + MANAGE_ADDON_SWACHCHH_BHARAT_TAX + " TEXT,"
                + MANAGE_ADDON_MAIN_PRODUCT_ID + " TEXT NOT NULL)";

        String create_table_address = "CREATE TABLE "
                + TABLE_ADDRESS + "("
                + ADDRESS_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ADDRESS_ID + " TEXT NOT NULL,"
                + ADDRESS_NAME + " TEXT,"
                + ADDRESS_EMAIL + " TEXT,"
                + ADDRESS_DELIVERY_PHONE + " TEXT,"
                + ADDRESS_HOUSE_NO + " TEXT,"
                + ADDRESS_LANDMARK + " TEXT,"
                + ADDRESS_STREET + " TEXT,"
                + ADDRESS_CITY + " TEXT,"
                + ADDRESS_STATE + " TEXT,"
                + ADDRESS_PIN_CODE + " TEXT)";

        Log.e("Table Created:::", "ADDRESS");

//        String create_table_reordercart = "CREATE TABLE "
//                + TABLE_REORDER_CART + "("
//                + REORDER_CART_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + REORDER_CART_PRODUCT_ID + " TEXT NOT NULL,"
//                + REORDER_CART_CATEGORY_ID + " TEXT NOT NULL,"
//                + REORDER_CART_PRODUCT_NAME + " TEXT NOT NULL,"
//                + REORDER_CART_PRODUCT_IMAGE + " TEXT NOT NULL,"
//                + REORDER_CART_DISCRIPTION + " TEXT,"
//                + REORDER_CART_PRODUCT_TYPE + " TEXT NOT NULL,"
//                + REORDER_CART_AMOUNT + " TEXT NOT NULL,"
//                + REORDER_CART_DISCOUNT + " TEXT,"
//                + REORDER_CART_VAT + " TEXT,"
//                + REORDER_CART_SERVICE_TAX + " TEXT,"
//                + REORDER_CART_SWACHCHH_BHARAT_TAX + " TEXT,"
//                + REORDER_CART_PRODUCT_QTY + " TEXT NOT NULL)";
//        Log.d("Create Reorder Cart ", create_table_reordercart);
//
//        String create_table_reorder_addon = "CREATE TABLE "
//                + TABLE_REORDER_ADDON + "("
//                + REORDER_ADDON_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + REORDER_ADDON_ID + " TEXT NOT NULL,"
//                + REORDER_ADDON_CATEGORY_ID + " TEXT NOT NULL,"
//                + REORDER_ADDON_PRODUCT_NAME + " TEXT NOT NULL,"
//                + REORDER_ADDON_PRODUCT_IMAGE + " TEXT NOT NULL,"
//                + REORDER_ADDON_DISCRIPTION + " TEXT,"
//                + REORDER_ADDON_PRODUCT_TYPE + " TEXT,"
//                + REORDER_ADDON_AMOUNT + " TEXT,"
//                + REORDER_ADDON_DISCOUNT + " TEXT,"
//                + REORDER_ADDON_VAT + " TEXT,"
//                + REORDER_ADDON_SERVICE_TAX + " TEXT,"
//                + REORDER_ADDON_SWACHCHH_BHARAT_TAX + " TEXT,"
//                + REORDER_ADDON_MAIN_PRODUCT_ID + " TEXT NOT NULL,"
//                + REORDER_ADDON_PRODUCT_QTY + " TEXT NOT NULL )";
//        Log.d("Create Reorder addon ", create_table_reorder_addon);

        db.execSQL(create_table_branch);
        db.execSQL(create_table_favourite);
        db.execSQL(create_table_cart);
        db.execSQL(create_table_addon);
        db.execSQL(create_table_manage_addon);
        db.execSQL(create_table_address);
//        db.execSQL(create_table_reordercart);
//        db.execSQL(create_table_reorder_addon);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MANAGE_ADDON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REORDER_CART);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REORDER_ADDON);
        onCreate(db);
    }
}
