package vn.btec.campusexpensemanagement.entities;

public class Category {

    private int id;

    private String name;

    private String type;

    private String email;


    public Category(int id, String name, String type, String email) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }
    public int getCateId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
