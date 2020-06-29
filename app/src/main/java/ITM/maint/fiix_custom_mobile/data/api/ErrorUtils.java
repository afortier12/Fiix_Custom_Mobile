package ITM.maint.fiix_custom_mobile.data.api;

import android.util.Log;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import ITM.maint.fiix_custom_mobile.data.api.ServiceGenerator;
import ITM.maint.fiix_custom_mobile.data.api.responses.APIError;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;


public class ErrorUtils {

    private static final String TAG = "ErrorUtils";

    public static APIError parseError(final Response<?> response) {
        JSONObject bodyObj = null;
        ArrayList<String> messages = new ArrayList<>();

        try {
            String errorBody = response.errorBody().string();
            if (errorBody != null) {
                bodyObj = new JSONObject(errorBody);
                JSONArray errArray = bodyObj.getJSONArray("error");
                JSONObject errObj = errArray.getJSONObject(0);
                messages.add(errObj.optString("message"));
            } else {
                messages.add("Unable to parse error");
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
            messages.add("Unable to parse error");
        }

        return new APIError.Builder()
                .messages(messages)
                .build();
    }


}
