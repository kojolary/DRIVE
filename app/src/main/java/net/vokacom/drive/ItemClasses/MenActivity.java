package net.vokacom.drive.ItemClasses;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import net.vokacom.drive.MainActivity;
import net.vokacom.drive.R;
import net.vokacom.drive.mData.Product;
import net.vokacom.drive.mData.ProductMen;
import net.vokacom.drive.mListView.ProductAdapter;
import net.vokacom.drive.mListView.ProductAdapterMen;
import net.vokacom.drive.productDetailSection.DetailedForPurchase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProductAdapterMen.OnItemClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String Stringname="Name";
    String Stringemail="Email";

    //@Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private String URL = "https://app.tedxafariwaa.com/LoginandRegistration/products_men.php";

    //a list to store all the products
// Creating Progress dialog.
    ProgressDialog progressDialog;
    //a list to store all the products
    private List<ProductMen> productListMen;
    //the recyclerview
    private RecyclerView rV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //prevent screenshot or screen capture
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //end of it

        setContentView(R.layout.activity_men);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAuth= FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(MenActivity.this,MainActivity.class));
                }
            }
        };

        //getting the recyclerview from xml
        rV = findViewById(R.id.listRecycleView);
        rV.setHasFixedSize(true);
        rV.setLayoutManager(new LinearLayoutManager(this));



        //initializing the productlist
        productListMen = new ArrayList<>();



        //this method will fetch and parse json
        //to display it in recyclerview
        load_data_from_server();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //add name to navigation item
        View hView = navigationView.getHeaderView(0);
        TextView nav_user= hView.findViewById(R.id.textViewNavigation);
        TextView nav_email= hView.findViewById(R.id.textViewNavigationEmail);
        nav_user.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
        nav_email.setText(mAuth.getCurrentUser().getEmail());


    }

    private void load_data_from_server() {

        //get string response using url
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @SuppressLint("ShowToast")
            @Override
            public void onResponse(String response) {

                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting product object from json array
                        JSONObject productMen = array.getJSONObject(i);

                        //adding the product to product list
                        productListMen.add(new ProductMen(
                                productMen.getInt("id"),
                                productMen.getString("title"),
                                productMen.getString("short_description"),
                                productMen.getString("rating"),
                                productMen.getString("price"),
                                productMen.getString("image")

                        ));
                    }

                    //creating adapter object and setting it to recyclerview
                    ProductAdapterMen adapter = new ProductAdapterMen(getApplication(), productListMen);

                    rV.setAdapter(adapter);
                    adapter.setOnItemClickListener(MenActivity.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MenActivity.this, "Unable to get data, internet", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Hiding the progress dialog after all task complete.
                        // Show timeout error message
                        Toast.makeText(getApplication(),
                                error + "Oops. Timeout error!",
                                Toast.LENGTH_LONG).show();
                    }
                } else if (error instanceof NetworkError) {

                    // Show timeout error message
                    Toast.makeText(getApplication(),
                            "Oops. Network error!",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    // Hiding the progress dialog after all task complete.
                    // Show timeout error message
                    Toast.makeText(getApplication(),
                            "Oops. Server error!",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    // Show timeout error message
                    Toast.makeText(getApplication(),
                            "Oops. Authentication error!",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    // Show timeout error message
                    Toast.makeText(getApplication(),
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    // Show timeout error message
                    Toast.makeText(getApplication(),
                            "Oops. No Network error!",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof TimeoutError) {
                    // Show timeout error message
                    Toast.makeText(getApplication(),
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        //instantiate request queue
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getApplication()));

        //add request to request queue
        requestQueue.add(stringRequest);


    }


    @Override
    public void OnItemClick(int position) {
        ProductMen clickedItem = productListMen.get(position);
        Intent myIntents;
        myIntents = new Intent(getApplicationContext(), DetailedForPurchase.class);


        myIntents.putExtra("EXTRA_ID", clickedItem.getId());
        myIntents.putExtra("EXTRA_TITLE", clickedItem.getTitle());
        myIntents.putExtra("EXTRA_DESCRIPTION", clickedItem.getshort_description());
        myIntents.putExtra("EXTRA_PRICE", clickedItem.getPrice());
        myIntents.putExtra("EXTRA_RATING", clickedItem.getRating());
        myIntents.putExtra("EXTRA_IMAGE", clickedItem.getImage());
        myIntents.putExtra("TYPE_OF_ACCOUNT", "MEN");

        startActivity(myIntents);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);


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
                    mAuth.signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent a = new Intent(MenActivity.this, WomenActivity.class);
            startActivity(a);
        } else if (id == R.id.nav_gallery) {
//            Intent a = new Intent(MenActivity.this, MenActivity.class);
//            startActivity(a);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
