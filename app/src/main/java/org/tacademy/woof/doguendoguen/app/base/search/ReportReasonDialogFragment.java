package org.tacademy.woof.doguendoguen.app.base.search;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tacademy.woof.doguendoguen.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportReasonDialogFragment extends DialogFragment {


    public ReportReasonDialogFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.LoginDialogTheme);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_reason_dialog, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.yes, R.id.no})
    public void onAnswerClicked(View view) {
        switch (view.getId()) {
            case R.id.yes:
                if(listener != null)
                    listener.onAdapterItemClick("yes");
                break;
            case R.id.no:
                if(listener != null)
                    listener.onAdapterItemClick("no");
                break;
        }
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public interface OnAdapterItemClickLIstener {
        public void onAdapterItemClick(String answer);
    }

    OnAdapterItemClickLIstener listener;
    public void setOnAdapterItemClickListener(OnAdapterItemClickLIstener listener) {
        this.listener = listener;
    }

}
