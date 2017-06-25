package org.tacademy.woof.doguendoguen.app.base.message;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.tacademy.woof.doguendoguen.R;

/**
 * Created by Tak on 2017. 6. 5..
 */

class UserViewHolder extends RecyclerView.ViewHolder {
    TextView userMessage;
    ImageView userImage;

    public UserViewHolder(View view) {
        super(view);

        userMessage = (TextView) view.findViewById(R.id.user_message);
        userImage = (ImageView) view.findViewById(R.id.user_image);
    }
}
