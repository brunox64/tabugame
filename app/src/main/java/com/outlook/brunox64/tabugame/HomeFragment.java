package com.outlook.brunox64.tabugame;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bruno on 05/02/2017.
 */
public class HomeFragment extends Fragment {

    public interface ViewCreatedListener {
        void onViewCreated();
    }

    private ViewCreatedListener mViewCreatedListener;

    public void setOnViewCreatedListener(ViewCreatedListener viewCreatedListener) {
        mViewCreatedListener = viewCreatedListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DebugUtils.info(this, "onCreateView");
        return inflater.inflate(R.layout.view_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mViewCreatedListener != null) mViewCreatedListener.onViewCreated();;
    }

}
