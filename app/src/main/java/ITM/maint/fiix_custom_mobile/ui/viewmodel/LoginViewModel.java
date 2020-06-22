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
import ITM.maint.fiix_custom_mobile.data.repository.UserRepository;

public class LoginViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private IUserService userService;
    private LiveData<List<User>> userResponseLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        userRepository = new UserRepository(this.getApplication());
        userService = userRepository.getPartService();
        userResponseLiveData = userRepository.getPartResponseMutableLiveData();

    }

    public void addUser(User user){
        userRepository.addUser(user);
    }

    public void findUser(String username) {
        userRepository.findUser(username);
    }

    public void dispose(){
        userRepository.dispose();
    }

    public LiveData<List<User>> getUserResponseLiveData() {
        return userResponseLiveData;
    }

}
