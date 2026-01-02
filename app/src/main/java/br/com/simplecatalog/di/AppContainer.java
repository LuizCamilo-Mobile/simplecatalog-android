package br.com.simplecatalog.di;

import android.content.Context;
import androidx.room.Room;

import br.com.simplecatalog.data.local.AppDatabase;
import br.com.simplecatalog.data.local.dao.ItemDao;
import br.com.simplecatalog.data.mapper.ItemMapper;
import br.com.simplecatalog.data.remote.RetrofitClient;
import br.com.simplecatalog.domain.usecase.GetItemsUseCase;
import br.com.simplecatalog.repository.ItemRepository;
import br.com.simplecatalog.repository.ItemRepositoryImpl;

public class AppContainer {
    /* O que é AppContainer na prática?
    * Pense nele como uma caixa de ferramentas do app: você pede a caixa e ela te entrega tudo montado.
    * Em vez de a MainActivity ficar criando Retrofit, Room, Repository, etc. (bagunça), ela só pede:
    * “me dá um ViewModelFactory / UseCase / Repository pronto”.
    * Isso é um jeito “manual” de fazer DI (injeção de dependência), sem Hilt.
    * Ele cria e guarda uma única instância de:
    * banco (Room)
    * rede (Retrofit)
    * repositório (API + cache)
    * caso de uso (GetItemsUseCase)
    * …pra você não ficar criando isso em Activity.
    * */

    private static AppContainer instance;
    /* Isso guarda uma única instância do AppContainer para o app inteiro .
    * */

    // Singletons do app
    public final ItemDao itemDao;
    public final ItemRepository itemRepository;
    public final GetItemsUseCase getItemsUseCase;
    public final ItemMapper itemMapper;
    public final RetrofitClient retrofitClient;
    /* O que são essas variáveis:
    * final = depois que o container cria, não muda mais.
    * São “serviços” do app: banco, rede, repo, use case.
    * Isso permite que qualquer parte do app use esses mesmos objetos.
    * */

    private AppContainer(Context context) {
    /* Por que private?
    * Pra obrigar o app a criar o container apenas via getInstance(), garantindo o “singleton”.
    * */

        // criando o Room Database (banco local)
        AppDatabase db = Room.databaseBuilder(
                context.getApplicationContext(),
                AppDatabase.class,
                "simplecatalog.db"
        ).fallbackToDestructiveMigration().build();
        /* O que cada argumento significa:
        * context.getApplicationContext()
        * → usa o contexto do app, não da Activity (mais seguro pra singleton)
        * AppDatabase.class
        * → sua classe @Database(...) (Room precisa dela)
        * "simplecatalog.db"
        * → nome do arquivo do banco no dispositivo
        * */

        this.itemDao = db.itemDao();
        /* O que é DAO?
        * DAO é a interface que faz as consultas no banco.
        * Você não faz SQL no app  — você centraliza no DAO.
        * itemDao() é um método que existirá em AppDatabase e devolve o ItemDao.
        * Resumo: Aqui você criou o banco e pegou o “acesso às tabelas” através do DAO.
        * */

        // criando o cliente de rede (Retrofit)
        this.retrofitClient = new RetrofitClient();
        /*
        * O que é RetrofitClient aqui?
        * Uma classe que vai construir:
        * OkHttp (interceptors, logs, timeouts)
        * Retrofit (baseUrl + converter Gson)
        * ApiService (os endpoints)
        * Ou seja, essa linha cria tudo que é “mundo HTTP”.
        * No seu esqueleto eu referenciei retrofitClient.apiService
        * depois — então dentro de RetrofitClient você terá algo
        * como public final ApiService apiService;
        * */

        // criando o Mapper
        this.itemMapper = new ItemMapper();
        /* Por que Mapper existe?
        * Pra impedir que:
        * ItemDto (formato da API) vá direto pra UI
        * ItemEntity (formato do banco) vá direto pra UI
        * O Mapper faz isso:
        * Dto -> Entity (pra salvar no banco)
        * Entity -> Domain (pra mostrar na UI)
        * Isso deixa seu projeto “limpo” e bem explicável em entrevista.
        * */

        // criando o Repository REAL (API + Room)
        this.itemRepository = new ItemRepositoryImpl(
                retrofitClient.apiService,
                itemDao,
                itemMapper
        );
        /* Essa é a parte mais importante.
        * O Repository é o “cérebro do dado”:
        * Ele decide quando:
        * buscar da API
        * salvar no Room
        * ler do Room
        * devolver pro app como List<Item> (domínio)
        * Por isso ele precisa:
        * retrofitClient.apiService → pra buscar remoto
        * itemDao → pra cache local
        * itemMapper → pra converter formatos
        * Em entrevista, isso é “Single source of truth” na prática.
        * */

        // criando o UseCase
        this.getItemsUseCase = new GetItemsUseCase(itemRepository);
        /* O UseCase encapsula a “ação”:
        * “carregar itens”
        * Ele recebe o repository porque:
        * UseCase não sabe se o dado vem da internet ou do banco
        * UseCase só pede: “me dá itens”
        * Quem resolve isso é o repository
        * Isso é o que te dá testabilidade: você testa o UseCase mockando o repository.
        * */
    }

    public static AppContainer getInstance(Context context) {
        if (instance == null) {
            instance = new AppContainer(context);
        }
        return instance;
    }
}

