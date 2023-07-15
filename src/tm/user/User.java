package tm.user;

public class User {
    private String userName;
    private String email;
    private String password;
    private String userType;

    public User() {
    }

    public User(String userName, String email, String password, String userType) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.setUserType(userType);
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
