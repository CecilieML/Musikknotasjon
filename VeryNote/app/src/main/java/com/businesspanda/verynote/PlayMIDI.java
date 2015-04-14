package com.businesspanda.verynote;

import android.os.Environment;
import android.widget.Toast;

import org.jfugue.Pattern;
import org.jfugue.Player;

import java.io.File;
import java.io.IOException;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;


public class PlayMIDI {

    public void playMIDI(Pattern pattern) {
        Player player = new Player();
        String filepath = Environment.getExternalStorageDirectory().toString();

        try {

            File file = new File(filepath, "music.mid");
            player.saveMidi(pattern, file);

        } catch (IOException e) {
            Toast.makeText(Config.context, "Problem generating MIDI!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        /*File path = Config.context.getFileStreamPath("music.midi");

        MediaPlayer mediaPlayer = MediaPlayer.create(Config.context, Uri.fromFile(path));
        mediaPlayer.start(); // no need to call prepare(); create() does that for you

        mediaPlayer.release();
        mediaPlayer = null;*/


        try {
            player.playMidiDirectly(new File(filepath, "music.mid"));
            Toast.makeText(Config.context, "IMA PLAYING THE MIDIS!!!!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(Config.context, "IOEXceotion", Toast.LENGTH_SHORT).show();
        } catch (InvalidMidiDataException e)
        {
            e.printStackTrace();
            Toast.makeText(Config.context, "invalidmidiexception", Toast.LENGTH_SHORT).show();
        }

    }

}
