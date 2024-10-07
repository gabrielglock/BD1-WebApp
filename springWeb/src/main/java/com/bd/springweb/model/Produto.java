package com.bd.springweb.model;

public class Produto {

    private Integer id;
    private String nome;
    private double preco;
    private Integer qtd_disponivel;
    private String descricao;
    private String cnpj; //fk loja

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Integer getQtd_disponivel() {
        return qtd_disponivel;
    }

    public void setQtd_disponivel(Integer qtd_disponivel) {
        this.qtd_disponivel = qtd_disponivel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
