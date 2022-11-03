package ch.apprun.pixelmaler.utils;

import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import ch.apprun.pixelmaler.Pixel;

public class LogAppUtil {

    private static final String APPLICATION_PATH_LOGAPP = "ch.apprun.logmessage";
    private static final String TASK_NAME = "Pixelmaler";

    public static Intent createIntent(List<Pixel> pixels) {
// format depends on app, see logbook format guideline
        JSONObject log = new JSONObject();
        JSONArray points = new JSONArray();


        try {

            for(int i = 0; i < pixels.size(); i++){

                JSONObject pixelObject = new JSONObject();

                pixelObject.put("y",pixels.get(i).getyNr());
                pixelObject.put("x",pixels.get(i).getxNr());
                pixelObject.put("color",String.format("#%06X", (0xFFFFFF & pixels.get(i).getDrawPaint().getColor())));

                points.put(pixelObject);
            }


            log.put("task", TASK_NAME);
            log.put("pixels", points);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent("ch.apprun.intent.LOG");

        intent.putExtra(APPLICATION_PATH_LOGAPP, log.toString());
        return intent;
    }


}