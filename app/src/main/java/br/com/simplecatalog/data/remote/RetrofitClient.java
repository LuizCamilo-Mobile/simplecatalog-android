package br.com.simplecatalog.data.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public final class RetrofitClient {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    private RetrofitClient() {}

    public static ApiService createApiService(boolean debug) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(debug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)     // precisa terminar em /
                .client(client)        // Retrofit usa OkHttp
                .build();              // sem converter ainda

        return retrofit.create(ApiService.class); // proxy
    }

    public static ApiService createTimeoutApiService(boolean debug) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(debug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)  // bem baixo pra estourar fácil
                .writeTimeout(2, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // obrigatório mesmo usando @Url
                .client(client)
                .build();

        return retrofit.create(ApiService.class);
    }
}
