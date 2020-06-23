package ITM.maint.fiix_custom_mobile.data.repository;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;

public interface IWorkOrderRepository {

    //retrieve parts from Fiix api
    public void findParts(FindRequest partRequest);
    //add part to Fiix
    public void addPart(FindRequest partRequest);
    //change part in Fiix
    public void changePart(FindRequest partRequest);
    //remove part from Fiix
    public void removePart(FindRequest partRequest);


}
