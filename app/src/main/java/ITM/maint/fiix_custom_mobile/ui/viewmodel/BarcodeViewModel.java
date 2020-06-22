package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BarcodeViewModel extends ViewModel {

    private MutableLiveData<String> barcode = new MutableLiveData<String>();

    public MutableLiveData<String> getBarcode(){
        return barcode;
    }
}