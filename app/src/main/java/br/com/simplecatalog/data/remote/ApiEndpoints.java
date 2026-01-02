package br.com.simplecatalog.data.remote;

/**
 * Centraliza a base URL e os paths dos endpoints da API.
 * Objetivo: evitar strings mágicas espalhadas e facilitar manutenção e testes.
 */
public final class ApiEndpoints {
    private ApiEndpoints() {} // impede instanciação

    // Base URL da API usada no RetrofitClient (JSONPlaceholder para estudo)
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    // Paths dos endpoints
    public static final String ITEMS = "posts";
}
