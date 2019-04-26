package com.loginworks.royaldines.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.loginworks.royaldines.R;
import com.loginworks.royaldines.adapters.BranchAdapter;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.LocalUtilty;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.models.BranchParser;

import java.util.ArrayList;

public class BranchSelection extends AppCompatActivity {

    LocalUtilty localUtilty;
    private String TAG = BranchSelection.this.getClass().getSimpleName();
    private Activity mActivity;
    private RecyclerView branchRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<BranchParser> branchList;
    private BranchAdapter branchAdapter;
    private MyPreferences myPreferences;
    private DataHandlerDB dataHandlerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_selection);

        mActivity = BranchSelection.this;
        myPreferences = MyPreferences.getInstance();
        dataHandlerDB = DataHandlerDB.getInstance();

        localUtilty = LocalUtilty.getInstance();
        findViews();

        try {
            branchList = dataHandlerDB.getBranchData();
            branchAdapter = new BranchAdapter(mActivity, branchList);
            branchRecyclerView.setAdapter(branchAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findViews() {
        branchRecyclerView = (RecyclerView) findViewById(R.id.branchRecycler);
        mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        branchRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        if (myPreferences.getBranchId(mActivity).equals("")) {
            Toast.makeText(mActivity, getResources().getString(R.string.select_branch), Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

}
