package org.tacademy.woof.doguendoguen.app.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.tacademy.woof.doguendoguen.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tak on 2017. 6. 4..
 */

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.next_btn) Button nextBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignUpEndActivity.class);
                startActivity(intent);
            }
        });
    }
}















