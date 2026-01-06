package br.com.simplecatalog.data.remote;

import java.util.List;

import br.com.simplecatalog.data.remote.dto.ItemDto;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {

    // Requisito: JSONPlaceholder com @GET("posts")
    @GET("posts")
    Call<List<ItemDto>> getItems();

    // Extra (para simular delay/timeout de forma controlada)
    // Vamos chamar: https://httpstat.us/200?sleep=5000
    @GET
    Call<ResponseBody> slowCall(@Url String fullUrl);
}
