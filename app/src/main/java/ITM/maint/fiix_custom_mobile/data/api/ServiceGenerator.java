package ITM.maint.fiix_custom_mobile.data.api;

import com.google.android.gms.common.util.Hex;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ITM.maint.fiix_custom_mobile.constants.Fiix;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static Retrofit retrofit;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Fiix.FIIX_URL.getField())
                    .addConverterFactory(new ResponseConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create());

    private static String requestUrl = Fiix.FIIX_URL.getField() +"/api/?action=FindResponse&appKey="+Fiix.API_key.getField()+"&accessKey="+Fiix.Access_key.getField()+"&signatureMethod=HmacSHA256&signatureVersion=1";

    public static <S> S createService(Class<S> serviceClass) {

        httpClient.interceptors().clear();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);

        HeaderInterceptor headerInterceptor = new HeaderInterceptor();
        QueryInterceptor queryInterceptor = new QueryInterceptor();

        httpClient.addInterceptor(interceptor);
        httpClient.addInterceptor(headerInterceptor);
        httpClient.addInterceptor(queryInterceptor);

        builder.client(httpClient.build());
        retrofit = builder.build();
        return retrofit.create(serviceClass);
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