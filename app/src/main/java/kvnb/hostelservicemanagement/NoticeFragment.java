package kvnb.hostelservicemanagement;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.SecureRandom;


public class NoticeFragment extends Fragment {
    View v;
    public static final String MESSAGES_CHILD = "notices";
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<FriendlyMessage1, NoticeFragment.MessageViewHolder>
            mFirebaseAdapter,myadapter;

    public NoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Checkingmsg", "11");

    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_notice, container, false);
        mMessageRecyclerView = (RecyclerView) v.findViewById(R.id.messageRecyclerView1);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setStackFromEnd(false);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<FriendlyMessage1> parser = new SnapshotParser<FriendlyMessage1>() {
            @Override
            public FriendlyMessage1 parseSnapshot(DataSnapshot dataSnapshot) {
                FriendlyMessage1 friendlyMessage = dataSnapshot.getValue(FriendlyMessage1.class);
                if (friendlyMessage != null) {
                    Log.v("Checkingmsg", "15");
                    friendlyMessage.setId(dataSnapshot.getKey());
                }
                return friendlyMessage;
            }
        };
        Log.v("Checkingmsg", "12");
        DatabaseReference messagesRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD);
        FirebaseRecyclerOptions<FriendlyMessage1> options =
                new FirebaseRecyclerOptions.Builder<FriendlyMessage1>()
                        .setQuery(messagesRef, parser)
                        .build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<FriendlyMessage1, NoticeFragment.MessageViewHolder>(options) {
            @Override
            public NoticeFragment.MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                Log.v("Checkingmsg", "15");
                LayoutInflater inflater = LayoutInflater.from(getContext());
                Log.v("Checkingmsg", "15");
               View v1=inflater.inflate(R.layout.item_card, viewGroup, false);
                Log.v("Checkingmsg", "16");
               return new NoticeFragment.MessageViewHolder(v1);
            }

            @Override
            protected void onBindViewHolder(final NoticeFragment.MessageViewHolder viewHolder,
                                            int position,
                                            FriendlyMessage1 friendlyMessage) {



                if (friendlyMessage.getText() != null) {
                    Log.v("Checkingmsg", "17");

                    //viewHolder.imageview.setImageResource(getimage());
                    //viewHolder.imageview.setVisibility(ImageView.VISIBLE);
                    viewHolder.messageTextView.setText(friendlyMessage.getText());
                    viewHolder.cardview.setVisibility(CardView.VISIBLE);
                    viewHolder.cardview.setCardBackgroundColor(Color.parseColor("#f2f2f2"));
                    viewHolder.messengerTextView.setVisibility(TextView.VISIBLE);
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);

                }

                viewHolder.messengerTextView.setText(friendlyMessage.getName());
                //viewHolder.messengerTextView.setVisibility(TextView.GONE);
                viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewHolder.messengerTextView.getVisibility() == TextView.GONE) {

                            viewHolder.messengerTextView.setVisibility(TextView.VISIBLE);
                          //   viewHolder.imageview.setVisibility(ImageView.GONE);

                        } else {
                            //viewHolder.imageview.setVisibility(ImageView.VISIBLE);
                            viewHolder.messengerTextView.setVisibility(TextView.GONE);
                        }
                    }

                });


            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                Log.v("Checkingmsg", "16");
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
        Log.v("Checkingmsg", "13");
        return v;

    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        CardView cardview;
        TextView messengerTextView;

       // ImageView imageview;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.cardtext1);

            cardview = (CardView) itemView.findViewById(R.id.cardview12);
            messengerTextView = (TextView) itemView.findViewById(R.id.cardtext2);
           // imageview = (ImageView) itemView.findViewById(R.id.imageView4);
        }
    }
    ///get any random image
    public int getimage(){
        SecureRandom r=new SecureRandom();
        int [] array={R.drawable.noticis,R.drawable.noicie,R.drawable.noicie,R.drawable.noticis};
        int i=r.nextInt(3);
        return array[i];

    }
}
