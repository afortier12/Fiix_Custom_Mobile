package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private SavedStateHandle stateHandle;
    private static final String USERNAME_KEY = "username";
    private static final String USERID_KEY = "userId";

    public SharedViewModel(SavedStateHandle stateHandle) {
        this.stateHandle = stateHandle;
    }

    public void setUsername(String username) {
        stateHandle.set(USERNAME_KEY, username);
    }

    public void setUserId(int userId) {
        stateHandle.set(USERID_KEY, userId);
    }

    public LiveData<String> getUsername() {
        return stateHandle.getLiveData(USERNAME_KEY);
    }

    public LiveData<Integer> getUserId() {
        return stateHandle.getLiveData(USERID_KEY);
    }

    public SavedStateHandle getStateHandle() {
        return stateHandle;
    }
}
