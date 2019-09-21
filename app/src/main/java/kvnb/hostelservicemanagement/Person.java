package kvnb.hostelservicemanagement;

public class Person
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


