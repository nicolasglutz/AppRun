package ch.bfh.bootcamp.utils;

import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

public class LogAppUtil {

    private static final String APPLICATION_PATH_LOGAPP = "ch.apprun.logmessage";

    public static Intent createIntent(String solution) {
        Intent intent = new Intent("ch.apprun.intent.LOG");
// format depends on app, see logbook format guideline
        JSONObject log = new JSONObject();
        try {
            log.put("task", "MetallDetektor");
            log.put("solution", solution);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra(APPLICATION_PATH_LOGAPP, log.toString());
        return intent;
    }
}
