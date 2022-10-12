package ch.bfh.treasuremap.utils;

import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.util.ArrayList;
import java.util.List;

public class LogAppUtil {

    private static final String APPLICATION_PATH_LOGAPP = "ch.apprun.logmessage";
    private static final String TASK_NAME = "Schatzkarte";

    public static Intent createIntent(List<Overlay> overlays) {
        Intent intent = new Intent("ch.apprun.intent.LOG");
// format depends on app, see logbook format guideline
        JSONObject log = new JSONObject();
        JSONArray points = new JSONArray();


        try {

            for (Overlay overlay : overlays) {

                if (overlay instanceof Marker) {

                    Marker marker = (Marker) overlay;

                    if (marker.getTitle() == "Position") {

                       JSONObject markerObject = new JSONObject();

                        markerObject.put("lat",marker.getPosition().getLatitude());
                        markerObject.put("lon",marker.getPosition().getLongitude());

                        points.put(markerObject);
                    }
                }
            }


            log.put("task", TASK_NAME);
            log.put("points", points);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra(APPLICATION_PATH_LOGAPP, log.toString());
        return intent;
    }


}
