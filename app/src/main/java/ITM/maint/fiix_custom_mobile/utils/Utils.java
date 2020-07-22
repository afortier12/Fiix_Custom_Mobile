package ITM.maint.fiix_custom_mobile.utils;

import android.graphics.Color;

import androidx.core.graphics.ColorUtils;

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

    public static boolean isColorDark(long color) {
        int red = (int) (color >> 16) & 0xff;
        int green = (int) (color >> 8) & 0xff;
        int blue = (int) (color) & 0xff;
        double luma = ((0.299 * red) + (0.587 * green) + (0.114 * blue)) / 255;

        if (luma > 0.5f) {
            return false; // It's a light color
        } else {
            return true; // It's a dark color

        }
    }
}
