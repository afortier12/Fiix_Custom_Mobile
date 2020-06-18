package ITM.maint.fiix_custom_mobile.constants;

public enum Fiix {

    FIIX_URL("https://integritytool.macmms.com"),
    API_key("macmmsackp3848fbda83ce8bfff2fe692e700e40d392049fcc1c6928619403d94"),
    Access_key("macmmsaakp3844fa3e6d75a198199ec20f727518bad4dc4f798531d5427225c"),
    API_secret ("macmmsaskp38410245f872c82bf62de179360f648957ac37ea162a95cecebbe91e94f8ec45084");

    private final String field;

    private Fiix(String field) {
        this.field = field;
    }

    public String getField(){
        return field;
    }
}
