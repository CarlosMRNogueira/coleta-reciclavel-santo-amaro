package com.example.coletareciclavelsantoamaro;

import android.content.pm.PackageManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.ComponentActivity;

public class MainActivity extends ComponentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        101
                );
            }
        }
        if (Build.VERSION.SDK_INT >= 33 && !NotificationUtils.hasPostNotifPermission(this)) {
            NotificationUtils.requestPostNotifPermission(this);
        }
        NotificationUtils.createChannel(this);

        Button btnConsulta = findViewById(R.id.btnConsulta);
        Button btnAgendar  = findViewById(R.id.btnAgendar);
        Button btnCancelar = findViewById(R.id.btnCancelar);

        btnConsulta.setOnClickListener(v ->
                startActivity(new Intent(this, ConsultaActivity.class)));

        btnAgendar.setOnClickListener(v ->
                startActivity(new Intent(this, CadastrarNotificacaoActivity.class)));

        btnCancelar.setOnClickListener(v -> {
            NotificationUtils.cancelAll(this);
            Toast.makeText(this, "Lembretes cancelados.", Toast.LENGTH_SHORT).show();
        });
    }
}
