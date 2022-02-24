package Model;

public class User {

    private String Name,Department;
    private int Contact, MasterAccess;

    public User(String Name, String Department, int Contact, int MasterAccess){
        this.Name = this.Name;
        this.Department = this.Department;
        this.Contact = this.Contact;
        this.MasterAccess = this.MasterAccess;
    }

    public User(){
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public int getContact() {
        return Contact;
    }

    public void setContact(int contact) {
        Contact = contact;
    }

    public int getMasterAccess() {
        return MasterAccess;
    }

    public void setMasterAccess(int masterAccess) {
        MasterAccess = masterAccess;
    }

}