package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import ITM.maint.fiix_custom_mobile.data.model.entity.Storage;

public interface IPartAdd {

    public void findPart(String barcode);

    public void findLocation(int partId);

    public void addToStorage(int partId, int qty, Storage storage);

    public void checkStorage(int partId, Storage storage);


}
