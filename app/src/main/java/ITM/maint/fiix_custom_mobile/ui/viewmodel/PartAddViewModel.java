package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ITM.maint.fiix_custom_mobile.data.repository.remote.PartRepository;

public class PartAddViewModel extends ViewModel {

    private String barcodeValue;
    private MutableLiveData<String> barcode = new MutableLiveData<String>();
    private PartRepository repository = new PartRepository();

    public MutableLiveData<String> getBarcode(){
        return barcode;
    }

    public void setBarcodeValue(String barcodeValue) {
        this.barcodeValue = barcodeValue;
    }
}
