package com.businesspanda.verynote;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import org.jfugue.Pattern;
import org.jfugue.Player;

import java.io.File;
import java.io.IOException;

/**
 * Created by CecilieMarie on 23.03.2015.
 */
public class PlayMIDI {

    public void playMIDI(Pattern pattern) {
        try {
            String filepath = Environment.getExternalStorageDirectory().toString() + "/VeryNote/";
            File file = new File(filepath, "music.mid");

            Player player = new Player();
            player.saveMidi(pattern, file);
        } catch (IOException e) {
            Toast.makeText(Config.context, "Problem generating MIDI!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        File path = Config.context.getFileStreamPath("music.mid");

        MediaPlayer mediaPlayer = MediaPlayer.create(Config.context, Uri.fromFile(path));
        mediaPlayer.start(); // no need to call prepare(); create() does that for you

        mediaPlayer.release();
        mediaPlayer = null;

    }

}
