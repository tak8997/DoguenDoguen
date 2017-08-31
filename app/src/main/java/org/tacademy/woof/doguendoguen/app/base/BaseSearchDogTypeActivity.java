package org.tacademy.woof.doguendoguen.app.base;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.tacademy.woof.doguendoguen.R;
import org.tacademy.woof.doguendoguen.app.rxbus.Events;
import org.tacademy.woof.doguendoguen.app.rxbus.RxEventBus;
import org.tacademy.woof.doguendoguen.util.SoundSearchUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tak on 2017. 7. 3..
 */
public class BaseSearchDogTypeActivity extends BaseActivity {
    public static final String DOGTYPE = "dogType";

    @BindView(R.id.search_dog_types) EditText searchKey;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private List<String> searchList;
    private ArrayList<String> newSearchList = new ArrayList<String>();
    private DogTypeAdapter adapter;
    public static Events.TypeMsgEvents typeMsgEvents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_search_dog_type);
        ButterKnife.bind(this);

        addDogTypeList();//검색리스트를 애드 해준다

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new DogTypeAdapter());
    }

    public List<String> searchDogType(String query) {
        newSearchList.clear();
        for(int i=0; i<searchList.size() ; i++){
            String searchData = searchList.get(i).toString();
            String keyWord= query.toString();

            boolean isData = SoundSearchUtil.matchString(searchData,keyWord); //검색할 대상 , 검색 키워드로  검색키워드가 검색대상에 있으면  true를 리턴해준다

            if(isData){
                newSearchList.add(searchData);//검색대상에 있으면 새로운 리스트를 만들어서 이름을 애드해준다
            }
        }

        return newSearchList;
    }

    public void showResult(List<String> result) {
        if(result.isEmpty())
            adapter.addDogType(Collections.<String>emptyList());
        else
            adapter.addDogType(result);
    }

    private void addDogTypeList() {
        searchList = Arrays.asList(getResources().getStringArray(R.array.dog_types));
    }

    @OnClick({R.id.exit})
    public void onExitClicked() {
        finish();
    }

    private class DogTypeAdapter extends RecyclerView.Adapter<DogTypeAdapter.ViewHolder> {
        private List<String> dogTypes;

        @Override
        public DogTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.search_list, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DogTypeAdapter.ViewHolder holder, int position) {
            holder.type.setText(dogTypes.get(position));
            holder.type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dogType = dogTypes.get(position);
                    Toast.makeText(BaseSearchDogTypeActivity.this, dogType + " 를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    RxEventBus.getInstance().send(new Events.TypeMsgEvents(dogType));
                }
            });
        }

        @Override
        public int getItemCount() {
            return dogTypes == null ? 0 : dogTypes.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView type;

            public ViewHolder(View itemView) {
                super(itemView);

                type = (TextView) itemView.findViewById(R.id.name);
            }
        }

        public void addDogType(List<String> result) {
            this.dogTypes = result;
            this.notifyDataSetChanged();
        }
    }
}
