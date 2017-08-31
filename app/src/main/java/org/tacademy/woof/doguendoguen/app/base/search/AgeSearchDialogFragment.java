package org.tacademy.woof.doguendoguen.app.base.search;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

import static org.tacademy.woof.doguendoguen.R.id.under_twelve;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgeSearchDialogFragment extends DialogFragment {


    public AgeSearchDialogFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialogTheme);
    }

    @BindView(R.id.under_four) TextView ageUnderFor;
    @BindView(R.id.under_twelve) TextView ageUnderTwelve;
    @BindView(R.id.up_twelve) TextView ageUpTwelve;
    @BindView(R.id.all_age) TextView ageAll;
    private RxEventBus bus;
    public static Events.AgeMsgEvents ageMsgEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_age_search_dialog, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.dog_age)
    public void onAgeTitleClicked() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        bus = RxEventBus.getInstance();
    }

    @OnClick({R.id.under_four, R.id.under_twelve, R.id.up_twelve, R.id.all_age})
    public void onAgeClicked(View view) {
        switch (view.getId()) {
            case R.id.under_four:
                sendEventMsg(ageUnderFor.getText().toString());
                break;
            case under_twelve:
                sendEventMsg(ageUnderTwelve.getText().toString());
                break;
            case R.id.up_twelve:
                sendEventMsg(ageUpTwelve.getText().toString());
                break;
            case R.id.all_age:
                sendEventMsg(ageAll.getText().toString());
                break;
        }
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    private void sendEventMsg(String eventMsg) {
        ageMsgEvents = new Events.AgeMsgEvents(eventMsg);
        bus.send(ageMsgEvents);
    }
}
