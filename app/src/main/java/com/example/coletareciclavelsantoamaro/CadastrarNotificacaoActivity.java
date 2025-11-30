package com.example.coletareciclavelsantoamaro;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.ComponentActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class CadastrarNotificacaoActivity extends ComponentActivity {

    private Spinner spBairro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        if (Build.VERSION.SDK_INT >= 31) {
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (!am.canScheduleExactAlarms()) {
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);

                Toast.makeText(this,
                        "Ative 'Alarmes Precisos' e volte para agendar.",
                        Toast.LENGTH_LONG).show();
            }
        }

        spBairro = findViewById(R.id.spBairro);
        CheckBox cb07 = findViewById(R.id.cb07);
        CheckBox cb19 = findViewById(R.id.cb19);
        Button btnAgendar  = findViewById(R.id.btnAgendar);
        Button btnVoltar = findViewById(R.id.btnVoltar);

        // 🔥 CARREGAMENTO SEGURO DO SPINNER
        carregarSpinner();

        btnAgendar.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 31) {
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

                if (!am.canScheduleExactAlarms()) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);

                    Toast.makeText(this,
                            "Ative 'Alarmes Precisos' e volte para tentar novamente.",
                            Toast.LENGTH_LONG).show();
                }
            }

            String bairro = (String) spBairro.getSelectedItem();
            int[] dias = DataProvider.getRecicladoWeekdays(bairro);

            if (!cb07.isChecked() && !cb19.isChecked()) {
                Toast.makeText(this, "Escolha 07:00 e/ou 19:00.", Toast.LENGTH_SHORT).show();
                return;
            }

            NotificationUtils.createChannel(this);

            if (cb07.isChecked())
                NotificationUtils.scheduleWeeklyReminder(this, dias, 7, 0, NotificationUtils.REQ_MORNING);

            if (cb19.isChecked())
                NotificationUtils.scheduleWeeklyReminder(this, dias, 19, 0, NotificationUtils.REQ_EVENING);


            // TESTE DE 1 MINUTO
            new android.os.Handler().postDelayed(() -> {
                Notification n = new NotificationCompat.Builder(
                        this, NotificationUtils.CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_popup_reminder)
                        .setContentTitle("🔔 TESTE: Notificação de Reciclado")
                        .setContentText("Esta é uma simulação para mostrar no vídeo.")
                        .setAutoCancel(true)
                        .build();

                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                        .notify(9999, n);
            }, 60 * 1000);


            Toast.makeText(this,
                    "Lembrete(s) agendado(s) para 1 dia antes.",
                    Toast.LENGTH_SHORT).show();
        });

        btnVoltar.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // GARANTE QUE O SPINNER VOLTE CARREGADO APÓS PERMISSÃO
        carregarSpinner();
    }

    // MÉTODO QUE SEMPRE CARREGA O SPINNER COM SEGURANÇA
    private void carregarSpinner() {
        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    DataProvider.getBairros()
            );
            spBairro.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
