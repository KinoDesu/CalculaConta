package com.example.calculaconta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.calculaconta.models.Pedido;
import com.example.calculaconta.models.Pessoa;
import com.example.calculaconta.repositories.PedidoRepository;
import com.example.calculaconta.repositories.PessoaRepository;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AdicionarPedido extends AppCompatActivity {

    private final PessoaRepository pessoaRepository = PessoaRepository.getInstance();
    private final PedidoRepository pedidoRepository = PedidoRepository.getInstance();
    private List<Pessoa> listaPessoas = new ArrayList<>();
    private LinearLayout linearListaPessoas;
    private EditText editNome;
    private EditText editValor;
    private EditText editQuantidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_pedido);

        linearListaPessoas = findViewById(R.id.LinearAddPedidoPessoas);
        editNome = findViewById(R.id.editNomePedido);
        editValor = findViewById(R.id.editValorPedido);
        editQuantidade = findViewById(R.id.editQuantidade);

        listaPessoas = pessoaRepository.getListaDePessoas();

        for (int i = 0; i < listaPessoas.size(); i++) {
            CheckBox checkbox = new CheckBox(new ContextThemeWrapper(this, R.style.MyCheckBox));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            checkbox.setText(listaPessoas.get(i).getNome());
            linearListaPessoas.addView(checkbox, params);
        }

        editValor.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String text = editValor.getText().toString();

                // Remover qualquer vírgula existente
                text = text.replace(",", "");
                text = text.replace("R$ ", "");

                if (text.length() > 0 && text.charAt(0) == '0') {
                    text = text.substring(1);
                }

                // Adicionar vírgula antes dos dois últimos dígitos
                int length = text.length();
                if (length > 2) {
                    text = "R$ " + text.substring(0, length - 2) + "," + text.substring(length - 2);
                } else {
                    // Caso tenha menos de dois dígitos, adicione a vírgula no início
                    text = "R$ " + "," + text;
                }
                // Atualizar o texto do EditText
                editValor.removeTextChangedListener(this);
                editValor.setText(text);


                // Mover o cursor para o final
                int selectionIndex = text.length();
                editValor.setSelection(selectionIndex);

                editValor.addTextChangedListener(this);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    public void adicionarPedido(View v) {
        List<Pessoa> pessoasSelecionadas = new ArrayList<>();

        for (int i = 0; i < linearListaPessoas.getChildCount(); i++) {
            View view = linearListaPessoas.getChildAt(i);

            if (view instanceof CheckBox) {
                CheckBox cb = (CheckBox) view;

                if (cb.isChecked()) {
                    String nome = cb.getText().toString();
                    pessoasSelecionadas.add(pessoaRepository.getPessoaByNome(nome));
                }
            }
        }

        String nome = editNome.getText().toString();

        String valorTexto = editValor.getText().toString();
        valorTexto = valorTexto.replace("R$ ", "");
        valorTexto = valorTexto.replace(",", ".");

        double valor = Double.parseDouble(valorTexto);
        int quantidade = Integer.parseInt(editQuantidade.getText().toString());

        Pedido pedido = pedidoRepository.adicionarPedido(new Pedido(nome, quantidade, valor, pessoasSelecionadas));
        for (Pessoa pessoa:pessoasSelecionadas) {
            Pessoa pessoaOriginal = pessoaRepository.getPessoaById(pessoa.getId());
            pessoaOriginal.setValorPagar(pessoa.getValorPagar()+pedido.getValorPessoa());
        }

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.dataFileName), Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSP = sharedPreferences.edit();
        Gson gson = new Gson();
        String listaPedidosDefaultJson = gson.toJson(pedidoRepository.getListaDePedidos());
        String listaPessoasDefaultJson = gson.toJson(pessoaRepository.getListaDePessoas());

        editorSP.putString(getString(R.string.sharedPedidos), listaPedidosDefaultJson);
        editorSP.putString(getString(R.string.sharedPessoas), listaPessoasDefaultJson);
        editorSP.apply();

        Toast.makeText(this, nome + " foi salvo(a)!", Toast.LENGTH_SHORT).show();

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top);
        finish();
    }
}