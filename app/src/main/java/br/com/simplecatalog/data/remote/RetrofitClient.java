package br.com.simplecatalog.data.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {

    // Requisito: BASE_URL terminando em /
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    private RetrofitClient() {}

    public static ApiService createApiService(boolean debug) {
        OkHttpClient client = buildOkHttpClient(debug,
                10,  // connect timeout
                15,  // read timeout
                15   // write timeout
        );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiService.class);
    }

    // Cliente “agressivo” para forçar timeout mais facilmente
    public static ApiService createTimeoutApiService(boolean debug) {
        OkHttpClient client = buildOkHttpClient(debug,
                2,   // connect timeout
                1,   // read timeout (bem baixo para demonstrar timeout)
                2
        );

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // baseUrl é obrigatório, mesmo usando @Url
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiService.class);
    }

    private static OkHttpClient buildOkHttpClient(
            boolean debug,
            long connectTimeoutSec,
            long readTimeoutSec,
            long writeTimeoutSec
    ) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeoutSec, TimeUnit.SECONDS)
                .readTimeout(readTimeoutSec, TimeUnit.SECONDS)
                .writeTimeout(writeTimeoutSec, TimeUnit.SECONDS);

        if (debug) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        // Exemplo de interceptor “de app” (não obrigatório):
        builder.addInterceptor(chain -> chain.proceed(
                chain.request().newBuilder()
                        .header("X-Debug", "Exercise5")
                        .build()
        ));

        return builder.build();
    }
}
