package challenge.two.alc.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import challenge.two.alc.R;
import challenge.two.alc.controllers.FirebaseUtil;
import challenge.two.alc.controllers.NetworkConnection;

public class ListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Button newTourBtn;
    private DrawerLayout drawer;
    private static final String myPhoneNo = "+233557748335";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkNetwork(); //check device network state

        validateUser(); //check current login user

        initializeNavDrawer(toolbar); //initialize the navigation drawer




    }

    private void initializeNavDrawer(Toolbar toolbar) {
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void validateUser() {
        newTourBtn = (Button) findViewById(R.id.newTourBtn);
        if (FirebaseUtil.isAdmin == true) {
            newTourBtn.setVisibility(View.VISIBLE);
        } else {
            newTourBtn.setVisibility(View.INVISIBLE);
        }

        newTourBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this, TourActivity.class));

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_menu:
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseUtil.attachListener();

                            }
                        });
                FirebaseUtil.detachedListener();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachedListener();
    }


    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUtil.openFbReference("traveldeals", this);
        RecyclerView rvDeals = (RecyclerView) findViewById(R.id.rvDeals);
        final TourAdapter adapter = new TourAdapter();
        rvDeals.setAdapter(adapter);
        LinearLayoutManager dealsLayoutManager =
                new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        rvDeals.setLayoutManager(dealsLayoutManager);
        FirebaseUtil.attachListener();

    }



    private void checkNetwork() {
        if (!NetworkConnection.checkInternetConnection(this)) {
            alertNetworkError();
        }

    }

    private void alertNetworkError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.check_internet);
        builder.setTitle(R.string.error_message_no_internet);
        builder.setPositiveButton(R.string.user_confirmation_ok, null);
        builder.create().show();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_booking:
                startActivity(new Intent(this, BookingActivity.class));
                break;
            case R.id.nav_callUs:
                callDeveloper();
                break;
            case R.id.nav_share:
                shareOnSocial();
                break;
            case R.id.nav_feedback:
                sendFeedBack();
                break;
        }
        return true;
    }

    private void callDeveloper() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", myPhoneNo, null));
        startActivity(intent);
    }

    private void shareOnSocial() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.travel_mon_download_text);
        startActivity(shareIntent);
    }

    private void sendFeedBack() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",getString(R.string.my_email), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
    }
}
