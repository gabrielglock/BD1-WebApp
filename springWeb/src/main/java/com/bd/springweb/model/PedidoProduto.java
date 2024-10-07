package com.bd.springweb.model;

public class PedidoProduto {

    private int idPedido;
    private int idProduto;
    private int quantidade;
    private double preco;

    // Construtores
    public PedidoProduto() {}

    public PedidoProduto(int idPedido, int idProduto, int quantidade, double preco) {
        this.idPedido = idPedido;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    // Getters e Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }


    public double calcularValorTotal() {
        return quantidade * preco;
    }
}

