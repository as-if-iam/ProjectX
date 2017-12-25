package techbie.projectx.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import techbie.projectx.R;
import techbie.projectx.pojo.UserData;

public class DetailUserView extends AppCompatActivity {
    @BindView(R.id.iv_user)
    ImageView imageView;
    @BindView(R.id.tv_userId)
    TextView tvUserId;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.tv_userLastName)
    TextView tvLastName;
    @BindView(R.id.tv_userAvatarLink)
    TextView tvLink;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user_view);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ButterKnife.bind(this);
        UserData data = (UserData) getIntent().getSerializableExtra("user");
        setUserData(data);
    }

    private void setUserData(UserData data) {
        setTitle(data.getFirstName());
        Picasso.with(this).load(data.getAvatar()).into(imageView);
        tvUserId.setText(data.getId().toString());
        tvUserName.setText(data.getFirstName());
        tvLastName.setText(data.getLastName());
        tvLink.setText(data.getAvatar());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
