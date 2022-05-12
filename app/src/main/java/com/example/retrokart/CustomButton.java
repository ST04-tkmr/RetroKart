package com.example.retrokart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomButton extends androidx.appcompat.widget.AppCompatButton {
    static int[] buttonStates = new int[5];
    int buttonId;

    public CustomButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                buttonStates[buttonId] = 1;
                break;
            case MotionEvent.ACTION_UP:
                buttonStates[buttonId] = 0;
                performClick();
                break;
            default:
                break;
        }
        return true;
    }

    public boolean performClick() {
        super.performClick();
        return true;
    }
}
