package ITM.maint.fiix_custom_mobile.utils;

public class Box<T> {

    private T boxContent;

    public Box(T t) {
        this.boxContent = t;
    }

    public T getBoxContent() {
        return boxContent;
    }
}
