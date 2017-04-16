package mysh3ll.loginapi.Interface;

import mysh3ll.loginapi.POJO.Ticket;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Michel on 03/02/2017.
 */

public interface ValidationTicket {

    @GET("ticket/{codeUnique}")
    Call<Ticket> getTicket(@Path("codeUnique") String codeUnique);
}
