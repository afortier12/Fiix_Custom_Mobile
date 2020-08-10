package ITM.maint.fiix_custom_mobile.data.api;

import android.util.Log;

import com.google.android.gms.common.util.Hex;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
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
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

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

            String jsonString = response.body().string();

            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
                return response;
            }

            StringReader stringReader = new StringReader(jsonString);
            JsonReader jsonReader = new JsonReader(stringReader);

            JsonToken jsonToken;
            JSONArray jsonArray = new JSONArray();
            try {
                while ((jsonToken = jsonReader.peek()) != JsonToken.END_DOCUMENT) {
                    switch (jsonToken) {
                        case BEGIN_OBJECT:
                            jsonReader.beginObject();
                            continue;
                        case END_OBJECT:
                            jsonReader.endObject();
                            continue;
                        case BEGIN_ARRAY:
                            jsonReader.beginArray();
                            continue;
                        case END_ARRAY:
                            jsonReader.endArray();
                            continue;
                        case NAME:
                            String name = jsonReader.nextName();
                            if (name.equalsIgnoreCase("error")) {
                                JSONObject errObj = jsonObject.getJSONObject("error");
                                String leg = errObj.getString("leg");
                                String code = errObj.getString("code");
                                String msg = errObj.getString("message");
                                String stack = errObj.getString("stackTrace");
                                errObj = new JSONObject();
                                errObj.put("leg", leg);
                                errObj.put("code", code);
                                errObj.put("message", msg);
                                errObj.put("stackTrace", stack);
                                jsonArray.put(errObj);

                            } else if (name.equalsIgnoreCase("objects")) {
                                break;
                            }
                            System.out.println("Token Value >>>> " + name);
                            continue;
                        case STRING:
                            String value = jsonReader.nextString();
                            System.out.println("Token Value >>>> " + value);
                            continue;
                        case NUMBER:
                            long lValue = jsonReader.nextLong();
                            System.out.println("Token Value >>>> " + lValue);
                            continue;
                        case NULL:
                            jsonReader.nextNull();
                            System.out.println("Token Value >>>> null");
                            continue;
                        case BOOLEAN:
                            Boolean bool = jsonReader.nextBoolean();
                            System.out.println("Token Value >>>> " + bool);
                            continue;
                        default:
                            throw new AssertionError("default");
                    }
                    break;
                }

            } catch (IOException | AssertionError | JSONException e) {
                e.printStackTrace();
            } finally {
                jsonReader.close();
            }

            Response.Builder builder = response.newBuilder();
            if (jsonArray.length() > 0) {
                jsonObject.remove("error");
                try {
                    jsonObject.put("error", jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return response;
                }
                builder.code(400);
            } else {
                builder.code(response.code());
            }
            ResponseBody body = ResponseBody.create(jsonObject.toString(), response.body().contentType());
            return builder.body(body)
                    .build();

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
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
                e.printStackTrace();
            }

            return hmacString;
        }

    }


}