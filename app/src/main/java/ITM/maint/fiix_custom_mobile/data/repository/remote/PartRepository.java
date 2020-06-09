package ITM.maint.fiix_custom_mobile.data.repository.remote;


import ITM.maint.fiix_custom_mobile.data.api.PartService;

public class PartRepository extends FiixRespository {

    private PartService partService;

    public PartRepository() {
        super();
        partService = super.retrofit.create(PartService.class);
    }

    public PartService getPartService() {
        return partService;
    }
}
