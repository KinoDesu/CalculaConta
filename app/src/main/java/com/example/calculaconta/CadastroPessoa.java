package com.example.calculaconta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.calculaconta.models.Pessoa;
import com.example.calculaconta.repositories.PessoaRepository;
import com.google.gson.Gson;

import java.util.List;

public class CadastroPessoa extends AppCompatActivity {
    private final PessoaRepository pessoaRepository = PessoaRepository.getInstance();
    private List<Pessoa> listaPessoas;
    private EditText editTextNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pessoa);
        editTextNome = findViewById(R.id.editTextPersonName);

        popularDados();

    }

    private void popularDados(){
        listaPessoas = pessoaRepository.getListaDePessoas();
    }

    public void salvarPessoa(View view) {

        if (editTextNome.getText().toString().trim().length() != 0) {
            String nome = editTextNome.getText().toString();
            Pessoa pessoa = new Pessoa(nome);
            pessoaRepository.adicionarPessoa(pessoa);

            Toast.makeText(CadastroPessoa.this, nome + " foi salvo(a)!", Toast.LENGTH_SHORT).show();

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Intent intent = new Intent(this, CadastroPessoa.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top);
            finish();
        } else {
            Toast.makeText(CadastroPessoa.this, "Preencha todas as informações!", Toast.LENGTH_LONG).show();
        }
    }

    public void finalizarCadastros(View view) {

        if (listaPessoas.size() >= 2 || (listaPessoas.size() >= 1 && editTextNome.getText().toString().trim().length() != 0)) {

            if (editTextNome.getText().toString().trim().length() != 0) {
                String nome = editTextNome.getText().toString();

                Pessoa pessoa = new Pessoa(nome);
                pessoaRepository.adicionarPessoa(pessoa);

                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.dataFileName), Context.MODE_PRIVATE);
                SharedPreferences.Editor editorSP = sharedPreferences.edit();
                Gson gson = new Gson();
                String listaPessoasDefaultJson = gson.toJson(pessoaRepository.getListaDePessoas());

                editorSP.putString(getString(R.string.sharedPessoas), listaPessoasDefaultJson);
                editorSP.apply();


                Toast.makeText(CadastroPessoa.this, nome + " foi salvo(a)!", Toast.LENGTH_SHORT).show();

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Intent intent = new Intent(this, HomePage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top);
                finish();
            } else {
                Intent intent = new Intent(this, HomePage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top);
                finish();
            }
        } else {
            Toast.makeText(CadastroPessoa.this, "Deve ter no mínimo 2 participantes!", Toast.LENGTH_SHORT).show();
        }

    }
}