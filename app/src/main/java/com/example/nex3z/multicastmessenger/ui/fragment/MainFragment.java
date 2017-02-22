package com.example.nex3z.multicastmessenger.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nex3z.multicastmessenger.R;
import com.example.nex3z.multicastmessenger.internal.dagger.component.MessengerComponent;
import com.example.nex3z.multicastmessenger.model.MessageModel;
import com.example.nex3z.multicastmessenger.presenter.MessengerPresenter;
import com.example.nex3z.multicastmessenger.ui.MessengerView;
import com.example.nex3z.multicastmessenger.ui.adapter.MessageAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainFragment extends BaseFragment implements MessengerView {
    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    @BindView(R.id.rv_messages) RecyclerView mRvList;
    @BindView(R.id.et_input) EditText mEtInput;

    @Inject MessengerPresenter mPresenter;
    @Inject MessageAdapter mAdapter;
    private Unbinder mUnbinder;

    public MainFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected boolean onInjectView() throws IllegalStateException {
        getComponent(MessengerComponent.class).inject(this);
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        initRecyclerView();
        initPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear) {
            mPresenter.clear();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void renderMessageModelAt(int position) {
        mAdapter.notifyItemInserted(position);
        mRvList.scrollToPosition(position);
    }

    @Override
    public void renderMessageModelList(List<MessageModel> messages) {
        mAdapter.setMessageCollection(messages);
    }

    @Override public Context context() {
        return getActivity().getApplicationContext();
    }

    @OnClick(R.id.btn_send)
    void onSendClick() {
        mPresenter.send(mEtInput.getText().toString());
    }

    private void initPresenter() {
        mPresenter.setView(this);
        mPresenter.initialize();
    }

    private void initRecyclerView() {
        mRvList.setAdapter(mAdapter);
        mRvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvList.setItemAnimator(null);
        mRvList.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }
}
