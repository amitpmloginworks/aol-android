package com.loginworks.royaldines.utility;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.loginworks.royaldines.R;
import com.loginworks.royaldines.activity.DashboardActivity;
import com.loginworks.royaldines.activity.Splash;
import com.loginworks.royaldines.extras.FragmentsName;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.fragments.AddAddressFragment;
import com.loginworks.royaldines.fragments.AddressFragment;
import com.loginworks.royaldines.fragments.CategoriesFragment;
import com.loginworks.royaldines.fragments.CheckOutFragment;
import com.loginworks.royaldines.fragments.ConfirmOrderFragment;
import com.loginworks.royaldines.fragments.FoodFragment;
import com.loginworks.royaldines.fragments.GetOTPFragment;
import com.loginworks.royaldines.fragments.OrderHistoryDetailFragment;
import com.loginworks.royaldines.fragments.OrderHistoryFragment;
import com.loginworks.royaldines.fragments.ThankyouFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.loginworks.Vaades.fragments.ReOrderCheckoutFragment;


public class Util {

    private static String userID_file;
    private static Util util = null;
    private File file = null;
    private boolean deleted;
    private boolean delivery = false;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction = null;
    private String TAG = Util.class.getSimpleName();

    public static Util getInstance() {
        if (util == null) {
            util = new Util();
        }
        return util;
    }

    public static String refactorDOB(String dob) {
        String updated_dob = dob.substring(0, 10);
        Log.d(" UPDATED DOB ::", " " + updated_dob);
        return updated_dob;
    }

    public boolean isOnlyLetters(String str) {
//        if (!Pattern.matches(".*[a-zA-Z].*", str))
//            return true;
//        else return false;
        Pattern p = Pattern.compile("^[ A-Za-z]+$");
        Matcher m = p.matcher(str);
        boolean b = m.matches();
        Log.e("MATCHER", "::::::::-----  " + b);
        return b;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    //Logout from teh application
    public void closeApplication(Activity activity) {

        try {
            Intent startMain = new Intent(activity, Splash.class);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(startMain);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Logout from the application
    public void closeAppBackPress(Activity activity) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(startMain);
    }

    //Hiding Soft Keyboard
    public void disableKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void fileCreate(Activity activity, Boolean loginSta, String filename) {
        // write on SD card file data in the text box
        try {
            File myFile_second = new File("/sdcard/" + filename); //"/sdcard/"
            myFile_second.createNewFile();

            FileOutputStream fOut_log = new FileOutputStream(myFile_second);
            OutputStreamWriter myOutWriter_log =
                    new OutputStreamWriter(fOut_log);
            myOutWriter_log.append(String.valueOf(loginSta));
            myOutWriter_log.close();
            fOut_log.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String fileRead(String filename) {
        Boolean read = false;
        try {
            File myFile = new File("/sdcard/" + filename);
            FileInputStream fIn_log = new FileInputStream(myFile);
            BufferedReader myReader_log = new BufferedReader(
                    new InputStreamReader(fIn_log));
            String aDataRow_log = "";
            userID_file = "";
            while ((aDataRow_log = myReader_log.readLine()) != null) {
                userID_file += aDataRow_log + "\n";
            }
            myReader_log.close();

        } catch (FileNotFoundException e) {
            userID_file = null;
            e.printStackTrace();
        } catch (Exception e) {
            userID_file = null;
            e.printStackTrace();
        }
        return userID_file;
    }

    public Boolean deleteLocalFile(String filename) {
        file = new File("/sdcard/" + filename);
        deleted = file.delete();
        Boolean fileDelete = deleted;
        if (file.exists()) {
            fileDelete = false;
        } else {
            fileDelete = true;
        }
        return fileDelete;
    }


    /*private Bitmap decodeFile(File file)
    {
        try
        {
            /*//********************* decode image size ********************
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file), null, options);

            // ********************** Find the correct scale value. It should be the power of 2. ********************
            options.inSampleSize = BitmapConverter.calculateInSampleSize(options, 145, 105);
            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeStream(new FileInputStream(file), null, options);
        }
        catch(FileNotFoundException eFileNotFoundException)
        {

            return null;
        }
    }*/

    //Fragment Transition
    public void openFragment(Context activity, String fragmentName, int fragmentId, Bundle bundle) {
        try {
            boolean session = MyPreferences.getInstance().getSession(activity);

//            fragmentManager = MainNavDrawerActivity.fragmentManager;
            String page = "";
            switch (fragmentId) {
                case FragmentsName.CATEGORIES_ID:
                    fragmentManager = DashboardActivity.fragmentManager;
                    CategoriesFragment categoriesFragment = new CategoriesFragment();
                    if (fragmentManager.getBackStackEntryCount() > 0)
                        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                            fragmentManager.popBackStack();
                        }
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main_nav_drawer, categoriesFragment, fragmentName);
                    fragmentTransaction.addToBackStack(null);
                    break;

                case FragmentsName.FOOD_ID:
                    fragmentManager = DashboardActivity.fragmentManager;
                    FoodFragment foodFragment = new FoodFragment();
                    foodFragment.setArguments(bundle);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main_nav_drawer, foodFragment, fragmentName);
                    fragmentTransaction.addToBackStack(fragmentName);
                    break;

                case FragmentsName.CHECKOUT_ID:
                    fragmentManager = DashboardActivity.fragmentManager;
                    CheckOutFragment checkOutFragment = new CheckOutFragment();
                    checkOutFragment.setArguments(bundle);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main_nav_drawer, checkOutFragment, fragmentName);
                    fragmentTransaction.addToBackStack(fragmentName);
                    break;

                case FragmentsName.OTP_ID:
                    fragmentManager = DashboardActivity.fragmentManager;
                    GetOTPFragment getOTPFragment = new GetOTPFragment();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main_nav_drawer, getOTPFragment, fragmentName);
                    fragmentTransaction.addToBackStack(fragmentName);
                    break;

                case FragmentsName.ADD_ADDRESS_ID:
                    fragmentManager = DashboardActivity.fragmentManager;
                    AddAddressFragment addAddressFragment = new AddAddressFragment();
                    addAddressFragment.setArguments(bundle);

                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main_nav_drawer, addAddressFragment, fragmentName);

//                    if (bundle != null) {
//                        page = bundle.getString("page");
//                        if (page != null && !page.equalsIgnoreCase("otp")) {
//                            fragmentTransaction.addToBackStack(fragmentName);
//                        }
//                    } else {
//                        fragmentTransaction.addToBackStack(fragmentName);
//                    }
                        fragmentTransaction.addToBackStack(fragmentName);

                    break;

                case FragmentsName.CUSTOMER_DELIVERY_ADDRESS_ID:
                    fragmentManager = DashboardActivity.fragmentManager;
                    AddressFragment addressFragment = new AddressFragment();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main_nav_drawer, addressFragment, fragmentName);

//                    if (bundle != null) {
//                        page = bundle.getString("page");
//                        if (page != null && !page.equalsIgnoreCase("otp")) {
//                            fragmentTransaction.addToBackStack(fragmentName);
//                        }
//                    } else {
//                        fragmentTransaction.addToBackStack(fragmentName);
//                    }
                    fragmentTransaction.addToBackStack(fragmentName);

