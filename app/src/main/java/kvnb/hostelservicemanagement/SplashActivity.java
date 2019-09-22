package kvnb.hostelservicemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Main-Activity. */
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    DatabaseReference mydbref = FirebaseDatabase.getInstance().getReference().child("person").child(firebaseUser.getUid());
                    mydbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Log.v("checkingtheerror", "114");
                                Person person = dataSnapshot.getValue(Person.class);
                                Intent intent = new Intent(getApplicationContext(), ParentActivity.class);
                                if (person.getType()) {
                                    intent.putExtra("type", "doctor");
                                } else {
                                    intent.putExtra("type", "patient");
                                }
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, SignInActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
