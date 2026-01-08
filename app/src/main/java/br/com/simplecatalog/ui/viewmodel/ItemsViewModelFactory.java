package br.com.simplecatalog.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.simplecatalog.domain.usecase.GetItemsUseCase;

public class ItemsViewModelFactory implements ViewModelProvider.Factory {

    private final GetItemsUseCase useCase;

    public ItemsViewModelFactory(GetItemsUseCase useCase) {
        this.useCase = useCase;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ItemsViewModel.class)) {
            return (T) new ItemsViewModel(useCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
