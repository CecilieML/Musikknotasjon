package com.businesspanda.verynote;

/** Copyright (C) 2015 by BusinessPanda - Cecilie M. Langfeldt, Helene H. Larsen.
 **
 ** Permission to use, copy, modify, and distribute this software and its
 ** documentation for any purpose and without fee is hereby granted, provided
 ** that the above copyright notice appear in all copies and that both that
 ** copyright notice and this permission notice appear in supporting
 ** documentation.  This software is provided "as is" without express or
 ** implied warranty.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import org.jfugue.MusicStringParser;
import org.jfugue.MusicXmlRenderer;
import org.jfugue.Pattern;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import nu.xom.Serializer;


public class ExportXML implements Serializable {

    String filename = "untitled.xml";

    public void setFilename(String filename) {
        this.filename = filename + ".xml";
    }

    public String convertArrayListToString(ArrayList<Note> notesList) {
        //Converts the ArrayList to a string the parser can read.
        double ppqCconversion = 0.0041666667; //Constant for converting BPM -> PPQ.

        String allNotes = "T"+ (int)Math.ceil(60/((60000/Config.context.metronomNmb)* ppqCconversion)); //BPM -> PPQ conversion.

        for (Note note : notesList) {
            if(note.getDurationOfNote() != "" ) {

                String noteFromList = note.getName().replaceAll("s", "#");

                if(note.getName().equals("|")){
                    allNotes = allNotes + " " + noteFromList;
                } else {
                    allNotes = allNotes + " " + noteFromList + note.getDurationOfNote();
                }
            }
        }
        return allNotes;
    }

    void saveToFile(Pattern pattern) {
        //Parses current pattern and temporarily saves to phone internal memory.

        try {

            FileOutputStream fos = Config.context.openFileOutput(filename, Context.MODE_WORLD_READABLE);

            MusicXmlRenderer renderer = new MusicXmlRenderer();
            MusicStringParser parser = new MusicStringParser();
            parser.addParserListener(renderer);

            parser.parse(pattern);

            Serializer serializer = new Serializer(fos, "UTF-8");
            serializer.setIndent(4);
            serializer.write(renderer.getMusicXMLDoc());

            fos.flush();
            fos.close();
        } catch (IOException e) {
            Toast.makeText(Config.context, "Problem exporting XML.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    void saveToSD(Pattern pattern) {
        //Parses current pattern and permanently saves to phone SD memory (either external or partition of internal memory).

        try {
            if(checkIfSDPresent()) {
                String filepath = Environment.getExternalStorageDirectory().toString();
                File file = new File(filepath,filename);

                if (!file.exists()) {
                    FileOutputStream fos = new FileOutputStream(file);

                    MusicXmlRenderer renderer = new MusicXmlRenderer();
                    MusicStringParser parser = new MusicStringParser();
                    parser.addParserListener(renderer);

                    parser.parse(pattern);

                    Serializer serializer = new Serializer(fos, "UTF-8");
                    serializer.setIndent(4);
                    serializer.write(renderer.getMusicXMLDoc());

                    fos.flush();
                    fos.close();

                    Toast.makeText(Config.context, "File saved.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Config.context, "File with that name already exists.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Config.context, "No SD-card found.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(Config.context, "Problem saving XML.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static boolean checkIfSDPresent() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    void sendToEmail() {
        //Opens the dialog to share the XML-file via mainly email, but other channels such as GoogleDisk will
        //appear if installed on phone.

        File path = Config.context.getFileStreamPath(filename);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(path));
        sendIntent.setType("application/xml");
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Config.context.startActivity(Intent.createChooser(sendIntent, "Share!"));

    }

}
