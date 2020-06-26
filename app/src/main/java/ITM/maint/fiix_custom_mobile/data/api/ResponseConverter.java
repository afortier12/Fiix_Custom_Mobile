package ITM.maint.fiix_custom_mobile.data.api;

import java.io.IOException;

import javax.annotation.Nullable;

import ITM.maint.fiix_custom_mobile.data.api.responses.FindResponse;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public class ResponseConverter<T> implements Converter<ResponseBody, T> {
    final private Converter<ResponseBody, FindResponse<T>> converter;

    public ResponseConverter(Converter<ResponseBody, FindResponse<T>> converter){
        this.converter = converter;
    }

    @Nullable
    @Override
    public T convert(ResponseBody value) throws IOException {
        FindResponse<T> responseModel = converter.convert(value);
        return responseModel.getObjects();

    }
}
