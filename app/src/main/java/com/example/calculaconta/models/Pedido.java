package com.example.calculaconta.models;

import java.util.List;

public class Pedido {
    private int id;
    private String nome;
    private int quantidade;
    private double valorProduto;
    private double valorTotal;
    private List<Pessoa> listaPessoas;
    private double valorPessoa;

    public Pedido(int id, String nome, int quantidade, double valorProduto, List<Pessoa> listaPessoas) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.valorProduto = valorProduto;
        this.valorTotal = valorProduto*quantidade;
        this.listaPessoas = listaPessoas;
        this.valorPessoa = valorTotal/listaPessoas.size();
    }

    public Pedido(String nome, int quantidade, double valorProduto, List<Pessoa> listaPessoas) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.valorProduto = valorProduto;
        this.valorTotal = valorProduto*quantidade;
        this.listaPessoas = listaPessoas;
        this.valorPessoa = valorTotal/listaPessoas.size();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<Pessoa> getListaPessoas() {
        return listaPessoas;
    }

    public void setListaPessoas(List<Pessoa> listaPessoas) {
        this.listaPessoas = listaPessoas;
    }

    public void addPessoa(Pessoa pessoa){
        listaPessoas.add(pessoa);
    }

    public double getValorPessoa() {
        return valorPessoa;
    }

    public void setValorPessoa(double valorPessoa) {
        this.valorPessoa = valorPessoa;
    }

    public double getValorProduto() {
        return valorProduto;
    }

    public void setValorProduto(double valorProduto) {
        this.valorProduto = valorProduto;
    }
}
