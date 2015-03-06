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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import nu.xom.Serializer;


/**
 * Created by CecilieMarie on 03.03.2015.
 */
public class ExportXML implements Serializable {

    String filename = "music.xml";

    void export() {
        try {

            FileOutputStream fos = Config.context.openFileOutput(filename, Context.MODE_PRIVATE);

            MusicXmlRenderer renderer = new MusicXmlRenderer();
            MusicStringParser parser = new MusicStringParser();
            parser.addParserListener(renderer);

            Pattern pattern = new Pattern("C D E F G A B |");
            parser.parse(pattern);

            Serializer serializer = new Serializer(fos, "UTF-8");
            serializer.setIndent(4);
            serializer.write(renderer.getMusicXMLDoc());

            System.out.println("var i void :3");

            fos.flush();
            fos.close();

            copyFileToInternal();
        } catch (IOException e) {
            Toast.makeText(Config.context, "PROBLEMS!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void copyFileToInternal() {
        try {
            InputStream is = Config.context.getAssets().open("music.xml");

            File cacheDir = Config.context.getCacheDir();
            File outFile = new File(cacheDir, "music.xml");

            OutputStream os = new FileOutputStream(outFile.getAbsolutePath());

            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) > 0) {
                os.write(buff, 0, len);
            }
            os.flush();
            os.close();
            is.close();
            System.out.println("copied file to cache!!!");

        } catch (IOException e) {
            e.printStackTrace(); // TODO: should close streams properly here
        }
    }

    void sendToEmail() {

        //File path = Config.context.getFileStreamPath(filename);

        Uri uri = Uri.parse("content://com.businesspanda.verynote/" + filename); //

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        //sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(path));
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        //sendIntent.setDataAndType(uri, "application/xml"); //
        sendIntent.setType("application/xml");
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Config.context.startActivity(Intent.createChooser(sendIntent, "Share!"));

    }

}
