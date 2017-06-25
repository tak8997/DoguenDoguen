package org.tacademy.woof.doguendoguen.app.base.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tacademy.woof.doguendoguen.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class QuestionFragment extends Fragment {



    public QuestionFragment() {
    }
    public static QuestionFragment newInstance() {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @BindView(R.id.first_answer) TextView firAnswer;
    @BindView(R.id.second_answer) TextView secAnswer;
    @BindView(R.id.third_answer) TextView thirdAnswer;
    @BindView(R.id.fourth_answer) TextView fourAnswer;
    @BindView(R.id.fifth_answer) TextView fifAnswer;

    boolean firstClicked = false;
    boolean secondClicked = false;
    boolean thirdClicked = false;
    boolean fourthClicked = false;
    boolean fifthClicked = false;

    @OnClick({R.id.first_question, R.id.second_question, R.id.third_question, R.id.fourth_question, R.id.fifth_question})
    public void onQuestionClicked(View view ) {
        switch (view.getId()) {
            case R.id.first_question:
                if(firstClicked == false) {
                    firAnswer.setVisibility(View.VISIBLE);
                    firstClicked = true;
                } else {
                    firAnswer.setVisibility(View.GONE);
                    firstClicked = false;
                }
                break;
            case R.id.second_question:
                if(secondClicked == false) {
                    secAnswer.setVisibility(View.VISIBLE);
                    secondClicked = true;
                } else {
                    secAnswer.setVisibility(View.GONE);
                    secondClicked = false;
                }
                break;
            case R.id.third_question:
                if(thirdClicked == false) {
                    thirdAnswer.setVisibility(View.VISIBLE);
                    thirdClicked = true;
                } else {
                    thirdAnswer.setVisibility(View.GONE);
                    thirdClicked = false;
                }
                break;
            case R.id.fourth_question:
                if(fourthClicked == false) {
                    fourAnswer.setVisibility(View.VISIBLE);
                    fourthClicked = true;
                } else {
                    fourAnswer.setVisibility(View.GONE);
                    fourthClicked = false;
                }
                break;
            case R.id.fifth_question:
                if(fifthClicked == false) {
                    fifAnswer.setVisibility(View.VISIBLE);
                    fifthClicked = true;
                } else {
                    fifAnswer.setVisibility(View.GONE);
                    fifthClicked = false;
                }
                break;
        }
    }

    @OnClick(R.id.back)
    public void onBackClicked() {
        getFragmentManager().popBackStack();
    }
}
