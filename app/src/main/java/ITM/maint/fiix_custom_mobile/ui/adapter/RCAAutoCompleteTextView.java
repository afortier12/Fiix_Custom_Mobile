package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.icu.util.Measure;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RCAAutoCompleteTextView extends androidx.appcompat.widget.AppCompatAutoCompleteTextView {

    private boolean showAlways;

    public RCAAutoCompleteTextView(Context context) {
        super(context);
    }

    public RCAAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RCAAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setShowAlways(boolean showAlways) {
        this.showAlways = showAlways;
    }

    @Override
    public boolean enoughToFilter() {
        return showAlways || super.enoughToFilter();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        //showDropDownIfFocused();
    }

    private void showDropDownIfFocused() {
        if (enoughToFilter() && isFocused() && getWindowVisibility() == View.VISIBLE) {

            showDropDown();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        //showDropDownIfFocused();
    }

    @Override
    public void showDropDown() {

        super.showDropDown();
    }
}