package org.tacademy.woof.doguendoguen.app.base.message;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.tacademy.woof.doguendoguen.R;

/**
 * Created by Tak on 2017. 6. 5..
 */

class OtherUserViewHolder extends RecyclerView.ViewHolder {
    TextView otherUserMessage;
    TextView otherUserName;
    ImageView otherUserImage;

    public OtherUserViewHolder(View view) {
        super(view);

        otherUserMessage = (TextView) view.findViewById(R.id.other_user_message);
        otherUserName = (TextView) view.findViewById(R.id.other_user_name);
        otherUserImage = (ImageView) view.findViewById(R.id.other_user_image);
    }
}
