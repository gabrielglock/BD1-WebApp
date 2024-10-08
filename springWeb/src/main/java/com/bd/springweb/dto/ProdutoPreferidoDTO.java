package com.bd.springweb.dto;

public class ProdutoPreferidoDTO {
    private String nomeProduto;
    private int totalComprado;

    public ProdutoPreferidoDTO(String nomeProduto, int totalComprado) {
        this.nomeProduto = nomeProduto;
        this.totalComprado = totalComprado;
    }


    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getTotalComprado() {
        return totalComprado;
    }

    public void setTotalComprado(int totalComprado) {
        this.totalComprado = totalComprado;
    }
}
