package ITM.maint.fiix_custom_mobile.data.repository;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;

public interface IPartRepository {

    //retrieve parts from Fiix api
    public void findParts(FindRequest partRequest);
    //add part to Fiix
    public void addPart(Part part);
    //change part in Fiix
    public void changePart(FindRequest partRequest);
    //remove part from Fiix
    public void removePart(FindRequest partRequest);


}
