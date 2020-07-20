package ITM.maint.fiix_custom_mobile.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class Utils {

    // Serialize a single object.
    public static String serializeToJson(Box box, Type type) {
        Gson gson = new Gson();
        return gson.toJson(box, type);
    }

    // Deserialize to single object.
    public static Box deserializeFromJson(String jsonString, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, type);
    }
}
