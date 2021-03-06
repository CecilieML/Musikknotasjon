package com.businesspanda.verynote;

/** Copyright (C) 2009 by Aleksey Surkov.
 **
 ** Permission to use, copy, modify, and distribute this software and its
 ** documentation for any purpose and without fee is hereby granted, provided
 ** that the above copyright notice appear in all copies and that both that
 ** copyright notice and this permission notice appear in supporting
 ** documentation.  This software is provided "as is" without express or
 ** implied warranty.
 */

import java.lang.Runnable;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import org.jtransforms.fft.DoubleFFT_1D;


public class PitchDec implements Runnable {

    /* Numbers that work:
        RATE: 8000      BUFFERSIZE: 4096        CHUNK_SIZE_IN_SAMPLES: 1024 (128)
        RATE: 8000      BUFFERSIZE: 8192        CHUNK_SIZE_IN_SAMPLES: 4096 (512)
        RATE: 16000     BUFFERSIZE: 8192        CHUNK_SIZE_IN_SAMPLES: 4096 (256) *
        RATE: 16000     BUFFERSIZE: 16384       CHUNK_SIZE_IN_SAMPLES: 8192 (512)
        RATE: 44100     BUFFERSIZE: 4096        CHUNK_SIZE_IN_SAMPLES: 1024 (...)
     */

    private final static int RATE = 16000;
    private final static int CHANNEL_MODE = AudioFormat.CHANNEL_IN_MONO;
    private final static int ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    int BUFFERSIZE = 8192 ;//AudioRecord.getMinBufferSize(RATE, CHANNEL_MODE, ENCODING);
    int SOURCE = MediaRecorder.AudioSource.MIC;

    private DoubleFFT_1D fft;

    short[] audio_data;

    private final static int CHUNK_SIZE_IN_SAMPLES = 4096;

    private final static int CHUNK_SIZE_IN_MS = 1000 * CHUNK_SIZE_IN_SAMPLES / RATE;

    private final static int CHUNK_SIZE_IN_BYTES = RATE * CHUNK_SIZE_IN_MS / 1000 * 2;

    private final static int MIN_FREQUENCY = 131; //C3

    private final static int MAX_FREQUENCY = 1976; //B6

    public PitchDec(MainActivity parent, Handler handler) {
        parent_ = parent;
        handler_ = handler;
    }

    private static class FreqResult {
        public double best_frequency;
    }

    public static class FrequencyCluster {
        public double average_frequency = 0;
        public double total_amplitude = 0;

        public void add(double freq, double amplitude) {
            double new_total_amp = total_amplitude + amplitude;
            average_frequency = (total_amplitude * average_frequency + freq * amplitude) / new_total_amp;
            total_amplitude = new_total_amp;
        }

        public boolean isNear(double freq) {
            if (Math.abs(1 - (average_frequency / freq)) < 0.05) {
                // only 5% difference
                return true;
            } else {
                return false;
            }
        }

        public boolean isHarmonic(double freq) {
            double harmonic_factor = freq / average_frequency;
            double distance_from_int = Math.abs(Math.round(harmonic_factor) - harmonic_factor);
            if (distance_from_int < 0.05) {
                // only 5% distance
                return true;
            } else {
                return false;
            }
        }

        @Override public String toString() {
            return "(" + average_frequency + ", " + total_amplitude + ")";
        }
    }

