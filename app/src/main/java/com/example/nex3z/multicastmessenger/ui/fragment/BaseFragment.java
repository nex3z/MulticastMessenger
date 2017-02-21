package com.example.nex3z.multicastmessenger.ui.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.example.nex3z.multicastmessenger.internal.dagger.HasComponent;

public abstract class BaseFragment extends Fragment {

    private boolean mIsInjected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        try {
            mIsInjected = onInjectView();
        } catch (IllegalStateException e) {
            Log.e(e.getClass().getSimpleName(), e.getMessage());
            mIsInjected = false;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsInjected) onViewInjected(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mIsInjected) {
            mIsInjected = onInjectView();
            if (mIsInjected) {
                onViewInjected(savedInstanceState);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) throws IllegalStateException {
        C component = componentType.cast(((HasComponent<C>) getActivity()).getComponent());
        if (component == null) {
            throw new IllegalStateException(componentType.getSimpleName()
                    + " has not been initialized yet.");
        }
        return component;
    }

    protected boolean onInjectView() throws IllegalStateException {
        return false;
    }

    @CallSuper
    protected void onViewInjected(Bundle savedInstanceState) {}
}