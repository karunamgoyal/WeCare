package kvnb.hostelservicemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Doctor extends AppCompatActivity {
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView messengerTextView;
        TextView des;
        TextView join;
        TextView video;
        CardView card;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.groupname);
            des = itemView.findViewById(R.id.groupdescription);
            messengerTextView = (TextView) itemView.findViewById(R.id.admin);
            join = itemView.findViewById(R.id.join);
            video = itemView.findViewById(R.id.video);
            card = itemView.findViewById(R.id.carddoctor1);
        }
    }

    private static final String TAG = "";
    public static final String MESSAGES_CHILD = "person";
    private static final int REQUEST_INVITE = 1;
    private static final int REQUEST_IMAGE = 2;
    private static final String LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 100;
    public static final String ANONYMOUS = "anonymous";
    private static final String MESSAGE_SENT_EVENT = "message_sent";
    private String mUsername;
    private String mPhotoUrl;
    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;
    ;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Button mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    private ImageView mAddMessageImageView;

    // Firebase instance variables
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Person, Doctor.MessageViewHolder>
            mFirebaseAdapter;
    // private OnFragmentInteractionListener mListener;
    String doctor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUsername = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        mMessageRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setStackFromEnd(false);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<Person> parser = new SnapshotParser<Person>() {
            @Override
            public Person parseSnapshot(DataSnapshot dataSnapshot) {
                Person friendlyMessage = dataSnapshot.getValue(Person.class);
                if (friendlyMessage != null) {
                    friendlyMessage.setId(dataSnapshot.getKey());
                }
                return friendlyMessage;
            }
        };

        DatabaseReference messagesRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD);
        FirebaseRecyclerOptions<Person> options =
                new FirebaseRecyclerOptions.Builder<Person>()
                        .setQuery(messagesRef, parser)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Person, Doctor.MessageViewHolder>(options) {
            @Override
            public Doctor.MessageViewHolder onCreateViewHolder(ViewGroup viewPerson, int i) {

                LayoutInflater inflater = LayoutInflater.from(viewPerson.getContext());
                return new Doctor.MessageViewHolder(inflater.inflate(R.layout.item_doctor, viewPerson, false));
            }

            @Override
            protected void onBindViewHolder(final Doctor.MessageViewHolder viewHolder,
                                            int position,
                                            final Person friendlyMessage) {

                if (friendlyMessage.getName() != null) {
                    Log.d("Checckingmsss", friendlyMessage.getName());
                    viewHolder.messageTextView.setText(friendlyMessage.getName());
                    if ((friendlyMessage.getType() == false)) {
                        Log.d("Checckingmsss", "" + friendlyMessage.getType());
                        viewHolder.des.setVisibility(TextView.GONE);
                        viewHolder.messageTextView.setVisibility(TextView.GONE);
                        viewHolder.messengerTextView.setVisibility(TextView.GONE);
                        viewHolder.join.setVisibility(TextView.GONE);
                        viewHolder.video.setVisibility(TextView.GONE);
                        viewHolder.card.setVisibility(CardView.GONE);
                    }
                }
                viewHolder.des.setText(friendlyMessage.getContact_number());
                viewHolder.join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                        i.putExtra("doctor", friendlyMessage.getId());
                        startActivity(i);
                    }
                });
                viewHolder.video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), VideoChat.class);
                        i.putExtra("doctor", friendlyMessage.getId());
                        startActivity(i);
                    }
                });
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setAdapter(mFirebaseAdapter);


    }


    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseAdapter.stopListening();
    }
}
