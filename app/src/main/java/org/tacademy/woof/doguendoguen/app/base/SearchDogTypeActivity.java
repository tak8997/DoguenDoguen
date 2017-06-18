package org.tacademy.woof.doguendoguen.app.base;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.util.SoundSearchUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.media.CamcorderProfile.get;

/**
 * Created by Tak on 2017. 6. 9..
 */

public class SearchDogTypeActivity extends ListActivity {
    public static final String DOGTYPE = "dogType";

    @BindView(R.id.search_dog_types) EditText searchKey;
    @BindView(android.R.id.list) ListView dogTypeItem;

    ArrayList<String> searchList = new ArrayList<String>();
    ArrayList<String> newSearchList = new ArrayList<String>();
    MenuAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_search_dog_type);
        ButterKnife.bind(this);

        addDogTypeList();//검색리스트를 애드 해준다

        searchKey.addTextChangedListener(textWatcherInput);//텍스트가 변할때 이벤트

        mAdapter = new MenuAdapter(SearchDogTypeActivity.this, R.layout.search_list, searchList);
        setListAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void addDogTypeList() {
        searchList.add("골든리트리버");searchList.add("그레이하운드");searchList.add("그레이트덴");searchList.add("꼬통드뚤레아");

        searchList.add("닥스훈트");searchList.add("달마시안");searchList.add("도고 까나리오");searchList.add("도베르만");

        searchList.add("래브라도리 트리버");searchList.add("라이카");searchList.add("롯트와일러");

        searchList.add("마스티프");searchList.add("말라뮤트");searchList.add("말티즈");searchList.add("믹스견");

        searchList.add("삽살개");searchList.add("샤페");searchList.add("세인트버나");searchList.add("스피");

        searchList.add("아메리칸불독");searchList.add("아키다");searchList.add("요크셰테리어");
    }

    @OnClick({R.id.exit})
    public void onExitClicked() {
        String dogType = searchKey.getText().toString();
        finish();
    }

    TextWatcher textWatcherInput = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            dogTypeItem.setVisibility(View.VISIBLE);
            if(s.length()==0){//텍스트가 다 지워질때는 전체 목록을 보여준다

                mAdapter = new MenuAdapter(SearchDogTypeActivity.this, R.layout.search_list, searchList);
                setListAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }else{
                newSearchList.clear();
                for(int i=0; i<searchList.size() ; i++){
                    String searchData = searchList.get(i).toString();
                    String keyWord= s.toString();

                    boolean isData = SoundSearchUtil.matchString(searchData,keyWord); //검색할 대상 , 검색 키워드로  검색키워드가 검색대상에 있으면  true를 리턴해준다

                    if(isData){
                        newSearchList.add(searchData);//검색대상에 있으면 새로운 리스트를 만들어서 이름을 애드해준다
                    }
                }

                mAdapter = new MenuAdapter(SearchDogTypeActivity.this, R.layout.search_list, newSearchList);
                setListAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();//검색이 끝나면 새로운 리스트로 리스트뷰를 갱신해준다
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public class MenuAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;
        String fInfo;


        public MenuAdapter(Context context, int textViewResourseId, ArrayList<String> items) {
            super(context, textViewResourseId, items);
            this.items = items;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.search_list, null);
            }

            fInfo = items.get(position);

            //	((LinearLayout) v.findViewById(R.id.com_rowid)).setOnClickListener(ListViewCLICKed);
            ((TextView) v.findViewById(R.id.name)).setText(fInfo.toString());
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dogType = items.get(position);
                    Toast.makeText(SearchDogTypeActivity.this, dogType + " 를 선택하셨습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra(DOGTYPE, dogType);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

            return v;

        }

    }
}
