package kvnb.hostelservicemanagement;

public class Group {
    private String groupname;
    private String groupdes;
    private String admin;
    private String id;

    public String getGroupname() {
        return groupname;
    }

    public String getGroupdes() {
        return groupdes;
    }

    public String getAdmin() {
        return admin;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public void setGroupdes(String groupdes) {
        this.groupdes = groupdes;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Group() {
    }

    public Group(String groupname, String groupdes, String admin) {
        this.groupname = groupname;
        this.groupdes = groupdes;
        this.admin = admin;
    }
}
