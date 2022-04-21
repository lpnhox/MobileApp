package vu.htr.cs.muzikapp.login;

public class User {
    private String userName,email,phone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public User(String userName, String email, String phone) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
    }

    public User() {
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
