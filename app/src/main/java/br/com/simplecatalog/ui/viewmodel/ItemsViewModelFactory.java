package br.com.simplecatalog.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import br.com.simplecatalog.domain.usecase.GetItemsUseCase;

/**
 * Factory responsável por criar o ItemsViewModel com o UseCase injetado no construtor.
 *
 * Por que é necessário:
 * - ViewModel padrão do Android exige construtor vazio, mas o nosso tem dependências
 * - A Factory resolve isso criando a instância manualmente e entregando pronta
 *
 * Benefícios:
 * - Mantém o ViewModel desacoplado de Context e Views
 * - Permite fornecer dependências mock/fake em testes
 *
 * Uso Factory para injetar dependências no ViewModel via construtor,
 * mantendo o domínio desacoplado e permitindo testes com versões fake ou mocks.
 * Isso mostra:
 * entendimento de DI (injeção por construtor)
 * organização do estado da tela
 * respeito às boundaries (UI não cria UseCase solto)
 */
public class ItemsViewModelFactory implements ViewModelProvider.Factory {

    // Guardamos o UseCase que será injetado no ViewModel.
    private final GetItemsUseCase getItemsUseCase;

    // A Activity vai criar a Factory passando o UseCase do AppContainer.
    public ItemsViewModelFactory(GetItemsUseCase getItemsUseCase) {
        this.getItemsUseCase = getItemsUseCase;
    }

    @Override
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        // Verifica se a Factory foi chamada para o ViewModel correto
        if (modelClass.isAssignableFrom(ItemsViewModel.class)) {
            // Cria o ViewModel com a dependência do UseCase
            return (T) new ItemsViewModel(getItemsUseCase); // ← referência direta ao que você queria entender
        }
        throw new IllegalArgumentException("ViewModel desconhecido");
    }
}

