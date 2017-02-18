package com.example.nex3z.multicastmessenger.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.nex3z.multicastmessenger.executor.JobExecutor;
import com.example.nex3z.multicastmessenger.interactor.ReceiveMessage;
import com.example.nex3z.multicastmessenger.interactor.SendMessage;
import com.example.nex3z.multicastmessenger.model.MessageModel;
import com.example.nex3z.multicastmessenger.presenter.MessengerPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainFragment extends Fragment implements MessengerView {
    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    @BindView(R.id.rv_messages) RecyclerView mRvList;
    @BindView(R.id.et_input) EditText mEtInput;

    private MessageAdapter mAdapter;
    private MessengerPresenter mPresenter;
    private Unbinder mUnbinder;

    public MainFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
        mUnbinder.unbind();
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

    @OnClick(R.id.btn_send)
    void onSendClick() {
        mPresenter.send(mEtInput.getText().toString());
    }

    private void initPresenter() {
        UIThread uiThread = new UIThread();
        mPresenter = new MessengerPresenter(new SendMessage(new JobExecutor(), uiThread), new ReceiveMessage(new JobExecutor(), uiThread));
        mPresenter.setView(this);
        mPresenter.initialize();
    }

    private void initRecyclerView() {
        mAdapter = new MessageAdapter(getContext());
        mRvList.setAdapter(mAdapter);
        mRvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvList.setItemAnimator(null);
        mRvList.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }
}
