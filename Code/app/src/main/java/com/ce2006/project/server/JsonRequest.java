package com.ce2006.project.server;

import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Wrapper of request that parse the results before returning
 * Created by lhtan on 30/3/15.
 */
public class JsonRequest extends Request {

    public JsonRequest(String url) {
        super(url);
    }

    public static JsonRequest createRequest(String url) {
        return new JsonRequest(url);
    }

    /**
     * parse the returning object as json string
     * @return
     */
    public Object execute() {
        try {
            String c = (String) super.execute();
            if (c.length() > 2) {
                c = c.substring(1, c.length() - 1).replaceAll("\\\\", "");
                Log.d("Tan", c);
                JSONParser parser = new JSONParser();

                Object obj = parser.parse(c);
                return (JSONObject) obj;
            }
        } catch (Exception e) {
            Log.d("Tan", "exception: " + e);
            e.printStackTrace();
            return null;
        }
        return null;
    }

}
