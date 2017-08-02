package org.tacademy.woof.doguendoguen.app.sign;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.tacademy.woof.doguendoguen.DoguenDoguenApplication;
import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.rest.RestClient;
import org.tacademy.woof.doguendoguen.rest.user.AuthService;
import org.tacademy.woof.doguendoguen.util.SharedPreferencesUtil;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SignUpFragment extends DialogFragment {
    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userPwdCheck.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND)
                    signUp();

                return false;
            }
        });
    }

    @OnClick({R.id.regist_finish, R.id.cancel})
    public void onRegistClicked(View view) {
        switch (view.getId()) {
            case R.id.regist_finish:
                signUp();
                break;
            case R.id.cancel:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    private void signUp() {
        if (!validateUserAccount()) {
            Log.d("LoginFragment", "Login Failed");
            return;
        } else {
            progressBar.setVisibility(View.VISIBLE);

            AuthService userService = RestClient.createService(AuthService.class);
            Observable<JsonObject> signUpUser = userService.signUp(email, password, pwdCheck);

            signUpUser.debounce(2000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(jsonObject -> {
                        String message = jsonObject.get("message").getAsString();
                        String userToken = jsonObject.get("user_token").getAsString();

                        Log.d("SignUpFragment", userToken);
                        if(message.equals("success")) {
                            //로그인해서 받아온 user token을 저장
                            SharedPreferencesUtil.getInstance().setUserId(userToken);
                            if(listener != null) {
                                getActivity().getSupportFragmentManager().popBackStack();
                                listener.onAdapterItemClick(true);
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                    }, throwable -> {
                        if(throwable instanceof HttpException) {
                            HttpException exception = (HttpException) throwable;
                            Log.d("SignUpFragment", exception.getMessage());
                            if(exception.response().code() == 405) {
                                Log.d("SignUpFragment", exception.getMessage());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DoguenDoguenApplication.getContext(), "이미 가입되어 있는 아이디입니다.", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    });
        }
    }

    @BindView(R.id.e_mail) EditText userEmail;
    @BindView(R.id.pwd) EditText userPwd;
    @BindView(R.id.pwd_check) EditText userPwdCheck;

    private String email;
    private String password;
    private String pwdCheck;
    private boolean validateUserAccount() {
        boolean isValid = true;
        email = userEmail.getText().toString().trim();
        password = userPwd.getText().toString().trim();
        pwdCheck = userPwdCheck.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("올바른 이메일을 입력해주세요.");
            isValid = false;
        } else
            userEmail.setError(null);

        if (password.isEmpty() || password.length() < 6 || password.length() > 16) {
            userPwd.setError("6~16글자 사이의 비밀번호를 입력해주세요.");
            isValid = false;
        } else if (isValidPassword(password)) {
            userPwd.setError("비빌번호에 숫자, 비밀번호, 특수문자가 포함되어야 합니다.");
            isValid = false;
        } else if(!password.equals(pwdCheck)) {
            userPwdCheck.setError("비빌번호와 비밀번호 확인이 일치하지 않습니다.");
            isValid = false;
        } else
            userPwd.setError(null);

        return isValid;
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public interface OnAdapterItemClickListener {
        public void onAdapterItemClick(boolean isSuccess);
    }

    OnAdapterItemClickListener listener;
    public void setOnAdapterItemClickListener(OnAdapterItemClickListener listener) {
        this.listener = listener;
    }
}










