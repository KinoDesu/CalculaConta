package com.example.calculaconta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.calculaconta.models.Pedido;
import com.example.calculaconta.models.Pessoa;
import com.example.calculaconta.repositories.PedidoRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;


//TODO: permitir editar/remover um pedido
//TODO: gravar histórico de restaurantes
//TODO: remover fechamento automático

public class HomePage extends AppCompatActivity {

    private List<Pedido> pedidos;
    private final PedidoRepository pedidoRepository = PedidoRepository.getInstance();
    private LinearLayout listaPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        listaPedidos = findViewById(R.id.listaPedidos);


        if (listaPedidos.getChildCount() > 1) {
            while (listaPedidos.getChildCount() > 1)
                listaPedidos.removeViewAt(1);
        }

        popularVariaveisTemp();

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.dataFileName), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String listaPedidosDefaultJson = gson.toJson(pedidos);
        String resultadoShared = sharedPreferences.getString(getString(R.string.sharedPedidos), listaPedidosDefaultJson);

        pedidos = gson.fromJson(resultadoShared, new TypeToken<List<Pedido>>() {});
        pedidoRepository.setListaPedidos(pedidos);

        gerarListaPedidos();

    }

    private void gerarListaPedidos() {

        if (pedidos.isEmpty()) {
            TextView textListaVazia = new TextView(this);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            textParams.setMargins(20, 50, 20, 0);
            textListaVazia.setLayoutParams(textParams);

            File sds = getCacheDir();


            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
            int color = ContextCompat.getColor(this, typedValue.resourceId);

            textListaVazia.setGravity(Gravity.CENTER);
            textListaVazia.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textListaVazia.setTextColor(color);
            String texto = "Parece que a mesa tá parada ainda...";
            textListaVazia.setText(texto);
            textListaVazia.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
            textListaVazia.setTypeface(null, Typeface.BOLD);
            listaPedidos.addView(textListaVazia);
            return;
        }

        DecimalFormat df = new DecimalFormat("0.00");

        for (int i = 0; i < pedidos.size(); i++) {
            final int idPedido = i;

            //region layouts

            final LinearLayout layoutPedido = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 50);
            layoutPedido.setLayoutParams(params);
            layoutPedido.setOrientation(LinearLayout.VERTICAL);

            final LinearLayout layoutBoxPedido = new LinearLayout(this);
            params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 50);
            layoutBoxPedido.setLayoutParams(params);
            layoutBoxPedido.setOrientation(LinearLayout.HORIZONTAL);
            layoutBoxPedido.setPadding(60, 0, 60, 0);

            TypedValue typedValuedsd = new TypedValue();
            getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValuedsd, true);
            int colordds = ContextCompat.getColor(this, typedValuedsd.resourceId);


            //endregion

            //region Nome
            TextView textViewNomePedido = new TextView(this);
            textViewNomePedido.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
            int color = ContextCompat.getColor(this, typedValue.resourceId);

            textViewNomePedido.setTextColor(color);
            String texto = pedidos.get(i).getNome();
            textViewNomePedido.setText(texto);
            textViewNomePedido.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textViewNomePedido.setTypeface(null, Typeface.BOLD);

            textViewNomePedido.setClickable(true);
            textViewNomePedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandirDetalhes(listaPedidos, layoutPedido, idPedido);
                }
            });
            //endregion

            //region Preco
            TextView textViewPrecoPedido = new TextView(this);
            textViewPrecoPedido.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            textViewPrecoPedido.setTypeface(null, Typeface.BOLD);
            textViewPrecoPedido.setTextColor(color);
            texto = pedidos.get(i).getQuantidade() + "x R$ " + df.format(pedidos.get(i).getValorProduto()).replace(".", ",");
            textViewPrecoPedido.setText(texto);
            textViewPrecoPedido.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textViewPrecoPedido.setGravity(Gravity.CENTER);
            textViewPrecoPedido.setClickable(true);
            textViewPrecoPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandirDetalhes(listaPedidos, layoutPedido, idPedido);
                }
            });
            //endregion

            //region imagem

            ImageView  imagemMaisInfo = new ImageView(this);
            LinearLayout.LayoutParams paramImagemMaisInfo = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    0.2f
            );
            paramImagemMaisInfo.gravity = Gravity.CENTER;
            imagemMaisInfo.setLayoutParams(paramImagemMaisInfo);
            String uri = "@android:drawable/ic_menu_add";
            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            imagemMaisInfo.setImageResource(imageResource);

            //endregion

            layoutBoxPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandirDetalhes(listaPedidos, layoutPedido, idPedido);
                }
            });

            layoutBoxPedido.addView(textViewNomePedido);
            layoutBoxPedido.addView(textViewPrecoPedido);
            layoutBoxPedido.addView(imagemMaisInfo);
            layoutPedido.addView(layoutBoxPedido);

            listaPedidos.addView(layoutPedido);
        }
    }

    private void expandirDetalhes(LinearLayout listaPedidos, LinearLayout layoutPedido, int id) {

        // Verificar se os detalhes já foram adicionados
        if (layoutPedido.getChildCount() > 1) {
            while (layoutPedido.getChildCount() > 1)
                layoutPedido.removeViewAt(1);
            return;
        }

        DecimalFormat df = new DecimalFormat("0.00");

        // Criar dinamicamente um TextView para os detalhes do pedido
        List<Pessoa> listaPessoas = pedidos.get(id).getListaPessoas();
        for (int i = 0; i < listaPessoas.size(); i++) {

            //region layouts
            final LinearLayout layoutDetalhes = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            layoutDetalhes.setLayoutParams(params);
            layoutDetalhes.setOrientation(LinearLayout.VERTICAL);

            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(com.google.android.material.R.attr.colorSecondary, typedValue, true);
            int color = ContextCompat.getColor(this, typedValue.resourceId);

            layoutDetalhes.setBackgroundColor(color);

            final LinearLayout layoutBoxDetalhes = new LinearLayout(this);
            params = new LinearLayout.LayoutParams(
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

            typedValue = new TypedValue();
            getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryVariant, typedValue, true);
            color = ContextCompat.getColor(this, typedValue.resourceId);

            textViewNomePessoa.setTextColor(color);
            String texto = (pedidos.get(id).getListaPessoas().get(i).getNome());
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

            texto = "R$ " + df.format(pedidos.get(id).getValorPessoa()).replace(".", ",");
            textViewPrecoPessoa.setText(texto);
            textViewPrecoPessoa.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textViewPrecoPessoa.setGravity(Gravity.END);
            textViewPrecoPessoa.setClickable(true);
            //endregion

            layoutBoxDetalhes.addView(textViewNomePessoa);
            layoutBoxDetalhes.addView(textViewPrecoPessoa);
            layoutDetalhes.addView(layoutBoxDetalhes);

            layoutPedido.addView(layoutDetalhes);
        }
    }

    private void popularVariaveisTemp() {
        pedidos = pedidoRepository.getListaDePedidos();
    }

    public void MoveToAddPedido(View v) {
        Intent intent = new Intent(this, AdicionarPedido.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top);
        finish();
    }

    public void MoveToPagar(View v) {
        Intent intent = new Intent(this, ContaFinal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}