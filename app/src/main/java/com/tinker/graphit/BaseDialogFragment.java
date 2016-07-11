package com.tinker.graphit;

import android.app.Activity;
import android.app.DialogFragment;

/**
 * Created by sonny on 2016/07/10.
 */
public abstract class BaseDialogFragment<T> extends DialogFragment {
    private T mActivityInstance;

    public final T getActivityInstance() {
        return mActivityInstance;
    }

    @Override
    public void onAttach(Activity activity) {
        mActivityInstance = (T) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityInstance = null;
    }
}









