package org.tacademy.woof.doguendoguen.app.base.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.rxbus.Events;
import org.tacademy.woof.doguendoguen.app.rxbus.RxEventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tak on 2017. 6. 12..
 */

public class GenderSearchDialogFragment extends DialogFragment {

    @BindView(R.id.female) TextView femmale;
    @BindView(R.id.male) TextView male;
    @BindView(R.id.any_gender) TextView anyGender;

    private RxEventBus bus;
    public static Events.GenderMsgEvents genderMsgEvents;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_gender_search_dialog, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        bus = RxEventBus.getInstance();
    }

    @OnClick(R.id.dog_gender)
    public void onGenderTitleClicked() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    @OnClick({R.id.female, R.id.male, R.id.any_gender})
    public void onGenderClicked(View view) {
        switch (view.getId()) {
            case R.id.female:
                sendEventMsg(femmale.getText().toString());
                break;
            case R.id.male:
                sendEventMsg(male.getText().toString());
                break;
            case R.id.any_gender:
                sendEventMsg(anyGender.getText().toString());
                break;
        }
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    private void sendEventMsg(String eventMsg) {
        genderMsgEvents = new Events.GenderMsgEvents(eventMsg);
        bus.send(eventMsg);
    }
}
