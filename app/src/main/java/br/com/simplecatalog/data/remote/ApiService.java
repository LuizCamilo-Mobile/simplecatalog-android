package br.com.simplecatalog.data.remote;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {

    // Endpoint simples para pegar 1 post cru (JSON como texto)
    @GET("posts/1")
    Call<ResponseBody> getPostRaw();

    // Para simular delay/timeout com URL completa
    @GET
    Call<ResponseBody> slowRaw(@Url String fullUrl);
}
