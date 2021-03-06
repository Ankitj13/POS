package com.ankit.pointofsolution.modules;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.ankit.pointofsolution.MainActivity;
import com.ankit.pointofsolution.Models.Orders;
import com.ankit.pointofsolution.R;
import com.ankit.pointofsolution.adapter.OrdersAdapter;
import com.ankit.pointofsolution.api.ApiManager;
import com.ankit.pointofsolution.config.Messages;
import com.ankit.pointofsolution.storage.DBHelper;
import com.ankit.pointofsolution.storage.Preferences;
import com.ankit.pointofsolution.utility.NetworkOperations;
import com.ankit.pointofsolution.utility.SyncAdapter;
import com.ankit.pointofsolution.utility.Utility;

import java.util.ArrayList;

public class ListofOrdersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    ApiManager apiManager;
    Preferences pref;
    private DBHelper dbHelper;
    private Orders orders;
    private ListView listView;
    private ArrayList<Orders> ordersArrayList;
    private OrdersAdapter ordersAdapter;
    private Utility utility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // create useable objects...
        Resources res = getResources();
        apiManager = new ApiManager(this);
        pref = new Preferences(this);
        dbHelper = new DBHelper(this);
        utility = new Utility();
        listView = (ListView) findViewById(R.id.list);
        ordersArrayList = dbHelper.getAllOrders();
        System.out.println("ordersArrayList:"+ ordersArrayList.size());

        // Defined Array values to show in ListView
        if(savedInstanceState != null){
            ordersArrayList = savedInstanceState.getParcelableArrayList("d");
            ordersAdapter = new OrdersAdapter(this,ordersArrayList,res);
            // Assign adapter to ListView
            listView.setAdapter(ordersAdapter);
            ordersAdapter.notifyDataSetChanged();
        }
        else {
            ordersAdapter = new OrdersAdapter(this,ordersArrayList,res);
            // Assign adapter to ListView
            listView.setAdapter(ordersAdapter);
            ordersAdapter.notifyDataSetChanged();
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }
    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putParcelableArrayList("d", ordersArrayList);
            super.onSaveInstanceState(savedInstanceState);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listof_orders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            // Handle the cart action
            Intent i = new Intent(ListofOrdersActivity.this,MainActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_return) {

        } else if (id == R.id.nav_reports) {

            //working code checks weather orders exist .....
            if(dbHelper.getAllOrders().size()>0) {
                Intent i = new Intent(this, ListofOrdersActivity.class);
                startActivity(i);
            }else
            {
                Toast.makeText(this, Messages.EMPTY_ORDERS,Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.nav_reciept) {

        }else if (id == R.id.nav_customer) {
            Intent i = new Intent(this, CustomerActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_logout) {
            apiManager.logout(pref);
        } else if (id == R.id.nav_help) {
        }
        else if (id == R.id.sync) {
            NetworkOperations noptn = new NetworkOperations(this);
            if (noptn.hasActiveInternetConnection(this)) {
                new SyncAdapter(this).execute();
                /*SyncAdapter syncAdapter = new SyncAdapter(this, "products");
                syncAdapter.execute();
                utility.StartAsyncTaskInParallel(syncAdapter);
                SyncAdapter syncAdapter1 = new SyncAdapter(this, "orders");
                syncAdapter1.execute();
                utility.StartAsyncTaskInParallel(syncAdapter1);
                SyncAdapter syncAdapter2 = new SyncAdapter(this, "customers");
                syncAdapter2.execute();
                utility.StartAsyncTaskInParallel(syncAdapter2);*/
            }
            else {
                Toast.makeText(this,"Please connect Internet.", Toast.LENGTH_LONG).show();
            }
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
