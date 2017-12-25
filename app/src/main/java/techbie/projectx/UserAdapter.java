package techbie.projectx;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by asif on 25/12/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<UserData> userData = new ArrayList<>();
    private Context mContext;

    UserAdapter(ArrayList<UserData> userData, Context mContext) {
        this.userData = userData;
        this.mContext = mContext;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        UserData data = userData.get(position);
        holder.textView.setText(data.getFirstName());
        Picasso.with(mContext).load(data.getAvatar()).into(holder.imageView);
    }

    @Override
    public long getItemId(int i) {
        return userData.get(i).getId();
    }

    @Override
    public int getItemCount() {
        return userData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;

        TextView textView;

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.ll_main_item);
            textView = itemView.findViewById(R.id.tv_item);
            imageView = itemView.findViewById(R.id.iv_item);
        }
    }
}
