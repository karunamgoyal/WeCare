package kvnb.hostelservicemanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

class Person
{
    private String Name;
    private String dob;
    private boolean gender;
    private boolean type;
    private String contact_number;

    Person(){}
    Person(String Name, String dob, boolean gender, boolean type ,String contact_number)
    {
        this.dob = dob;
        this.gender = gender;
        this.Name = Name;
        this.type = type;
        this.contact_number = contact_number;
    }
}



public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText firstnameEdittext,lastnameEdittext,emailEdittext,passEdittext,passAgainEdittext,birthdayEdittext;
    private EditText contactNumber;
    private RadioGroup genderRadioGroup;
    private Button registerButton;
    private DatePickerDialog datePickerDialog;

    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind views with their ids
        bindViews();

        //Set listeners of views
        setViewActions();

        //Create DatePickerDialog to show a calendar to user to select birthdate
        prepareDatePickerDialog();
    }

    private void bindViews() {
        firstnameEdittext=(EditText)findViewById(R.id.firstname_edittext);
        lastnameEdittext=(EditText)findViewById(R.id.lastname_edittext);
        //emailEdittext=(EditText)findViewById(R.id.email_edittext);
        //passEdittext=(EditText)findViewById(R.id.password_edittext);
        //passAgainEdittext=(EditText)findViewById(R.id.password_again_edittext);
        birthdayEdittext=(EditText)findViewById(R.id.birthday_edittext);
        genderRadioGroup=(RadioGroup)findViewById(R.id.gender_radiogroup);
        registerButton=(Button)findViewById(R.id.register_button);

        //Edited
        contactNumber = (EditText)findViewById(R.id.contact_number);
    }

    private void setViewActions() {
        birthdayEdittext.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private void prepareDatePickerDialog() {
        //Get current date
        Calendar calendar=Calendar.getInstance();

        //Create datePickerDialog with initial date which is current and decide what happens when a date is selected.
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //When a date is selected, it comes here.
                //Change birthdayEdittext's text and dismiss dialog.
                birthdayEdittext.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                datePickerDialog.dismiss();
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void showToastWithFormValues() {

        //Get edittexts values
        String firstname=firstnameEdittext.getText().toString();
        String lastname=lastnameEdittext.getText().toString();
        //String email=emailEdittext.getText().toString();
        //String pass=passEdittext.getText().toString();
        //String passAgain=passAgainEdittext.getText().toString();
        String birthday=birthdayEdittext.getText().toString();
        String contactnumber = contactNumber.getText().toString();

        String name = firstname + lastname;


        //Get gender
        RadioButton selectedRadioButton=(RadioButton)findViewById(genderRadioGroup.getCheckedRadioButtonId());
        String radiogender=selectedRadioButton==null ? "":selectedRadioButton.getText().toString();

        RadioButton typeRadioButton=(RadioButton)findViewById(genderRadioGroup.getCheckedRadioButtonId());
        String typeofperson=selectedRadioButton==null ? "":selectedRadioButton.getText().toString();



        //Check all fields
        if(!firstname.equals("")&&!lastname.equals("")&&!birthday.equals("")&&!radiogender.equals("")){

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
        }
        else{
            Toast.makeText(this,getResources().getString(R.string.no_field_can_be_empty),Toast.LENGTH_SHORT).show();
        }
        boolean type = true;
        boolean gender = true;
        if(radiogender.equals("Female"))
        {
            gender = false;
        }

        if(typeofperson.equals(" am a doctor"))
            type = false;

        String birthdate = birthdayEdittext.toString();

        person = new Person(name,birthdate,gender,type,contactnumber);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.birthday_edittext:
                datePickerDialog.show();
                break;
            case R.id.register_button:
                showToastWithFormValues();
                break;
        }
    }

}
