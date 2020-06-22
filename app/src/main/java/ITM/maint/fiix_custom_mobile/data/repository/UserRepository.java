package ITM.maint.fiix_custom_mobile.data.repository;


import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ITM.maint.fiix_custom_mobile.constants.Users;
import ITM.maint.fiix_custom_mobile.data.api.IUserService;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.UserFindResponse;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.dao.IUserDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.User;
import ITM.maint.fiix_custom_mobile.data.api.ServiceGenerator;
import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;

public class UserRepository extends BaseRepository{

    private static final String TAG ="UserRepository";
    private IUserService userService;

   private MutableLiveData<List<User>> userResponseMutableLiveData;

    private ITM.maint.fiix_custom_mobile.data.model.dao.IPartDao IPartDao;

    public UserRepository(Application application) {
        super(application);
        userResponseMutableLiveData = new MutableLiveData<List<User>>();
        userService = ServiceGenerator.createService(IUserService.class);

        fiixDatabase = FiixDatabase.getDatabase(application);
        IUserDao userDao= fiixDatabase.userDao();
    }

    public void addUser(User user){
        Completable completable = fiixDatabase.userDao().insert(user);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = (new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Log.d(TAG, "User added to DB");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error adding user to DB");

            }
        });
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);

    }

    public void findUser(String username){

        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> assetFields = new ArrayList<>(Arrays.asList(
                Users.id.getField(),
                Users.username.getField(),
                Users.isGroup.getField()
        ));
        String fields = TextUtils.join(",",assetFields);

        FindRequest.FullText_Filter filter = new FindRequest.FullText_Filter(
                Users.username.getField(),
                username
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        requestUserFromFiix(new FindRequest("FindRequest", clientVersion, "User", fields, filters));
    }

    private void requestUserFromFiix(FindRequest userRequest){
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

    public void dispose(){
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }

    public IUserService getPartService() {
        return userService;
    }


}
