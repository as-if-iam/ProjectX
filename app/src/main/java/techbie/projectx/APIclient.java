package techbie.projectx;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Asif Ansari on 12/22/17 12:00 PM.
 */

public class APIclient {

    public static API getApi(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();

                HttpUrl url = original.url().newBuilder().build();
                original = original.newBuilder().url(url).build();
                Request request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            }
        }).addInterceptor(logging).build();


        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl("https://reqres.in")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build();
        return restAdapter.create(API.class);
    }
}
