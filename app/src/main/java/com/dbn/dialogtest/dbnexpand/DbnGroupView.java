package com.dbn.dialogtest.dbnexpand;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/5/16.
 */

public class DbnGroupView extends LinearLayout {

    public DbnGroupView(Context context) {
        super(context);
        init(context);
    }

    public DbnGroupView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DbnGroupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
    }


}
