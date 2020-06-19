package ITM.maint.fiix_custom_mobile.data.repository.remote;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.List;

import ITM.maint.fiix_custom_mobile.constants.Fiix;
import ITM.maint.fiix_custom_mobile.data.api.IUserService;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.PartFindResponse;
import ITM.maint.fiix_custom_mobile.data.api.responses.UserFindResponse;
import ITM.maint.fiix_custom_mobile.data.model.entity.User;
import ITM.maint.fiix_custom_mobile.utils.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;

public class UserRepository {

    private static final String TAG ="UserRepository";
    private IUserService userService;

    private String requestUrl = Fiix.FIIX_URL.getField() +"/api/?action=FindResponse&appKey="+Fiix.API_key.getField()+"&accessKey="+Fiix.Access_key.getField()+"&signatureMethod=HmacSHA256&signatureVersion=1";
    private MutableLiveData<List<User>> userResponseMutableLiveData;


    public UserRepository() {

        userResponseMutableLiveData = new MutableLiveData<List<User>>();


        userService = ServiceGenerator.createService(IUserService.class);
    }

    public void findUser(FindRequest userRequest){

        userService.findUser(userRequest)
                .enqueue(new Callback<UserFindResponse>() {

                    @Override
                    public void onResponse(Call<UserFindResponse> call, retrofit2.Response<UserFindResponse> response) {
                        if (response.body() != null){
                            userResponseMutableLiveData.postValue(response.body().getObjects());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserFindResponse> call, Throwable t) {
                        userResponseMutableLiveData.postValue(null);

                        if (t instanceof IOException) {
                            Log.d(TAG, "this is an actual network failure: " + t.getMessage());
                            // logging probably not necessary
                        }
                        else {
                            Log.d(TAG, "conversion issue! big problems: " + t.getMessage());
                        }
                    }
                });
    }

    public MutableLiveData<List<User>> getPartResponseMutableLiveData() {
        return userResponseMutableLiveData;
    }

    public IUserService getPartService() {
        return userService;
    }


}
