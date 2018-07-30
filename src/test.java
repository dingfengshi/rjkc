import javax.xml.crypto.Data;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) throws SQLException {
        Mysqlutil Mysqlutil = new Mysqlutil();
        java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
        java.sql.Date now2 = new java.sql.Date(System.currentTimeMillis());
        now2.setMonth(6);
        System.out.println(now2);

        now2.setDate(30);

        System.out.println(Mysqlutil.dateDiff(now, now2));

//        ArrayList<Books> rs = Mysqlutil.selectBookByPrice(10,20);
//        Books book = new Books("a", "b", "c", 1.1, 2);
//        Mysqlutil.insertBook(book);
//////        Mysqlutil.deleteBook(1);
//        User user = new User("s", "123456", 0);
//        Mysqlutil.insertUser(user);
////        Mysqlutil.deleteUser("s");
//        Date date = Date.valueOf("2018-06-03");
//        Record record = new Record("s", 3, date);
//        Mysqlutil.insertRecord(record);
//
//        ArrayList<Record> result = Mysqlutil.selectRecordByBid(3);
//        for (Record i : result) {
//            System.out.println(i);
//        }
    }
}
