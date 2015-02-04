package com.businesspanda.sheetnotes;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import org.jtransforms.fft.DoubleFFT_1D;
import pl.edu.icm.jlargearrays.*;

public class MainActivity extends Activity {

    int audioSource = MediaRecorder.AudioSource.MIC;    // Audio source is the device mic
    int channelConfig = AudioFormat.CHANNEL_IN_MONO;    // Recording in mono
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT; // Records in 16bit


    private DoubleFFT_1D fft;                           // The fft double array
    int blockSize = 1024;                               // deal with this many samples at a time
    int sampleRate = 44100;                              // Sample rate in Hz
    public double frequency = 0.0;                      // the frequency given

    RecordAudio recordTask;                             // Creates a Record Audio command
    TextView tv;                                        // Creates a text view for the frequency
    boolean started = false;
    Button startStopButton;

    int nmb = 0;

    //private static AudioRecord audioRecorder;

    //checks for microphone
    protected boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }

    // On Start Up
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.note);

        startStopButton = (Button) findViewById(R.id.startStopButton);

        if (!hasMicrophone())
        {
            startStopButton.setEnabled(false);
        }

    }




    // The Record and analysis class
    private class RecordAudio extends AsyncTask<Void, Double, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

        /*Calculates the fft and frequency of the input*/
            try {

                int bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioEncoding);                // Gets the minimum buffer needed
                AudioRecord audioRecord = new AudioRecord(audioSource, sampleRate, channelConfig, audioEncoding, bufferSize);   // The RAW PCM sample recording

                short[] buffer = new short[blockSize];          // Save the raw PCM samples as short bytes
                double[] audioDataDoubles = new double[(blockSize * 2)]; // Same values as above, as doubles

                double[] re = new double[blockSize];
                double[] im = new double[blockSize];
                double[] magnitude = new double[blockSize];

                audioRecord.startRecording();                   // Start working
                fft = new DoubleFFT_1D(blockSize);


                while (started) {
                 /* Reads the data from the microphone. it takes in data
                 * to the size of the window "blockSize". The data is then
                 * given in to audioRecord. The int returned is the number
                 * of bytes that were read*/

                    int bufferReadResult = audioRecord.read(buffer, 0, blockSize);

                    // Read in the data from the mic to the array
                    for (int i = 0; i < blockSize && i < bufferReadResult; i++) {

                    /* dividing the short by 32768.0 gives us the
                     * result in a range -1.0 to 1.0.
                     * Data for the compextForward is given back
                     * as two numbers in sequence. Therefore audioDataDoubles
                     * needs to be twice as large*/

                        audioDataDoubles[2 * i] = (double) buffer[i] / 32768.0; // signed 16 bit
                        audioDataDoubles[(2 * i) + 1] = 0.0;
                    }

                    //audiodataDoubles now holds data to work with
                    fft.complexForward(audioDataDoubles);

                    // Calculate the Real and imaginary and Magnitude.
                    for (int i = 0; i < blockSize; i++) {
                        // real is stored in first part of array
                        re[i] = audioDataDoubles[i * 2];
                        // imaginary is stored in the sequential part
                        im[i] = audioDataDoubles[(i * 2) + 1];
                        // magnitude is calculated by the square root of (imaginary^2 + real^2)
                        magnitude[i] = Math.sqrt((re[i] * re[i]) + (im[i] * im[i]));
                    }

                    double peak = -1.0;
                    // Get the largest magnitude peak
                    for (int i = 0; i < blockSize; i++) {
                        if (peak < magnitude[i])
                            peak = magnitude[i];
                    }

                    // calculated the frequency
                    frequency = (sampleRate * peak) / blockSize;

                /* calls onProgressUpdate
                 * publishes the frequency
                 */
                    publishProgress(frequency);
                    Thread.sleep(500);
                }

            } catch (Throwable t) {
                Log.e("AudioRecord", "Recording Failed");
            }

            return null;
        }

        // This should display the Frequency
        @Override
        protected void onProgressUpdate(Double... frequency) {

            //print the frequency
           /** String info = Double.toString(frequency[0]);**/

            String test = findNote(frequency[0]);

            //TextView doubleView = (TextView) findViewById(R.id.DoubleView);
            tv.setText(test);
        }
    }


    public String findNote(double freq){

        String note = " ";

        if(freq<130){
            note = "Less than C3";
        }else if(freq>130 && freq<138){
            note = "C3";
        }else if(freq>138 && freq<146) {
            note = "C#3";
        }else if(freq>146 && freq<155){
            note = "D3";
        }else if(freq>155 && freq<164) {
            note = "Eb3";
        }else if(freq>164 && freq<174){
            note = "E3";
        }else if(freq>174 && freq<185) {
            note = "F3";
        }else if(freq>185 && freq<196){
            note = "F#3";
        }else if(freq>196 && freq<207) {
            note = "G3";
        }else if(freq>207 && freq<220){
            note = "G#3";
        }else if(freq>220 && freq<233) {
            note = "A3";
        }else if(freq>233 && freq<246){
            note = "Bb3";
        }else if(freq>246 && freq<261) {
            note = "B";
        }else if(freq>261){
            note = "Higher than C4";
        }
        return note;

    }


    // For the click of the button
    public void startStop(View v) {
        if (started) {
            started = false;
            startStopButton.setText("Start");
            startStopButton.setEnabled(false);
            recordTask.cancel(true);
        } else {
            started = true;
            startStopButton.setText("Stop");

            recordTask = new RecordAudio();
            recordTask.execute();
        }
    }

    public void changeBackColor(View view){
        final Button clickTochange = (Button) findViewById(R.id.buttonChangeBack);
        final RelativeLayout theLayout = (RelativeLayout) findViewById(R.id.myLayout);

        if (nmb % 2 == 0){
            clickTochange.setTextColor(0xff00ffff);
            theLayout.setBackgroundColor(0xffff00dd);
            nmb++;
        }else {
            clickTochange.setTextColor(0xffff00dd);
            theLayout.setBackgroundColor(0xff00ffff);
            nmb++;
        }

    }

}


