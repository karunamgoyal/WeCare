package kvnb.hostelservicemanagement;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private Uri photoUrl;
    private EditText firstnameEdittext, lastnameEdittext, birthdayEdittext;
    private EditText contactNumber;
    private RadioGroup genderRadioGroup;
    private Button registerButton;
    private DatePickerDialog datePickerDialog;
    private RadioGroup doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        user = FirebaseAuth.getInstance().getCurrentUser();
        bindViews();
        setViewActions();
        prepareDatePickerDialog();
        loadimage();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = user.getDisplayName();
                    String phno = contactNumber.getText().toString();
                    String dob = birthdayEdittext.getText().toString();
                    boolean gender;
                    DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference messagesRef = mFirebaseDatabaseReference.child("person").child(user.getUid());
                    boolean type;
                    if (genderRadioGroup.getCheckedRadioButtonId() == R.id.male)
                        gender = true;
                    else
                        gender = false;
                    if (doctor.getCheckedRadioButtonId() == R.id.doctor)
                        type = true;
                    else
                        type = false;
                    Person person = new Person(name, dob, gender, type, phno);
                    messagesRef.setValue(person);

                    Intent intent = new Intent(getApplicationContext(), ParentActivity.class);
                    if (person.getType()) {
                        intent.putExtra("type", "doctor");
                    } else {
                        intent.putExtra("type", "patient");

                    }
                    Snackbar.make(v, "Successful", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Snackbar.make(v, "Error", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    private void loadimage() {


        if (user != null) {
            photoUrl = user.getPhotoUrl();
        }
        //Initialize ImageView
        CircleImageView imageView = (CircleImageView) findViewById(R.id.profile_picture);
        //Loading image from below URL into imageView
        Picasso.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.default_profile_pic)
                .resize(200, 200)
                .centerCrop()
                .into(imageView);
    }


    private void bindViews() {
        firstnameEdittext = (EditText) findViewById(R.id.firstname_edittext);
        lastnameEdittext = (EditText) findViewById(R.id.lastname_edittext);
        birthdayEdittext = (EditText) findViewById(R.id.birthday_edittext);
        genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radiogroup);
        registerButton = (Button) findViewById(R.id.register_button);
        contactNumber = (EditText) findViewById(R.id.contact_number);
        doctor = findViewById(R.id.typeradiogroup);
    }

    private void setViewActions() {
        birthdayEdittext.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private void prepareDatePickerDialog() {
        //Get current date
        Calendar calendar = Calendar.getInstance();

        //Create datePickerDialog with initial date which is current and decide what happens when a date is selected.
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //When a date is selected, it comes here.
                //Change birthdayEdittext's text and dismiss dialog.
                birthdayEdittext.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                datePickerDialog.dismiss();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void showToastWithFormValues() {

        //Get edittexts values
        String firstname = firstnameEdittext.getText().toString();
        String lastname = lastnameEdittext.getText().toString();
        String name = firstname + lastname;
        String birthday = birthdayEdittext.getText().toString();
        String contactnumber = contactNumber.getText().toString();


        //Get gender
        RadioButton selectedRadioButton = (RadioButton) findViewById(genderRadioGroup.getCheckedRadioButtonId());
        String radiogender = selectedRadioButton == null ? "" : selectedRadioButton.getText().toString();

        RadioButton typeRadioButton = (RadioButton) findViewById(genderRadioGroup.getCheckedRadioButtonId());
        String typeofperson = selectedRadioButton == null ? "" : selectedRadioButton.getText().toString();

        //Check all fields
        if (!firstname.equals("") && !lastname.equals("") && !birthday.equals("") && !radiogender.equals("")) {

            //Check if pass and passAgain are the same
            /*
            if(pass.equals(passAgain)){

                //Everything allright
                Toast.makeText(this,getResources().getString(R.string.here_is_values,("\nFirstname:"+firstname+"\nLastname:"+lastname+"\nEmail:"+email+"\nBirthday:"+birthday+"\nGender:"+gender)),Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,getResources().getString(R.string.passwords_must_be_the_same),Toast.LENGTH_SHORT).show();
            }

             */
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_field_can_be_empty), Toast.LENGTH_SHORT).show();
        }
        boolean type = true;
        boolean gender = true;
        if (radiogender.equals("Female")) {
            gender = false;
        }

        if (typeofperson.equals(" am a doctor"))
            type = false;

        String birthdate = birthdayEdittext.toString();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.birthday_edittext:
                datePickerDialog.show();
                break;
            case R.id.register_button:
                showToastWithFormValues();
                break;
        }
    }

}
