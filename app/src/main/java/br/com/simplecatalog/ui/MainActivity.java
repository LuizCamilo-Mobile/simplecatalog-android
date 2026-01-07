package br.com.simplecatalog.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.simplecatalog.R;
import br.com.simplecatalog.data.remote.ApiService;
import br.com.simplecatalog.data.remote.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EX5_2_RETROFIT";
    private static final String SLOW_URL = "https://httpstat.us/200?sleep=5000";

    private TextView txtOutput;
    private ExecutorService executor;

    private ApiService api;
    private ApiService apiTimeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtOutput = findViewById(R.id.txtOutput);
        Button btnRequest = findViewById(R.id.btnRequest);
        Button btnTimeout = findViewById(R.id.btnTimeout);

        executor = Executors.newSingleThreadExecutor();

        api = RetrofitClient.createApiService(true);
        apiTimeout = RetrofitClient.createTimeoutApiService(true);

        btnRequest.setOnClickListener(v -> getRetrofitRaw());
        btnTimeout.setOnClickListener(v -> simulateTimeout());
    }

    private void getRetrofitRaw() {
        txtOutput.setText("Carregando (Retrofit Raw)...");

        executor.execute(() -> {
            Call<ResponseBody> call = api.getPostRaw();

            try {
                // execute() é bloqueante -> background
                Response<ResponseBody> response = call.execute();

                int code = response.code();
                String body = (response.body() != null) ? response.body().string() : "";

                Log.d(TAG, "HTTP code=" + code);
                Log.d(TAG, "Body length=" + body.length());

                String preview = body.length() > 160 ? body.substring(0, 160) + "..." : body;
                Log.d(TAG, "Body preview=" + preview);

                runOnUiThread(() -> txtOutput.setText(
                        "HTTP " + code + "\n" +
                                "Body length: " + body.length() + "\n\n" +
                                preview
                ));

            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage(), e);
                runOnUiThread(() -> txtOutput.setText("IOException: " + e.getMessage()));
            }
        });
    }

    private void simulateTimeout() {
        txtOutput.setText("Simulando timeout...");

        executor.execute(() -> {
            Call<ResponseBody> call = apiTimeout.slowRaw(SLOW_URL);

            try {
                Response<ResponseBody> response = call.execute();
                int code = response.code();
                Log.d(TAG, "slowRaw HTTP code=" + code);

                runOnUiThread(() -> txtOutput.setText("slowRaw retornou HTTP " + code +
                        "\n(se não deu timeout, aumente o sleep)"));

            } catch (IOException e) {
                // aqui você deve ver SocketTimeoutException / timeout
                Log.e(TAG, "Timeout/IOException esperado: " + e.getClass().getSimpleName()
                        + " - " + e.getMessage(), e);

                runOnUiThread(() -> txtOutput.setText(
                        "Timeout/IOException esperado:\n" +
                                e.getClass().getSimpleName() + "\n" +
                                e.getMessage()
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
