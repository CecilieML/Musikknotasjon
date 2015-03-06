package com.businesspanda.verynote;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import org.jfugue.MusicStringParser;
import org.jfugue.MusicXmlRenderer;
import org.jfugue.Pattern;
import org.jfugue.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import nu.xom.Serializer;


/**
 * Created by CecilieMarie on 03.03.2015.
 */
public class ExportXML implements Serializable {

    String filename = "music.xml";

    void saveToFile(Pattern pattern) {
        try {

            FileOutputStream fos = Config.context.openFileOutput(filename, Context.MODE_WORLD_READABLE);

            MusicXmlRenderer renderer = new MusicXmlRenderer();
            MusicStringParser parser = new MusicStringParser();
            parser.addParserListener(renderer);

            //Pattern pattern = new Pattern("C D E F G A B |");
            parser.parse(pattern);

            Serializer serializer = new Serializer(fos, "UTF-8");
            serializer.setIndent(4);
            serializer.write(renderer.getMusicXMLDoc());

            System.out.println("var i void :3");

            fos.flush();
            fos.close();
        } catch (IOException e) {
            Toast.makeText(Config.context, "PROBLEMS!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    void sendToEmail() {
        File path = Config.context.getFileStreamPath(filename);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(path));
        sendIntent.setType("application/xml");
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Config.context.startActivity(Intent.createChooser(sendIntent, "Share!"));

    }

}