    //Analyzes sound from microphone, does FFT.
    public FreqResult AnalyzeFrequencies(short[] audio_data) {
        fft = new DoubleFFT_1D(CHUNK_SIZE_IN_SAMPLES);
        FreqResult fr = new FreqResult();

        double[] data = new double[CHUNK_SIZE_IN_SAMPLES * 2];
        final int min_frequency_fft = Math.round(MIN_FREQUENCY
                * CHUNK_SIZE_IN_SAMPLES / RATE);
        final int max_frequency_fft = Math.round(MAX_FREQUENCY
                * CHUNK_SIZE_IN_SAMPLES / RATE);

        for (int i = 0; i < CHUNK_SIZE_IN_SAMPLES; i++) {
            data[i * 2] = audio_data[i];
            data[i * 2 + 1] = 0;
        }

        if(MainActivity.runFFT)fft.complexForward(data);

        /*
        if(saveCounter<filesToSave) {
            saveAudiodata_afterFFT(data);
            saveCounter++;
        }*/

        double best_frequency;
        double best_amplitude = 0;

        List<Double> best_frequencies = new ArrayList<Double>();
        List<Double> best_amps = new ArrayList<Double>();

        for (int i = min_frequency_fft; i <= max_frequency_fft; i++) {

            final double current_frequency = i * 1.0 * RATE / CHUNK_SIZE_IN_SAMPLES;

            final double current_amplitude = Math.pow(Math.pow(data[i * 2], 2)
                    + Math.pow(data[i * 2 + 1], 2), 0.5);

            if (current_amplitude > best_amplitude) {
                best_amplitude = current_amplitude;

                best_frequencies.add(current_frequency);
                best_amps.add(best_amplitude);
            }
        }

        List<FrequencyCluster> clusters = new ArrayList<FrequencyCluster>();
        FrequencyCluster currentCluster = new FrequencyCluster();
        clusters.add(currentCluster);

        if (best_frequencies.size() > 0)
        {
            currentCluster.add(best_frequencies.get(0), best_amps.get(0));
        }

        // join clusters
        for(int i = 1; i < best_frequencies.size(); i++)
        {
            double freq = best_frequencies.get(i);
            double amp = best_amps.get(i);

            if (currentCluster.isNear(freq)) {
                currentCluster.add(freq, amp);
                continue;
            }

            // this isn't near, and isn't harmonic, it's a different one.
            // NOTE: assuming harmonies are consecutive (no unharmonics in between harmonies)
            currentCluster = new FrequencyCluster();
            clusters.add(currentCluster);
            currentCluster.add(freq, amp);
        }

        // join harmonies
        FrequencyCluster nextCluster;
        for(int i = 1; i < clusters.size(); i ++) {
            currentCluster = clusters.get(i - 1);
            nextCluster = clusters.get(i);
            if (currentCluster.isHarmonic(nextCluster.average_frequency)) {
                currentCluster.total_amplitude += nextCluster.total_amplitude;
            }
        }


        best_amplitude = 0;
        best_frequency = 0;
        for(int i = 0; i < clusters.size(); i ++) {
            FrequencyCluster clu = clusters.get(i);
            if (best_amplitude < clu.total_amplitude) {
                best_amplitude = clu.total_amplitude;
                best_frequency = clu.average_frequency;
            }
        }

        fr.best_frequency = best_frequency;

        return fr;
    }

    //Starts recording and pitchdetector
    public void run() {
        android.os.Process
                .setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        recorder_ = new AudioRecord(SOURCE, RATE, CHANNEL_MODE,
                ENCODING, BUFFERSIZE);

        if (recorder_.getState() != AudioRecord.STATE_INITIALIZED) {
            ShowError("Can't initialize AudioRecord");
            return;
        }

        recorder_.startRecording();
        while (!Thread.interrupted()) {

            audio_data = new short[BUFFERSIZE / 2];
            recorder_.read(audio_data, 0, CHUNK_SIZE_IN_BYTES / 2);

            double volume = getAmplitude(audio_data);

            if(volume>4400) {
                //pitchdetector
                FreqResult fr = AnalyzeFrequencies(audio_data);
                PostToUI(fr.best_frequency);
            }else{
                PostPauseToUI();
            }

        }
        recorder_.stop();
        recorder_.release();
    }

    //Returns amplitude of sound from recorder reading, used to avoid picking up background noise.
    public double getAmplitude(short[] audio_data) {
        recorder_.read(audio_data, 0, BUFFERSIZE);

        int max = 0;
        for (short s : audio_data){
            if (Math.abs(s) > max){
                max = Math.abs(s);
            }
        }
        return max;
    }

    private void PostPauseToUI() {
        handler_.post(new Runnable() {
            public void run() {
                parent_.writePause();            }
        });
    }

    private void PostToUI(final double pitch) {
        handler_.post(new Runnable() {
            public void run() {
                parent_.ShowPitchDetectionResult(pitch);
            }
        });
    }

    private void ShowError(final String msg) {
        handler_.post(new Runnable() {
            public void run() {
                new AlertDialog.Builder(parent_).setTitle("VeryNote")
                        .setMessage(msg).show();
            }
        });
    }

    private MainActivity parent_;
    private AudioRecord recorder_;
    private Handler handler_;
}