//import org.omg.CORBA.PUBLIC_MEMBER;

import javax.swing.*;
import java.awt.print.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class Mysqlutil {
    /*借书上限*/
    static public int bookLimit = 3;
    /*借书时限（天）*/
    static public int timeout = 1;

    private Connection getConnection() {
        try {
            Properties props = new Properties();
            props.put("user", "root");
            props.put("password", "shujuku");
            props.put("useUnicode", "true");
            props.put("useServerPrepStmts", "false"); // use client-side prepared statement
            props.put("characterEncoding", "UTF-8"); // ensure charset is utf8 here
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:Mysql://127.0.0.1:3306/bookstore", props);
            return conn;
        } catch (Exception e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }
        return null;
    }


    /**
     * BOOK插入API
     * BID自动生成，所以book对象的BID可以为空
     *
     * @return 成功为1, 失败为0
     */

    public int insertBook(Books book) {
        Connection conn = getConnection();
        String sql = "insert into books(bname,author,publisher,price,quantity) values(?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, book.getBname());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setDouble(4, book.getPrice());
            stmt.setInt(5, book.getQuantity());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }
        return 0;
    }


    /**
     * BOOK删除API
     * <p>
     * 根据书号删除书本
     *
     * @param bid
     * @return 1为成功，0为失败
     */
    public int deleteBook(int bid) {
        Connection conn = getConnection();
        String sql = "delete from books where bid= ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bid);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }
        return 0;
    }


    /**
     * BOOK选择API
     *
     * @return ArrayList<Books>
     */
    public ArrayList<Books> selectBookByName(String name) {
        return selectBook("select * from books where bname LIKE '%" + name + "%'");
    }

    public Books selectBookById(int id) {
        ArrayList<Books> rs = selectBook("select * from books where bid = '" + String.valueOf(id) + "'");
        if (rs.size() > 0) {
            return rs.get(0);
        } else {
            return null;
        }
    }

    public ArrayList<Books> selectBookByAuthor(String name) {
        return selectBook("select * from books where author = '" + name + "'");
    }

    public ArrayList<Books> selectBookByPublisher(String name) {
        return selectBook("select * from books where publisher = '" + name + "'");
    }

    public ArrayList<Books> selectBookByPrice(double low, double high) {
        return selectBook("select * from books where price >= " + low + " and price <= " + high);
    }

    private ArrayList<Books> selectBook(String s) {
        ArrayList<Books> resultlist = new ArrayList<>();
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement(); //创建Statement对象

            String sql = s;    //要执行的SQL
            ResultSet rs = stmt.executeQuery(sql);//创建数据对象
            while (rs.next()) {
                Books book = new Books(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getInt(6));
                resultlist.add(book);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultlist;
    }


    /**
     * 插入用户API
     *
     * @param user
     * @return 成功为1, 失败为0
     */

    public int insertUser(User user) {
        Connection conn = getConnection();
        String sql = "insert into user values(?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUid());
            stmt.setString(2, user.getPassword());
            stmt.setDouble(3, user.getBalance());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }
        return 0;
    }

    /**
     * 删除用户API
     *
     * @param uid
     * @return 成功为1, 失败为0
     */

    public int deleteUser(String uid) {
        Connection conn = getConnection();
        String sql = "delete from user where uid= ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }
        return 0;
    }


    /**
     * 用户查询api
     *
     * @param uid
     * @param password
     * @return 匹配成功，为对应的User对象，若匹配失败，为NULL
     */

    public User selectUser(String uid, String password) {
        Connection conn = getConnection();
        String sql = "select * from user where uid = ? and password = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(rs.getString(1), rs.getString(2), rs.getDouble(3));
                return user;
            }

        } catch (SQLException e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }
        return null;
    }

    private User selectUserBlance(String uid) {
        Connection conn = getConnection();
        String sql = "select * from user where uid = ? ";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(rs.getString(1), rs.getString(2), rs.getDouble(3));
                return user;
            }

        } catch (SQLException e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }
        return null;
    }


    /**
     * 记录插入API
     *
     * @param record
     * @return 成功返回1, 失败返回:
     * 已经借阅该书   0
     * 借阅超限      -1
     * 书本余量不足   -2
     * 余额不足      -3
     * 数据插入失败   -4
     */
    public int insertRecord(Record record) {
        Connection conn = getConnection();
        try {
            /*检查是否已经借阅*/
            ArrayList<Record> records = selectRecordByUid(record.getUid());
            for (Record r : records) {
                if (r.getBid() == record.getBid()) {
                    return 0;
                }
            }

            /*检查书籍是否还有剩余*/
            Books left = selectBookById(record.getBid());
            if (left.getQuantity() == 0) {
                return -2;
            }

            /*检查用户余额*/
            User user = selectUserBlance(record.getUid());
            if (user.getBalance() < 0) {
                return -3;
            }

            /*添加记录*/
            String sql = "insert into borrow values(?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, record.getUid());
            stmt.setInt(2, record.getBid());
            stmt.setDate(3, record.getDate());
            if (stmt.executeUpdate() > 0) {
                sql = "update books set quantity=? where bid=" + String.valueOf(record.getBid());
                stmt = conn.prepareStatement(sql);
                Books b = selectBookById(record.getBid());
                stmt.setInt(1, b.getQuantity() - 1);
                return stmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }
        return -4;
    }

    /**
     * 删除记录API
     *
     * @param uid，bid
     * @return 成功为1, 失败为0
     */

    public int deleteRecord(String uid, int bid) {
        Connection conn = getConnection();
        Mysqlutil utill = new Mysqlutil();
        String sql = "select * from borrow where uid= ? and bid = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            stmt.setInt(2, bid);
            Record record = utill.selectRecordByUidAndBid(uid, bid);
            if (record == null) {
                return 0;
            }

            User user = selectUserBlance(uid);
            Books book = selectBookById(bid);

            /*书籍数量+1*/
            sql = "update books set quantity=? where bid=" + String.valueOf(bid);
            stmt = conn.prepareStatement(sql);
            Books b = selectBookById(bid);
            stmt.setInt(1, b.getQuantity() + 1);
            stmt.executeUpdate();

            /*检查扣费*/
            java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
            int lastTime = dateDiff(record.getDate(), now);
            if (lastTime > timeout) {
                sql = "update user set balance=? where uid= ?";
                stmt = conn.prepareStatement(sql);
                double cost = Math.round(((lastTime - timeout) * 0.01 * book.getPrice()) * 100) / 100;
                stmt.setDouble(1, user.getBalance() - cost);
                stmt.setString(2, uid);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "您已超过归还时间，已扣费了" + cost + "元", "归还超时", JOptionPane.INFORMATION_MESSAGE);

            }

            /*删除记录*/
            sql = "delete from borrow where uid= ? and bid = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            stmt.setInt(2, bid);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }
        return 0;
    }

    /**
     * 计算天数差
     *
     * @param date1
     * @param date2
     * @return date1于date2的天数差
     */

    public int dateDiff(Date date1, Date date2) {
        Calendar cdate1 = Calendar.getInstance();
        Calendar cdate2 = Calendar.getInstance();
        cdate1.setTime(date1);
        cdate2.setTime(date2);
        setTimeToMidnight(cdate1);
        setTimeToMidnight(cdate2);
        long date1Ms = cdate1.getTimeInMillis();
        long date2Ms = cdate2.getTimeInMillis();
        long intervalMs = date1Ms - date2Ms;
        return Math.abs(millisecondsToDays(intervalMs)) - 1;
    }

    private int millisecondsToDays(long intervalMs) {
        return (int) (intervalMs / (1000 * 86400));
    }

    private void setTimeToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }


    /**
     * 借书记录查询API
     *
     * @return ArrayList<Record>
     */

    public ArrayList<Record> selectRecordByUid(String uid) {
        return selectRecord("select * from borrow where uid = '" + uid + "'");
    }

    public ArrayList<Record> selectRecordByBid(int Bid) {
        return selectRecord("select * from borrow where bid = '" + Bid + "'");
    }

    public Record selectRecordByUidAndBid(String uid, int bid) {
        ArrayList<Record> rs = selectRecord("select * from borrow where bid = '" + bid + "' and uid = '" + uid + "'");
        if (rs.size() > 0) {
            return rs.get(0);
        } else {
            return null;
        }
    }


    public ArrayList<Record> selectRecord(String s) {
        ArrayList<Record> resultlist = new ArrayList<>();
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            String sql = s;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Record record = new Record(rs.getString(1), rs.getInt(2), rs.getDate(3));
                resultlist.add(record);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultlist;
    }

    public int recharge(String uid, double value) {
        try {
            Connection conn = getConnection();
            String sql = "update user set balance=? where uid= ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            User user = selectUserBlance(uid);
            stmt.setDouble(1, user.getBalance() + value);
            stmt.setString(2, uid);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

