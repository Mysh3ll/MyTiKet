package mysh3ll.loginapi.Interface.Order;


import mysh3ll.loginapi.POJO.CodeUnique.UserCodeUniqueList;
import mysh3ll.loginapi.POJO.Order.UserOrderList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Michel on 09/02/2017.
 */

public interface UserOrders {

    @GET("user/orders")
    Call<UserOrderList> getOrders();

    @GET("user/order/{idEvent}")
    Call<UserCodeUniqueList> getCodeUnique(@Path("idEvent") String idEvent);

}
