package ITM.maint.fiix_custom_mobile.data.repository;

import ITM.maint.fiix_custom_mobile.data.api.requests.PartRequest;

public interface IPartRepository {

    public interface remote {

        //retrieve parts from Fiix api
        public void findParts(PartRequest partRequest);
        //add part to Fiix
        public void addPart(PartRequest partRequest);
        //change part in Fiix
        public void changePart(PartRequest partRequest);
        //remove part from Fiix
        public void removePart(PartRequest partRequest);
    }

}
