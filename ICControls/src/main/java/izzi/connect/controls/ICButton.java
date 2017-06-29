package izzi.connect.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by cevelez on 20/11/2015.
 */
public class ICButton extends Button {
    public ICButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundResource(R.drawable.ic_button);
    }
}
