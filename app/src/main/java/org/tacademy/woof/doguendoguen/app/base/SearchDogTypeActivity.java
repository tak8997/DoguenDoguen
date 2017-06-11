package org.tacademy.woof.doguendoguen.app.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;

import org.tacademy.woof.doguendoguen.R;

/**
 * Created by Tak on 2017. 6. 9..
 */

public class SearchDogTypeActivity extends AppCompatActivity {
    public static final String DOGTYPE = "dogType";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_search_dog_type);

        final AutoCompleteTextView tv = (AutoCompleteTextView) findViewById(R.id.search_dog_text_view);


        findViewById(R.id.search_finish_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dogType = tv.getText().toString();

                Intent intent = new Intent();
                intent.putExtra(DOGTYPE, dogType);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
