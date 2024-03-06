package com.example.calculaconta;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.example.calculaconta.models.Pessoa;
import com.example.calculaconta.repositories.PessoaRepository;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private float initialY;
    private final PessoaRepository pessoaRepository = PessoaRepository.getInstance();
    private List<Pessoa> listaPessoas = pessoaRepository.getListaDePessoas();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayout = findViewById(R.id.linear_layout);

        linearLayout.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialY = event.getY();
                    return true;

                case MotionEvent.ACTION_UP:
                    float finalY = event.getY();
                    if (initialY > finalY) {
                        // O usuário arrastou para cima
                        if (listaPessoas.isEmpty()) {
                            Intent intent = new Intent(MainActivity.this, CadastroPessoa.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top);
                        } else {
                            Intent intent = new Intent(MainActivity.this, HomePage.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top);
                        }

                    }
                    return true;
            }
            return false;
        });

    }
}