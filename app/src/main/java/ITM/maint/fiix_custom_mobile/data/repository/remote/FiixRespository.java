package ITM.maint.fiix_custom_mobile.data.repository.remote;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.util.Hex;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ITM.maint.fiix_custom_mobile.data.api.PartService;
import ITM.maint.fiix_custom_mobile.data.api.requests.PartAddRequest;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FiixRespository {

    private static final String FIIX_URL = "https://integritytool.macmms.com";
    private static final String API_key = "macmmsackp3848fbda83ce8bfff2fe692e700e40d392049fcc1c6928619403d94";
    private static final String Access_key = "macmmsaakp3844fa3e6d75a198199ec20f727518bad4dc4f798531d5427225c";
    private static final String API_secret = "macmmsaskp38410245f872c82bf62de179360f648957ac37ea162a95cecebbe91e94f8ec45084";

    protected Retrofit retrofit;

    public FiixRespository() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("action","AddRequest")
                                .addQueryParameter("appKey", API_key)
                                .addQueryParameter("accessKey",Access_key)
                                .addQueryParameter("signatureMethod", "HmacSHA256")
                                .addQueryParameter("signatureVersion", "1")
                                .build();

                        Request.Builder requestBuilder = original. newBuilder()
                                .url(url);
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(new Interceptor() {
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
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FIIX_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private String authString(){

        String message = null;
        String hmacString = null;

        //Trim off protocol
        if (FIIX_URL.indexOf("http://") == 0)
        {
            message = FIIX_URL.substring("http://".length());
        }
        else if (FIIX_URL.indexOf("https://") == 0)
        {
            message = FIIX_URL.substring("https://".length());
        }
        try {
            //Get Message bytes as UTF-8
            byte[] messageBytes = message.getBytes("UTF-8");
            //Get secret key bytes as UTF-8
            byte[] credentialsSecretKeyBytes = API_secret.getBytes("UTF-8");
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
