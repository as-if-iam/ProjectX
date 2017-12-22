package techbie.projectx;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Asif Ansari on 12/22/17 12:03 PM.
 */

public interface API {
    //2
    @GET("/api/{path}")
    Call<UserDataResponse> callUserList(@Path("path") String path, @Query("page") int page);
}
