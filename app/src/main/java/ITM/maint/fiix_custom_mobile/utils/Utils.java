package ITM.maint.fiix_custom_mobile.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.io.Resources;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ITM.maint.fiix_custom_mobile.R;

public class Utils {

    public static final float[] NEGATIVE = {
            -1.0f, 0, 0, 0, 255, // red
            0, -1.0f, 0, 0, 255, // green
            0, 0, -1.0f, 0, 255, // blue
            0, 0, 0, 1.0f, 0  // alpha
    };
    public static final String SYS_DELETE_DATE = "18000"; //Jan 1 1970, 00:00:00

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

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

    public static Drawable getPriorityIcon(int order, Context itemView) {
        if (order < 6) {
            return ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.ic_report_24px, null);
        } else if (order < 8) {
            return ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.ic_high_priority, null);
        } else if (order < 9) {
            return ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.ic_medium_priority, null);
        } else {
            return ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.ic_calendar, null);
        }
    }


    public static String convertDateTimeToUNIXEpochms(String datetime) {
        return null;
    }

    public static List<Integer> splitStringToListOfInt(String stringToSplit) throws Exception {
        String[] splitStrings = stringToSplit.trim().split(" ");
        List<Integer> returnList = new ArrayList<>();
        for (String string : splitStrings) {
            returnList.add(Integer.parseInt(string));
        }
        return returnList;
    }


}
