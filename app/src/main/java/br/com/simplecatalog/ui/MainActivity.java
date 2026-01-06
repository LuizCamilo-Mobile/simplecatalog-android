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

import br.com.simplecatalog.data.remote.ApiService;
import br.com.simplecatalog.data.remote.RetrofitClient;
import br.com.simplecatalog.data.remote.dto.ItemDto;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import br.com.simplecatalog.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EX5_NETWORK";

    private TextView txtOutput;
    private ExecutorService executor;

    private ApiService api;         // normal
    private ApiService apiTimeout;  // timeouts baixos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtOutput = findViewById(R.id.txtOutput);
        Button btnRequest = findViewById(R.id.btnRequest);
        Button btnTimeout = findViewById(R.id.btnTimeout);

        executor = Executors.newSingleThreadExecutor();

        boolean debug = true; // em entrevista: você explicaria que BuildConfig.DEBUG seria o ideal
        api = RetrofitClient.createApiService(debug);
        apiTimeout = RetrofitClient.createTimeoutApiService(debug);

        btnRequest.setOnClickListener(v -> fetchPosts());
        btnTimeout.setOnClickListener(v -> simulateTimeout());
    }

    private void fetchPosts() {
        txtOutput.setText("Carregando posts...");

        executor.execute(() -> {
            Call<List<ItemDto>> call = api.getItems();

            try {
                // Requisito: execute() síncrono em background
                Response<List<ItemDto>> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    List<ItemDto> items = response.body();

                    Log.d(TAG, "SUCESSO: itens=" + items.size());
                    if (!items.isEmpty()) {
                        ItemDto first = items.get(0);
                        Log.d(TAG, "Primeiro item: id=" + first.getId()
                                + " title=" + first.getTitle());
                    }

                    runOnUiThread(() -> txtOutput.setText(
                            "Sucesso!\nItens: " + items.size() +
                                    "\nPrimeiro title: " + (!items.isEmpty() ? items.get(0).getTitle() : "—")
                    ));
                } else {
                    int code = response.code();
                    String msg = "HTTP " + code;

                    Log.e(TAG, "FALHA HTTP: " + msg);

                    runOnUiThread(() -> txtOutput.setText("Falha: " + msg));
                }

            } catch (IOException e) {
                // Aqui caem: sem internet, DNS, timeout, conexão recusada...
                Log.e(TAG, "ERRO IO: " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
                runOnUiThread(() -> txtOutput.setText("IOException: " + e.getMessage()));
            }
        });
    }

    private void simulateTimeout() {
        txtOutput.setText("Simulando timeout...");

        executor.execute(() -> {
            // endpoint que dorme 5s; nosso readTimeout está em 1s -> deve estourar
            String slowUrl = "https://httpstat.us/200?sleep=5000";
            Call<ResponseBody> call = apiTimeout.slowCall(slowUrl);

            try {
                Response<ResponseBody> response = call.execute();

                // Se não deu timeout (rede rápida + configs diferentes), ainda loga o resultado
                Log.d(TAG, "Resposta slowCall: HTTP " + response.code());

                runOnUiThread(() -> txtOutput.setText(
                        "slowCall retornou HTTP " + response.code() + " (se não deu timeout, aumente sleep)"
                ));

            } catch (IOException e) {
                Log.e(TAG, "TIMEOUT/IO esperado: " + e.getClass().getSimpleName()
                        + " - " + e.getMessage(), e);

                runOnUiThread(() -> txtOutput.setText(
                        "Timeout/IOException esperado:\n" + e.getClass().getSimpleName() + "\n" + e.getMessage()
                ));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
