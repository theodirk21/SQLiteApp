package com.uva.dev.mobile.sqliteapp.model;

public class BookList {

    private String nomeLivro;
    private Double preco;
    private Boolean disponivel;

    public BookList(String nomeLivro, Double preco, Boolean disponivel) {
        this.nomeLivro = nomeLivro;
        this.preco = preco;
        this.disponivel = disponivel;
    }


    public String getNomeLivro() {
        return nomeLivro;
    }

    public Boolean isDisponivel() {
        return disponivel;
    }

    public Double getPreco() {
        return preco;
    }
}
