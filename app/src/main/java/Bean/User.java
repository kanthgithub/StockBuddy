package Bean;

import java.util.Date;

/**
 * Created by jaipr on 26-02-2017.
 */

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String age;
    private String location;
    private String password;

    public User() {
        this.email = "jaiprajapati3@gmail.com";
        this.firstName = "Jai";
        this.lastName = "Prajapati";
        this.gender = "Male";
        this.age = "21";
        this.location = "Gujarat";
        this.password="jai@123";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}
