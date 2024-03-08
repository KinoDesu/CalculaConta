package com.example.calculaconta;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CompraFinalizada extends AppCompatActivity {

    private TextView txtCountdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra_finalizada);
        txtCountdown = findViewById(R.id.txtFinal6);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        realizarContagem();
    }

    private void realizarContagem() {
        Thread t1 = new Thread(() -> {
            int count = 10;
            while (count >= 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                txtCountdown.setText(String.valueOf(count--));
            }
            CompraFinalizada.this.finish();
            System.exit(0);
        });

        t1.start();
    }
}