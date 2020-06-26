package ITM.maint.fiix_custom_mobile.data.api;

import android.util.Log;

import com.google.android.gms.common.util.Hex;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ITM.maint.fiix_custom_mobile.constants.Fiix;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    private static final String TAG = "ServiceGenerator";

    protected static Retrofit retrofit;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static final Gson gson = new GsonBuilder()
            .create();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Fiix.FIIX_URL.getField())
                    .addConverterFactory(new ResponseConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create(gson));

    private static String requestUrl = Fiix.FIIX_URL.getField() +"/api/?action=FindResponse&appKey="+Fiix.API_key.getField()+"&accessKey="+Fiix.Access_key.getField()+"&signatureMethod=HmacSHA256&signatureVersion=1";

    public static <S> S createService(Class<S> serviceClass) {

        httpClient.interceptors().clear();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);

        HeaderInterceptor headerInterceptor = new HeaderInterceptor();
        QueryInterceptor queryInterceptor = new QueryInterceptor();
        ResponseInterceptor responseInterceptor = new ResponseInterceptor();

        httpClient.addInterceptor(interceptor);
        httpClient.addInterceptor(headerInterceptor);
        httpClient.addInterceptor(queryInterceptor);
        httpClient.addInterceptor(responseInterceptor);

        builder.client(httpClient.build());
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

    private static class ResponseInterceptor implements Interceptor{

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            if (response.body().contentType().equals(MediaType.parse("text/plain;charset=utf-8"))) {
                String jsonString = response.body().string();
                Log.d(TAG, jsonString);
                Log.d(TAG, response.headers().toString());
                Log.d(TAG, response.message());
                Log.d(TAG, String.valueOf(response.code()));

                JSONObject jsonObject = null;
                InputStream is = new ByteArrayInputStream(jsonString.getBytes());
                JsonReader jsonReader = new JsonReader(new InputStreamReader(is));
                jsonReader.beginObject();
                while( jsonReader.hasNext() ){

                }

                ResponseBody body = ResponseBody.create(jsonString, response.body().contentType());

                return response.newBuilder()
                        .body(body)
                        .build();

            }
            return response;
        }
    }


    private static class HeaderInterceptor implements Interceptor {

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("action","FindResponse")
                    .addQueryParameter("appKey", Fiix.API_key.getField())
                    .addQueryParameter("accessKey",Fiix.Access_key.getField())
                    .addQueryParameter("signatureMethod", "HmacSHA256")
                    .addQueryParameter("signatureVersion", "1")
                    .build();

            Request.Builder requestBuilder = original. newBuilder()
                    .url(url);
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    }

    private static class QueryInterceptor implements Interceptor{

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder requestBuilder = original. newBuilder()
                    .header("Content-Type","text/plain")
                    .header("Authorization", authString())
                    .addHeader("Content-Type","text/plain");
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }

        private String authString(){

            String message = null;
            String hmacString = null;

            //Trim off protocol
            if (requestUrl.indexOf("http://") == 0)
            {
                message = requestUrl.substring("http://".length());
            }
            else if (requestUrl.indexOf("https://") == 0)
            {
                message = requestUrl.substring("https://".length());
            }
            try {
                //Get Message bytes as UTF-8
                byte[] messageBytes = message.getBytes("UTF-8");
                //Get secret key bytes as UTF-8
                byte[] credentialsSecretKeyBytes = Fiix.API_secret.getField().getBytes("UTF-8");
                //generate the signing key
                SecretKey signingKey = new SecretKeySpec(credentialsSecretKeyBytes, "HmacSHA256");
                //generate the MAC
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(signingKey);
                //get the signature
                byte[] hmac = mac.doFinal(messageBytes);
                hmacString = new String(Hex.bytesToStringLowercase(hmac));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }

            return hmacString;
        }

    }


}