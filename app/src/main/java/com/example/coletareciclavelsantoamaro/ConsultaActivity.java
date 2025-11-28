package com.example.coletareciclavelsantoamaro;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.activity.ComponentActivity;
import java.util.Arrays;

public class ConsultaActivity extends ComponentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        Spinner spBairro = findViewById(R.id.spBairro);
        Button btnMostrar = findViewById(R.id.btnMostrar);
        ListView list = findViewById(R.id.listResultados);

        spBairro.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                DataProvider.getBairros()));

        btnMostrar.setOnClickListener(v -> {
            String bairro = (String) spBairro.getSelectedItem();
            String[] linhas = DataProvider.getRecicladoLinhas(bairro);
            list.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, Arrays.asList(linhas)));
        });

        Button btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> finish());

    }
}
