package kvnb.hostelservicemanagement;

public class Person
{
    private String Name;
    private String dob;
    private boolean gender;
    private boolean type;
    private String contact_number;
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    Person(){}

    Person(String Name, String dob, boolean gender, boolean type ,String contact_number)
    {
        this.dob = dob;
        this.gender = gender;
        this.Name = Name;
        this.type = type;
        this.contact_number = contact_number;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getName() { return this.Name ;};
    public String getDob() { return this.dob; };
    public String getContact_number() { return this.contact_number; };
    public boolean getGender() { return this.gender ; };
    public boolean getType() { return this.type ;};

}


