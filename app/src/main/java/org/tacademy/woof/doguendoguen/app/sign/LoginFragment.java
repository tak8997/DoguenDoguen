package org.tacademy.woof.doguendoguen.app.sign;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.rest.RestClient;
import org.tacademy.woof.doguendoguen.rest.user.AuthService;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginFragment extends DialogFragment {
    private static final String TAG = "LoginFragment";

    private static final int NEXT_SIGN_UP = 200;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.LoginDialogTheme);
        if (getArguments() != null) {
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND)
                    logIn();

                return false;
            }
        });
    }

    @OnClick({R.id.log_in, R.id.sign_up})
    public void onSignedClicked(View view) {
        switch (view.getId()) {
            case R.id.log_in:
                logIn();
                break;
            case R.id.sign_up:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);
                SignUpFragment signUpFragment = SignUpFragment.newInstance();
                signUpFragment.setOnAdapterItemClickListener(new SignUpFragment.OnAdapterItemClickListener() {
                    @Override
                    public void onAdapterItemClick(boolean isSuccess) {
                        if(isSuccess) {
                            Log.d("LoginFragment", "popBackStack" + " , " + SharedPreferencesUtil.getInstance().getUserId());
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }
                });
                ft.replace(R.id.container, signUpFragment);
                ft.commit();
                break;
        }
    }

    @BindView(R.id.email) EditText userEmail;
    @BindView(R.id.password) EditText userPwd;
    private void logIn() {
        if (!validateUserAccount()) {
            Log.d("LoginFragment", "Login Failed");
            return;
        } else {
            AuthService userService = RestClient.createService(AuthService.class);
            Observable<JsonObject> getUser = userService.login(email, password);

            getUser.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {
                        String message = jsonObject.get("message").getAsString();
                        String userToken = jsonObject.get("user_token").getAsString();

                        if(message.equals("success")) {
                            //로그인해서 받아온 user token을 저장
                            SharedPreferencesUtil.getInstance().setUserId(userToken);
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }, Throwable::printStackTrace);
        }
    }

    private String email;
    private String password;
    private boolean validateUserAccount() {
        boolean isValid = true;
        email = userEmail.getText().toString();
        password = userPwd.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("올바른 이메일을 입력해주세요.");
            isValid = false;
        } else {
            userEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            userPwd.setError("비밀번호를 다시 입력해주세요.");
            isValid = false;
        } else {
            userPwd.setError(null);
        }

        return isValid;
    }
}
