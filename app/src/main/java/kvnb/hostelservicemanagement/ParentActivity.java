package kvnb.hostelservicemanagement;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static String ausername;
    private String str = "Message Checking";
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private static FloatingActionButton fab;

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseReference mydb = FirebaseDatabase.getInstance().getReference().child("notices");
        mydb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent intentNotification = new Intent();

                PendingIntent pendingintent = PendingIntent.getActivity(ParentActivity.this, 0, intentNotification, 0);
                Notification noti = new Notification.Builder(ParentActivity.this).setTicker("Ticker")
                        .setContentTitle("New Noticies")
                        .setContentText("Check your Noticies")
                        .setSmallIcon(R.drawable.logo)
                        .setContentIntent(pendingintent).getNotification();
                noti.flags = Notification.FLAG_AUTO_CANCEL;
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0, noti);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent in = getIntent();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ausername = firebaseUser.getUid().toString();
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        //Add fragment
        viewPageAdapter.AddFragment(new NoticeFragment(), "");
        viewPageAdapter.AddFragment(new NewsFragment(), "");
        viewPageAdapter.AddFragment(new ProfileFragment(), "");
        viewPager.setAdapter(viewPageAdapter);


        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_favorite1);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_complaint);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_account);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final int MY_CAMERA_REQUEST_CODE = 100;

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 22);
        }
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
        getMenuInflater().inflate(R.menu.parent, menu);
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
            Intent intent = new Intent(this, AppSettings.class);
            intent.putExtra(str, ausername);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    //attaching activities to drawer
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } /*else if (id == R.id.nav_gallery) {
            Intent in = new Intent(this, ChatActivity.class);
            startActivity(in);
        } */ else if (id == R.id.nav_slideshow) {
            FirebaseAuth.getInstance().signOut();
            Intent I = new Intent(this, SignInActivity.class);
            startActivity(I);
        } /*else if (id == R.id.nav_manage) {
            Intent in = new Intent(this, StepCounter.class);
            startActivity(in);

        }*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static FloatingActionButton getmyButton() {
        return fab;
    }

    public static String getMyData() {
        return ausername;
    }
}