/*
public class MainActivity extends ActionBarActivity {

    private static AudioRecord audioRecorder;
    private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;

    private static String audioFilePath;
    private static Button stopButton;
    private static Button playButton;
    private static Button recordButton;

    private boolean isRecording = false;

    //checks for microphone
    protected boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordButton = (Button) findViewById(R.id.recordButton);
        playButton = (Button) findViewById(R.id.playButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        if (!hasMicrophone())
        {
            stopButton.setEnabled(false);
            playButton.setEnabled(false);
            recordButton.setEnabled(false);
        } else {
            playButton.setEnabled(false);
            stopButton.setEnabled(false);
        }

        audioFilePath =
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/myaudio.3gp";
    }

    public void recordAudio (View view) throws IOException
    {
        isRecording = true;
        stopButton.setEnabled(true);
        playButton.setEnabled(false);
        recordButton.setEnabled(false);

        int bufferSize= AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT)*2;

        audioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
/*
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }



        short[] sourceBuffer = new short[bufferSize];
        double[] fftBuffer = new double[bufferSize];

        DoubleFFT_1D fft1d = new DoubleFFT_1D(bufferSize / 2);

        audioRecorder.startRecording();
        while(isRecording){
            int read = audioRecorder.read(sourceBuffer, 0, bufferSize);

            if(read != AudioRecord.ERROR_INVALID_OPERATION){
                for(int i = 0; i< bufferSize && i<read; ++i){
                    fftBuffer[i] = (double)(sourceBuffer[i]);


                }
            }


        }
        System.out.println("TESSSSSSSSSSSSSSSST!!!!!!!!!!!*****" + fftBuffer+ fft1d);
        //fft1d.realForward(fftBuffer);

    }

    public void stopAudio (View view)
    {

        stopButton.setEnabled(false);
        playButton.setEnabled(true);

        if (isRecording)
        {
            recordButton.setEnabled(false);
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
        } else {
            mediaPlayer.release();
            mediaPlayer = null;
            recordButton.setEnabled(true);
        }
    }

    public void playAudio (View view) throws IOException
    {
        playButton.setEnabled(false);
        recordButton.setEnabled(false);
        stopButton.setEnabled(true);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(audioFilePath);
        mediaPlayer.prepare();
        mediaPlayer.start();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */
