package br.com.simplecatalog.data.remote;

import java.util.List;

import br.com.simplecatalog.data.remote.dto.ItemDto;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("posts")
    Call<List<ItemDto>> getItems();
}
