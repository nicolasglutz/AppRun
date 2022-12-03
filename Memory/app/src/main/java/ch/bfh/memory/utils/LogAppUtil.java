package ch.bfh.memory.utils;

import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ch.bfh.memory.models.MemoryPair;

public class LogAppUtil {
    public static final String APPLICATION_PATH_LOGAPP = "ch.apprun.logmessage";
    private static final String TASK_NAME = "Memory";

    public static Intent createIntent( List<MemoryPair> pairs ) {
        Intent intent = new Intent("ch.apprun.intent.LOG");
// format depends on app, see logbook format guideline
        JSONObject log = new JSONObject();
        JSONArray solution = new JSONArray();
        for (MemoryPair pair : pairs) {
            if (pair.isComplete()) {
                JSONArray arrayPair = new JSONArray();
                arrayPair.put(pair.cardOne.getWord());
                arrayPair.put(pair.cardTwo.getWord());
                solution.put(arrayPair);
            }
        }

        try {
            log.put("task", TASK_NAME);
            log.put("solution", solution);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra(APPLICATION_PATH_LOGAPP, log.toString());
        return intent;
    }
}
