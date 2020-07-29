package ITM.maint.fiix_custom_mobile.data.model.Converters;

import androidx.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import ITM.maint.fiix_custom_mobile.data.model.entity.Source;
import ITM.maint.fiix_custom_mobile.data.model.entity.Problem;
import ITM.maint.fiix_custom_mobile.data.model.entity.Cause;



public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static ArrayList<String> fromTimestamp(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
        // return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static String arraylistToString(ArrayList<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        return json;
        // return date == null ? null : date.getTime();
    }


    @TypeConverter
    public static ArrayList<Source> fromSourceList(String value){
        Type listType = new TypeToken<ArrayList<Source>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String  sourceListToString(ArrayList<Source> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<Problem> fromProblemList(String value){
        Type listType = new TypeToken<ArrayList<Source>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String problemListToString(ArrayList<Problem> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<Cause> fromCauseList(String value){
        Type listType = new TypeToken<ArrayList<Source>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String causeListToString(ArrayList<Cause> list){
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


}
