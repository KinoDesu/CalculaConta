package com.example.calculaconta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.calculaconta.models.Pedido;
import com.example.calculaconta.models.Pessoa;
import com.example.calculaconta.repositories.PedidoRepository;
import com.example.calculaconta.repositories.PessoaRepository;

import java.text.DecimalFormat;
import java.util.List;

public class ContaFinal extends AppCompatActivity {
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final PessoaRepository pessoaRepository = PessoaRepository.getInstance();
    private final PedidoRepository pedidoRepository = PedidoRepository.getInstance();
    private final List<Pessoa> listaPessoas = pessoaRepository.getListaDePessoas();
    private final List<Pedido> listaPedidos = pedidoRepository.getListaDePedidos();

    private double valorServico = 0;
    private TextView txtSubtotalValor;
    private EditText editServico;
    private TextView txtTotalValor;
    private LinearLayout linearListaPessoasConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta_final);

        txtSubtotalValor = findViewById(R.id.txtSubtotalValor);
        editServico = findViewById(R.id.editServico);
        txtTotalValor = findViewById(R.id.txtValorTotal);
        linearListaPessoasConta = findViewById(R.id.linearListaPessoasConta);

        mostrarDadosTela();
        gerarListaPessoas();

        editServico.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                String txtServico = editServico.getText().toString();
                if (txtServico.trim().equalsIgnoreCase(""))
                    txtServico = "0";

                txtServico = txtServico.replace(",", ".");
                valorServico = Double.parseDouble(txtServico);

                String valorSubtotalText = txtSubtotalValor.getText().toString();
                valorSubtotalText = valorSubtotalText.replace(",", ".");
                valorSubtotalText = valorSubtotalText.replace("R$ ", "");

                double valorSubtotal = Double.parseDouble(valorSubtotalText);

                double valorTotal = ((valorSubtotal * valorServico) / 100) + valorSubtotal;
                txtTotalValor.setText("R$ " + df.format(valorTotal).replace(".", ","));

                recalcularPessoas();
            }
        });
    }

    private void recalcularPessoas() {
        while (linearListaPessoasConta.getChildCount() > 0)
            linearListaPessoasConta.removeViewAt(0);

        gerarListaPessoas();
    }

    private void mostrarDadosTela() {
        double valorSubtotal = 0.00;

        for (Pedido pedido : listaPedidos) valorSubtotal += pedido.getValorTotal();

        String valorSubtotalText = "R$ " + df.format(valorSubtotal).replace(".", ",");
        txtSubtotalValor.setText(valorSubtotalText);

        double valorTotal = ((valorSubtotal * valorServico) / 100) + valorSubtotal;
        txtTotalValor.setText("R$ " + df.format(valorTotal).replace(".", ","));
    }

    private void gerarListaPessoas() {

        DecimalFormat df = new DecimalFormat("0.00");

        // Criar dinamicamente um TextView para os detalhes do pedido
        for (int i = 0; i < listaPessoas.size(); i++) {

            final LinearLayout layoutBoxDetalhes = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 25, 0, 50);
            layoutBoxDetalhes.setLayoutParams(params);
            layoutBoxDetalhes.setOrientation(LinearLayout.HORIZONTAL);
            layoutBoxDetalhes.setPadding(60, 0, 60, 0);
            //endregion

            //region Nome
            TextView textViewNomePessoa = new TextView(this);
            textViewNomePessoa.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
            int color = ContextCompat.getColor(this, typedValue.resourceId);

            textViewNomePessoa.setTextColor(color);
            String texto = (listaPessoas.get(i).getNome());
            textViewNomePessoa.setText(texto);
            textViewNomePessoa.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textViewNomePessoa.setTypeface(null, Typeface.BOLD);
            //endregion

            //region Preco
            TextView textViewPrecoPessoa = new TextView(this);
            textViewPrecoPessoa.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            textViewPrecoPessoa.setTypeface(null, Typeface.BOLD);
            textViewPrecoPessoa.setTextColor(color);

            double aPagar = listaPessoas.get(i).getValorPagar();

            aPagar = ((aPagar * valorServico) / 100) + aPagar;

            texto = "R$ " + df.format(aPagar).replace(".", ",");
            textViewPrecoPessoa.setText(texto);
            textViewPrecoPessoa.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textViewPrecoPessoa.setGravity(Gravity.END);
            textViewPrecoPessoa.setClickable(true);
            //endregion

            layoutBoxDetalhes.addView(textViewNomePessoa);
            layoutBoxDetalhes.addView(textViewPrecoPessoa);

            linearListaPessoasConta.addView(layoutBoxDetalhes);
        }

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