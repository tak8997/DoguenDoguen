package org.tacademy.woof.doguendoguen.app.base.message;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.tacademy.woof.doguendoguen.R;

/**
 * Created by Tak on 2017. 6. 5..
 */

class OtherUserViewHolder extends RecyclerView.ViewHolder {
    TextView otherUser;

    public OtherUserViewHolder(View view) {
        super(view);

        otherUser = (TextView) view.findViewById(R.id.other_user);
    }
}
