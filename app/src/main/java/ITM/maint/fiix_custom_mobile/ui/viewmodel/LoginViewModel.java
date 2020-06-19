package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ITM.maint.fiix_custom_mobile.constants.Users;
import ITM.maint.fiix_custom_mobile.data.api.IUserService;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.User;
import ITM.maint.fiix_custom_mobile.data.repository.remote.UserRepository;

public class LoginViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private IUserService userService;
    private LiveData<List<User>> userResponseLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        userRepository = new UserRepository();
        userService = userRepository.getPartService();
        userResponseLiveData = userRepository.getPartResponseMutableLiveData();

    }

    public void findUser(String username) {

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

        userRepository.findUser(new FindRequest("FindRequest", clientVersion, "User", fields, filters));
    }

    public LiveData<List<User>> getUserResponseLiveData() {
        return userResponseLiveData;
    }

}
