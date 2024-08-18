package com.example.cronmetrodevoltas;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StopwatchActivity extends AppCompatActivity {

    private MyViewModel viewModel;

    private List<String> lapsList = new ArrayList<>();
    private int lapCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        // Obtener una instancia del ViewModel
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);

        // Referencia al TextView que muestra el tiempo
        final TextView timeView = findViewById(R.id.time_view);

        // Observar el número de segundos
        viewModel.seconds.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer seconds) {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
            }
        });

        // Restaurar el estado del cronómetro si la actividad se ha recreado
        if (savedInstanceState != null) {
            viewModel.restoreState(
                    savedInstanceState.getInt("seconds"),
                    savedInstanceState.getBoolean("running"),
                    savedInstanceState.getBoolean("wasRunning")
            );
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", viewModel.getSeconds());
        outState.putBoolean("running", viewModel.isRunning());
        outState.putBoolean("wasRunning", viewModel.wasRunning());
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.pausarT();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.resumirT();
    }

    // Manejar botones
    public void onClickStart(View view) {
        viewModel.comenzarT();
    }

    public void onClickStop(View view) {
        viewModel.detenerT();
    }

    public void onClickReset(View view) {
        viewModel.resetearT();
        viewModel.comenzarT();
    }

    // Método para registrar una nueva vuelta
    public void onClickLap(View view) {
        int seconds = viewModel.getSeconds();
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        String lapTime = String.format(Locale.getDefault(), "Lap %d: %d:%02d:%02d", ++lapCount, hours, minutes, secs);
        lapsList.add(lapTime);
        updateLapsTextView();
    }

    // Método para actualizar el TextView de vueltas
    private void updateLapsTextView() {
        TextView lapsTextView = findViewById(R.id.vueltasContadorTextView);
        StringBuilder lapsText = new StringBuilder();
        for (String lap : lapsList) {
            lapsText.append(lap).append("\n");
        }
        lapsTextView.setText(lapsText.toString());
    }
}
