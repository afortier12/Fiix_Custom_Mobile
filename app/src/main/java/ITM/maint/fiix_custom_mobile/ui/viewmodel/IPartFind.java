package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;

public interface IPartFind {

    public void findParts(String category, String type, String model);
}
