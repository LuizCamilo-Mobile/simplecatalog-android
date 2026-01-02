package br.com.simplecatalog.data.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitClient centraliza a configuração de rede do app.
 *
 * Responsabilidades:
 * - Criar e configurar o OkHttpClient (timeouts, interceptors, logs)
 * - Criar o Retrofit com baseUrl + Gson converter
 * - Expor uma instância pronta de ApiService
 *
 * Por que isso é útil:
 * - Evita repetir configuração de rede em vários lugares
 * - Facilita observabilidade (logs) e ajustes (timeouts, headers etc.)
 * - Mantém a boundary Remote bem isolada da UI e do domínio
 */
public class RetrofitClient {

    // ApiService pronto para uso pelo Repository
    public final ApiService apiService;

    public RetrofitClient() {
        // 1) Interceptor de logs HTTP (útil em debug e entrevistas)
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 2) OkHttp client configurado (timeouts + interceptors)
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        // 3) Retrofit usando Base URL centralizada + conversor Gson
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiEndpoints.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        // 4) Retrofit cria a implementação da interface ApiService em runtime
        this.apiService = retrofit.create(ApiService.class);
    }
}

