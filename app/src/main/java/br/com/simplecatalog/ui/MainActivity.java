package br.com.simplecatalog.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.simplecatalog.R;
import br.com.simplecatalog.data.remote.ApiService;
import br.com.simplecatalog.data.remote.RetrofitClient;
import br.com.simplecatalog.data.remote.dto.ItemDto;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EX5_3";
    private TextView txtOutput;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtOutput = findViewById(R.id.txtOutput);
        Button btnRequest = findViewById(R.id.btnRequest);

        api = RetrofitClient.createApiService(true);

        btnRequest.setOnClickListener(v -> fetchItems());
    }

    private void fetchItems() {
        txtOutput.setText("Carregando...");

        executor.execute(() -> {
            Call<List<ItemDto>> call = api.getItems();

            try {
                Response<List<ItemDto>> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    List<ItemDto> items = response.body();

                    Log.d(TAG, "Tamanho da lista: " + items.size());

                    if (!items.isEmpty()) {
                        ItemDto first = items.get(0);
                        Log.d(TAG, "Primeiro item: id=" + first.getId() + " title=" + first.getTitle());
                    }

                    runOnUiThread(() -> txtOutput.setText(
                            "Sucesso!\nItens: " + items.size() +
                                    "\nPrimeiro: " + (items.isEmpty() ? "—" : items.get(0).getTitle())
                    ));
                } else {
                    int code = response.code();
                    Log.e(TAG, "Falha HTTP: " + code);
                    runOnUiThread(() -> txtOutput.setText("Falha HTTP: " + code));
                }

            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage(), e);
                runOnUiThread(() -> txtOutput.setText("Erro de rede: " + e.getMessage()));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
