package com.example.calculaconta.repositories;

import com.example.calculaconta.models.Pessoa;

import java.util.ArrayList;
import java.util.List;

public class PessoaRepository {

    private static PessoaRepository instance;
    private int id = 1;
    private List<Pessoa> listaPessoas;

    private PessoaRepository() {
        listaPessoas = new ArrayList<>();
    }

    public static synchronized PessoaRepository getInstance() {
        if (instance == null) {
            instance = new PessoaRepository();
        }
        return instance;
    }

    public List<Pessoa> getListaDePessoas() {
        return listaPessoas;
    }

    public void adicionarPessoa(Pessoa pessoa) {
        pessoa.setId(id++);
        listaPessoas.add(pessoa);
    }

    public void clearList(){
        listaPessoas.clear();
        id = 1;
    }

    public Pessoa getPessoaById(int idPessoa){
        Pessoa pessoa = listaPessoas.stream().filter(p->p.getId()==idPessoa).findFirst().orElse(null);
        return pessoa;
    }
    public Pessoa getPessoaByNome(String nome){
        Pessoa pessoa = listaPessoas.stream().filter(p->p.getNome().equalsIgnoreCase(nome)).findFirst().orElse(null);
        return pessoa;
    }
}