                    break;

                case FragmentsName.CONFIRM_ORDER_ID:
                    fragmentManager = DashboardActivity.fragmentManager;
                    ConfirmOrderFragment confirmOrderFragment = new ConfirmOrderFragment();
                    confirmOrderFragment.setArguments(bundle);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main_nav_drawer, confirmOrderFragment, fragmentName);
//
//                    if (bundle != null) {
//                        page = bundle.getString("page");
//                        if (page != null && !page.equalsIgnoreCase("otp")) {
//                            fragmentTransaction.addToBackStack(fragmentName);
//                        }
//                    } else {
//                        fragmentTransaction.addToBackStack(fragmentName);
//                    }
                    fragmentTransaction.addToBackStack(fragmentName);

                    break;

                case FragmentsName.THANK_YOU_ID:
                    fragmentManager = DashboardActivity.fragmentManager;
                    if (fragmentManager.getBackStackEntryCount() > 0)
                        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                            fragmentManager.popBackStack();
                        }
                    ThankyouFragment thankyouFragment = new ThankyouFragment();
                    thankyouFragment.setArguments(bundle);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main_nav_drawer, thankyouFragment, fragmentName);
                    break;

                case FragmentsName.ORDER_HISTORY_ID:
                    fragmentManager = DashboardActivity.fragmentManager;
                    if (fragmentManager.getBackStackEntryCount() > 0)
                        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                            fragmentManager.popBackStack();
                        }
                    OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
                    orderHistoryFragment.setArguments(bundle);
                    // .addSharedElement(imageView, ViewCompat.getTransitionName(imageView))
                    // fragmentTransaction.addSharedElement(sharedElement, transitionName);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main_nav_drawer, orderHistoryFragment, fragmentName);
                    fragmentTransaction.addToBackStack(null);
                    break;

                case FragmentsName.ORDER_HISTORY_DETAIL_ID:

                    fragmentManager = DashboardActivity.fragmentManager;
                    OrderHistoryDetailFragment historyDetailFragment = new OrderHistoryDetailFragment();
                    historyDetailFragment.setArguments(bundle);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main_nav_drawer, historyDetailFragment, fragmentName);
                    fragmentTransaction.addToBackStack(fragmentName);
                    break;
            }
            fragmentTransaction.commit();

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private InputStream OpenHttpConnection(String urlString)
            throws IOException {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            throw new IOException("Error connecting");
        }
        return in;
    }

    public Bitmap downloadImage(String URL) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(ServiceURL.BASE_IMAGE_PATH + URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }

}
