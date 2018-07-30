public class Books {
    private int bid;//编号
    private String bname;//书名
    private String author;//作者
    private String publisher;//出版社
    private double price;//单价
    private int quantity;//数量

    public Books() {

    }

    public Books(int bid, String bname, String author, String publisher, double price, int quantity) {
        this.bid = bid;
        this.bname = bname;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.quantity = quantity;
    }

    public Books(String bname, String author, String publisher, double price, int quantity) {
        this.bname = bname;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.quantity = quantity;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
