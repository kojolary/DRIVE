package net.vokacom.drive.productDetailSection;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.vokacom.drive.GoogleMaps.MapsActivity;
import net.vokacom.drive.ItemClasses.MenActivity;
import net.vokacom.drive.ItemClasses.WomenActivity;
import net.vokacom.drive.MainActivity;
import net.vokacom.drive.R;

import java.util.Objects;

public class DetailedForPurchase extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
    String title;
    String short_description;
    String price;
    String rating;
    String image;
    String type;
    TextView textViewTitletop, textViewId, textViewShortDesc, textViewRating, textViewPrice;
    ImageView imageView;
    LinearLayout map;
    String Name, Number, photo, email, metadata;
    ImageView NavImage;
    TextView NavEmail, NaveText;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    Spinner spinner_amount, spinner_men, spinner_women;
    Double value1 = 0.00;
    TextView amount;
    private String id;

    //@Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //prevent screenshot or screen capture
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //end of it
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);

        //set id for spinner amount
        spinner_amount = findViewById(R.id.quantity_spinner);
        //set id for visible xml
        spinner_men = findViewById(R.id.body_spinner_men);
        spinner_women = findViewById(R.id.body_spinner_women);
        amount = findViewById(R.id.make_amount);
        map = findViewById(R.id.request_for_designer);


        spinner_amount.setOnItemSelectedListener(this);
        map.setOnClickListener(this);


        //Google Firebase for on boot check user
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(DetailedForPurchase.this, MainActivity.class));
                }
            }
        };

        FirebaseUser user = mAuth.getCurrentUser();
        Name = Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName();
       /* Number = String.valueOf(mAuth.getCurrentUser().getPhoneNumber());
        photo = String.valueOf(mAuth.getCurrentUser().getPhotoUrl());*/
        email = mAuth.getCurrentUser().getEmail();

        /*metadata = String.valueOf(mAuth.getCurrentUser().getMetadata());*/


        //receive intent from productAdapter
        Intent intents = getIntent();


        id = intents.getStringExtra("EXTRA_ID");
        title = intents.getStringExtra("EXTRA_TITLE");
        short_description = intents.getStringExtra("EXTRA_DESCRIPTION");
        price = intents.getStringExtra("EXTRA_PRICE");
        rating = intents.getStringExtra("EXTRA_RATING");
        image = intents.getStringExtra("EXTRA_IMAGE");
        type = intents.getStringExtra("TYPE_OF_ACCOUNT");


        textViewTitletop = findViewById(R.id.textViewTitle);
        textViewShortDesc = findViewById(R.id.textViewShortDesc);
        textViewRating = findViewById(R.id.textViewRating);
        textViewPrice = findViewById(R.id.textViewPrice);
        //textViewId = findViewById(R.id.textViewId);
        imageView = findViewById(R.id.imageView2);


        Glide.with(this
        )
                .load(image)
                .into(imageView)
        ;


        textViewTitletop.setText(title);
        //textViewId.setText(id);
        textViewPrice.setText(price);
        textViewRating.setText(rating);
        textViewShortDesc.setText(short_description);

        //imageView.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //add name to navigation item
        View hView = navigationView.getHeaderView(0);
        TextView nav_user = hView.findViewById(R.id.textViewNavigation);
        TextView nav_email = hView.findViewById(R.id.textViewNavigationEmail);
        nav_user.setText(mAuth.getCurrentUser().getDisplayName());
        nav_email.setText(mAuth.getCurrentUser().getEmail());

        //show navigation based on recycler view
        if (type.equals("MEN")) {
            spinner_women.setVisibility(Spinner.GONE); //SHOW the button
        } else if (type.equals("WOMEN")) {
            spinner_men.setVisibility(Spinner.GONE); //SHOW the button

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() ==  R.id.request_for_designer) {
            Intent mapIntent = new Intent(DetailedForPurchase.this, MapsActivity.class);
            startActivity(mapIntent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent a = new Intent(DetailedForPurchase.this, WomenActivity.class);
            startActivity(a);
        } else if (id == R.id.nav_gallery) {

            Intent a = new Intent(DetailedForPurchase.this, MenActivity.class);
            startActivity(a);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = (String) parent.getItemAtPosition(position);

        if (item.length() > 0) {
            //do your work here
            try {
                value1 = Double.valueOf(item);

            } catch (NumberFormatException e) {
                Log.i("Error Conversion", "Cannot make conversion to integar");
            }
            // Log.i("result", number_of_votes.toString());
            // Toast.makeText(ContestantsDetails.this, "Text is:" +number_of_votes.getText(), Toast.LENGTH_SHORT).show();
            // int b = Integer.parseInt(textViewPrice.getText().toString());
            double a = Double.valueOf(textViewPrice.getText().toString());

            double num = value1 * a;

            amount.setText(String.valueOf(num));
            // }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
