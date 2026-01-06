package br.com.simplecatalog.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import br.com.simplecatalog.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EX5_1_OKHTTP";
    private static final String URL = "https://jsonplaceholder.typicode.com/posts/1";

    private TextView txtOutput;
    private ExecutorService executor;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtOutput = findViewById(R.id.txtOutput);
        Button btnGet = findViewById(R.id.btnGet);

        // 1) Executor: garante que a rede roda fora da UI thread
        executor = Executors.newSingleThreadExecutor();

        // 2) Logging interceptor (observabilidade)
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 3) OkHttpClient com timeouts coerentes + interceptor
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        btnGet.setOnClickListener(v -> doGet());
    }

    private void doGet() {
        txtOutput.setText("Carregando...");

        executor.execute(() -> {
            // 4) Monta Request
            Request request = new Request.Builder()
                    .url(URL)
                    .get()
                    .build();

            // 5) Executa sincronamente (bloqueante) - por isso está no background
            try (Response response = client.newCall(request).execute()) {

                int code = response.code();
                ResponseBody body = response.body();
                String bodyString = (body != null) ? body.string() : "";

                Log.d(TAG, "HTTP code = " + code);
                Log.d(TAG, "Body length = " + bodyString.length());

                // só pra confirmar visualmente que veio JSON (sem poluir)
                String preview = bodyString.length() > 120
                        ? bodyString.substring(0, 120) + "..."
                        : bodyString;

                Log.d(TAG, "Body preview = " + preview);

                runOnUiThread(() -> txtOutput.setText(
                        "HTTP " + code + "\n" +
                                "Body length: " + bodyString.length() + "\n\n" +
                                preview
                ));

            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage(), e);
                runOnUiThread(() -> txtOutput.setText("IOException: " + e.getMessage()));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
