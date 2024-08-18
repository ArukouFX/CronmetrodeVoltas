package com.example.cronmetrodevoltas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.os.Handler;
import android.os.SystemClock;

import android.os.SystemClock;

public class MyViewModel extends ViewModel {

    private final MutableLiveData<Integer> _seconds = new MutableLiveData<>(0);
    public LiveData<Integer> seconds = _seconds;

    private final MutableLiveData<Boolean> _running = new MutableLiveData<>(false);
    public LiveData<Boolean> running = _running;

    private boolean wasRunning;
    private long startTime; // Para guardar el tiempo de inicio
    private final Handler handler = new Handler();

    // Comenzar el cronómetro
    public void comenzarT() {
        if (!_running.getValue()) { // Solo iniciar si no estaba corriendo
            startTime = SystemClock.elapsedRealtime() - (_seconds.getValue() * 1000);
            _running.setValue(true);
            correrT();
        }
    }

    // Método que ejecuta el cronómetro
    public void correrT() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (_running.getValue() != null && _running.getValue()) {
                    long elapsedMillis = SystemClock.elapsedRealtime() - startTime; // Tiempo transcurrido
                    _seconds.setValue((int) (elapsedMillis / 1000)); // Convertir a segundos y actualizar LiveData
                    handler.postDelayed(this, 1000); // Ejecutar de nuevo después de 1 segundo
                }
            }
        });
    }

    // Pausar el cronómetro
    public void pausarT() {
        wasRunning = _running.getValue();
        _running.setValue(false);
    }

    // Resumir el cronómetro
    public void resumirT() {
        if (wasRunning) {
            startTime = SystemClock.elapsedRealtime() - (_seconds.getValue() * 1000); // Ajustar startTime para el tiempo ya transcurrido
            _running.setValue(true);
            correrT();
        }
    }

    // Detener el cronómetro
    public void detenerT() {
        _running.setValue(false);
    }

    // Resetear el cronómetro
    public void resetearT() {
        detenerT();
        _seconds.setValue(0);
        startTime = 0; //
    }

    // Restaurar el estado
    public void restoreState(int seconds, boolean running, boolean wasRunning) {
        _seconds.setValue(seconds);
        _running.setValue(running);
        this.wasRunning = wasRunning;
        if (running) {
            startTime = SystemClock.elapsedRealtime() - (seconds * 1000); // Ajustar startTime si se está corriendo
            correrT();
        }
    }

    public int getSeconds() {
        return _seconds.getValue();
    }

    public boolean isRunning() {
        return _running.getValue();
    }

    public boolean wasRunning() {
        return wasRunning;
    }
}
