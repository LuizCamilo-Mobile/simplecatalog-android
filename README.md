**SimpleCatalog — Projeto Android com arquitetura MVVM + Room + Retrofit**

Este é um projeto Android simples e objetivo, criado para demonstrar competências essenciais no desenvolvimento **Android nativo com Java**, incluindo consumo de API, persistência local, arquitetura de camadas e testes automatizados.

---

### **Stack utilizada**

* **Linguagem:** Java (intermediário/avançado)
* **UI:** XML com **ConstraintLayout** + **RecyclerView** + **View Binding**
* **Networking:** Retrofit + OkHttp + Gson (Converter)
* **Persistência:** Room (cache local) sobre SQLite
* **Arquitetura:** MVVM com separação em camadas (UI → Domain → Data)
* **Ferramentas:** Git, Gradle, Android Studio
* **Testes:**

  * Unitários: JUnit + Mockito
  * Interface: Espresso (AndroidX Test)

---

### **Arquitetura do projeto**

O projeto segue um modelo **MVVM com princípios de Clean Architecture**, organizado em pacotes:

```
com.seunome.simplecatalog
│
├── ui/            → Activity, Adapter, ViewModel, ViewModelFactory
├── domain/        → Modelos de negócio e UseCases (sem dependências Android)
├── repository/    → Repositório (fonte única: API + cache local)
├── data/
│   ├── remote/    → Config Retrofit/OkHttp/Gson + DTOs da API
│   ├── local/     → Config Room Database + Entities + DAO
│   └── mapper/    → Conversores DTO ↔ Entity ↔ Domain
└── config/        → Arquivos de configuração compartilhados
```

**Responsabilidades principais:**

* A UI apenas observa estados expostos via **LiveData** do ViewModel.
* Regras de negócio são tratadas nos **UseCases** (`domain/`).
* O `ItemRepository` implementa cache local via Room e busca remota via Retrofit.
* As camadas `domain/` e `repository/` são totalmente testáveis de forma isolada.

---

### **Funcionalidades**

* Carrega uma lista de itens de uma API REST.
* Salva os itens localmente no **Room** como cache (offline-first básico).
* Exibe os itens em um **RecyclerView**.
* Permite `refresh()` manual via repositório (gancho para discutir em entrevista).
* Cobertura básica com testes automatizados (unit + UI).

---

### **Como rodar o app**

1. Clone o projeto
2. Abra no **Android Studio**
3. Sincronize o Gradle
4. Execute (`Run ▶️`) em um emulador ou dispositivo Android

---

### **Como rodar os testes**

**Testes unitários**

* Clique com botão direito em `domain/` ou na classe `GetItemsUseCaseTest`
* `Run Tests`

**Testes de interface (Espresso)**

* Selecione a build variant `debug`
* Clique com botão direito no pacote `androidTest`
* `Run Tests`

---

### **Pontos de destaque para entrevistas**

* Separação clara de **UI, Domain e Data**
* Consumo de API com **Retrofit + OkHttp + Gson**
* Persistência robusta e tipada com **Room**
* Testes unitários com dependências mockadas (**Mockito**)
* Testes de UI com **Espresso**
* Base para discutir:

  * Cache local
  * Migrações do Room
  * Interceptors do OkHttp
  * Estratégias offline-first
  * Versionamento de builds debug/release

---

### Licença

Este projeto foi criado para fins de estudo e entrevistas. Pode ser reutilizado e adaptado livremente.

---

**Desenvolvido por:** Luiz Camilo



