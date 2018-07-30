//import javax.jws.soap.SOAPBinding;

public class User {
    private String uid;
    private String password;
    private double balance;

    public User() {}

    public User(String uid, String password, double balance) {
        this.uid = uid;
        this.password = password;
        this.balance = balance;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
