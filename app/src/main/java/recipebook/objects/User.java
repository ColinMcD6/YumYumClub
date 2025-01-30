package recipebook.objects;

public class User{
    private String userName;
    private String passWord;
    private String email;
    private boolean loggedIn;


    public User(String userName, String passWord, String email){
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
    }

    public User(String userName, String passWord, String email, boolean loggedIn){
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.loggedIn = loggedIn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return email;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
