package br.com.simplecatalog.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.simplecatalog.data.local.entity.ItemEntity;
import br.com.simplecatalog.data.mapper.ItemMapper;
import br.com.simplecatalog.data.remote.dto.ItemDto;
import br.com.simplecatalog.databinding.ActivityMainBinding;
import br.com.simplecatalog.domain.model.Item;
import br.com.simplecatalog.ui.adapter.ItemsAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ItemsAdapter adapter;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ItemMapper mapper = new ItemMapper();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new ItemsAdapter();
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter);

        showIdle();

        binding.btnProcess.setOnClickListener(v -> processDataPipeline());
    }

    private void processDataPipeline() {
        showProcessing();

        executor.execute(() -> {
            // 1) “API response” simulada (DTO)
            List<ItemDto> dtos = createHardcodedDtos();

            // 2) DTO → Entity (simula “formato de banco”)
            List<ItemEntity> entities = mapper.dtosToEntities(dtos);

            // 3) Entity → Domain (o que a UI pode consumir)
            List<Item> domainItems = mapper.entitiesToDomain(entities);

            // 4) UI thread: render
            runOnUiThread(() -> {
                adapter.submit(domainItems);
                showDone();
            });
        });
    }

    private List<ItemDto> createHardcodedDtos() {
        List<ItemDto> list = new ArrayList<>();
        list.add(new ItemDto(1, "Café", "500g • torrado"));
        list.add(new ItemDto(2, "Leite", "Integral • 1L"));
        list.add(new ItemDto(3, "Arroz", "Tipo 1 • 5kg"));
        list.add(new ItemDto(4, "Feijão", "Carioca • 1kg"));
        list.add(new ItemDto(5, "Açúcar", "Refinado • 1kg"));
        list.add(new ItemDto(6, "Macarrão", "Espaguete • 500g"));
        return list;
    }

    private void showIdle() {
        binding.progress.setVisibility(android.view.View.GONE);
        binding.tvStatus.setText("Aguardando");
        binding.btnProcess.setEnabled(true);
    }

    private void showProcessing() {
        binding.progress.setVisibility(android.view.View.VISIBLE);
        binding.tvStatus.setText("Processando…");
        binding.btnProcess.setEnabled(false);
        adapter.submit(new ArrayList<>()); // limpa lista pra ver o efeito
    }

    private void showDone() {
        binding.progress.setVisibility(android.view.View.GONE);
        binding.tvStatus.setText("Concluído");
        binding.btnProcess.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
