package ITM.maint.fiix_custom_mobile.utils;

public class Utils {

    public static String padLeftZeros(String str, int n) {
        return String.format("%1$" + n + "s", str).replace(' ', '0');
    }

    public static String padRightZeros(String str, int n) {
        return String.format("%1$-" + n + "s", str).replace(' ', '0');
    }
}
