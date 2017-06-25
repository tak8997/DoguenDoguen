package org.tacademy.woof.doguendoguen.app.base.profile;


import android.os.Bundle;
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
public class AgreementFragment extends Fragment {


    public AgreementFragment() {
    }
    public static Fragment newInstance() {
        AgreementFragment fragment = new AgreementFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agreement_transaction, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @OnClick(R.id.back)
    public void onBackClicked() {
        getFragmentManager().popBackStack();
    }

}
