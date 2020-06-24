package ITM.maint.fiix_custom_mobile.data.api;

import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import ITM.maint.fiix_custom_mobile.data.api.responses.FindResponse;
import okhttp3.RequestBody;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ResponseConverterFactory extends Converter.Factory {

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        try
        {
            Type responseType = TypeToken.getParameterized(FindResponse.class, type).getType();
            final Converter<ResponseBody, FindResponse<?>> converter = retrofit.nextResponseBodyConverter(
                    this, responseType, annotations);
            return (Converter<ResponseBody, ?>) body -> {
                FindResponse<?> response = converter.convert(body);
                return response.getObjects();
            };
        } catch (Exception e) {
            return null;
        }
    }

}
