package mysh3ll.loginapi.POJO.Order;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserOrderList {

    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}