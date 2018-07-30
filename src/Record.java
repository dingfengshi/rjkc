import java.sql.Date;

public class Record {
    private String uid;
    private int bid;
    private Date date;

    public Record() {}

    public Record(String uid, int bid, Date date) {
        this.uid = uid;
        this.bid = bid;
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Record{" +
                "uid='" + uid + '\'' +
                ", bid=" + bid +
                ", date=" + date +
                '}';
    }
}
