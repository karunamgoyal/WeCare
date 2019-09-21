package kvnb.hostelservicemanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String ausername;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    TextView name;
    TextView username;
    TextView phone;
    TextView email;
    TextView rollno;
    TextView roomno;
    TextView hno;
   // private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ausername=ParentActivity.getMyData();

        Log.v("checkingprofile","11");
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_profile, container, false);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.v("checkingprofile","12");
        username = (TextView) v.findViewById(R.id.usernameprofile);
        name = (TextView) v.findViewById(R.id.nameprofile);
        rollno = (TextView) v.findViewById(R.id.rollnoprofile);
        roomno = (TextView) v.findViewById(R.id.roomnoprofile);
        hno = (TextView) v.findViewById(R.id.hostelnoprofile);
        phone=v.findViewById(R.id.phoneprofile);
        email=v.findViewById(R.id.emailprofile);
        name.setText(firebaseUser.getDisplayName());
        email.setText(firebaseUser.getEmail());
        phone.setText(firebaseUser.getEmail());


        return v;
    }

}
