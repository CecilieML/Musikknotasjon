package com.businesspanda.verynote;

import android.content.Context;

import org.jfugue.MusicStringParser;
import org.jfugue.MusicXmlRenderer;
import org.jfugue.Pattern;
import org.jfugue.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import nu.xom.Serializer;


/**
 * Created by CecilieMarie on 03.03.2015.
 */
public class ExportXML implements Serializable {

    void export() {
        try {
            FileOutputStream file = Config.context.openFileOutput("music.xml", Context.MODE_PRIVATE);

            MusicXmlRenderer renderer = new MusicXmlRenderer();
            MusicStringParser parser = new MusicStringParser();
            parser.addParserListener(renderer);

            Pattern pattern = new Pattern("C D E F G A B |");
            parser.parse(pattern);

            Serializer serializer = new Serializer(file, "UTF-8");
            serializer.setIndent(4);
            serializer.write(renderer.getMusicXMLDoc());

            System.out.println("var i void :3");

            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
