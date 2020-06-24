package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.IUserService;
import ITM.maint.fiix_custom_mobile.data.model.entity.FiixObject;
import ITM.maint.fiix_custom_mobile.data.model.entity.User;
import ITM.maint.fiix_custom_mobile.data.repository.UserRepository;

public class LoginViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private IUserService userService;
    private MutableLiveData<List<FiixObject>> userResponseLiveData;

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

    public LiveData<List<FiixObject>> getUserResponseLiveData() {
        return userResponseLiveData;
    }

}
