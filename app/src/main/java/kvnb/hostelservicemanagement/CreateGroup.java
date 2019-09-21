package kvnb.hostelservicemanagement;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_group);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        final TextView groupname = findViewById(R.id.groupname1);
        final TextView groupdes = findViewById(R.id.groupdes);
        Button b = (Button) findViewById(R.id.buttoncom);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatabaseReference messagesRef = mFirebaseDatabaseReference.child("groups");
                    RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
                    String name = groupname.getText().toString();
                    String des = groupdes.getText().toString();
                    String key = messagesRef.push().getKey();
                    Log.v("checkingtheerror","107");
                    if (!(name.equals("") || des.equals(""))) {

                        Log.v("checkingtheerror","123");
                        Group group = new Group(name,des, FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                        messagesRef.child(key).setValue(group);
                        Log.v("checkingtheerror","126");
                        Snackbar.make(v, "Group Registered", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Snackbar.make(v, "Please Fill Complete Details", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }


                } catch (Exception e) {
                    Snackbar.make(v, "Please Fill Complete Details", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}
