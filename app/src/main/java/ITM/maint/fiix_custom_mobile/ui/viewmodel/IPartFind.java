package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import ITM.maint.fiix_custom_mobile.data.api.requests.PartRequest;

public interface IPartFind {

    public void findParts(PartRequest.Filter filter);
}
