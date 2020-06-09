package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ITM.maint.fiix_custom_mobile.data.repository.remote.PartRepository;

public class BarcodeViewModel extends ViewModel {

    private MutableLiveData<String> barcode = new MutableLiveData<String>();

    public MutableLiveData<String> getBarcode(){
        return barcode;
    }
}