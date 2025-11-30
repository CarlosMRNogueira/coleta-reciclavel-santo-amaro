package com.example.coletareciclavelsantoamaro;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.ComponentActivity;

public class PermissaoActivity extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissao);

        Button btnPermissao = findViewById(R.id.btnPermissao);

        btnPermissao.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 31) {
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

                if (!am.canScheduleExactAlarms()) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(intent);
                    return;
                }
            }

            startActivity(new Intent(this, CadastrarNotificacaoActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT < 31) {
            startActivity(new Intent(this, CadastrarNotificacaoActivity.class));
            finish();
            return;
        }

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (am.canScheduleExactAlarms()) {
            startActivity(new Intent(this, CadastrarNotificacaoActivity.class));
            finish();
        }
    }
}
