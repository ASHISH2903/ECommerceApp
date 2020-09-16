package myapp.myecommerce.myapplication.Model;

public class AdminOrders {

    private String address, ciyt, date, name, phone, state, time, totalAmount;

    public AdminOrders() {
    }

    public AdminOrders(String address, String ciyt, String date, String name, String phone, String state, String time, String totalAmount) {
        this.address = address;
        this.ciyt = ciyt;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.state = state;
        this.time = time;
        this.totalAmount = totalAmount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCiyt() {
        return ciyt;
    }

    public void setCiyt(String ciyt) {
        this.ciyt = ciyt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
