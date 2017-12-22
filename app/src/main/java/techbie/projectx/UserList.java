package techbie.projectx;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserList extends AppCompatActivity {
    Context mContext;
    UserDataResponse userDataResponses;
    ArrayList<UserData> userData;
    @BindView(R.id.textView)
    TextView textView;
    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mContext = this;
        ButterKnife.bind(this);
        APIclient.getApi(mContext).callUserList("users", 1).enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.code() == 200) {
                    userDataResponses = response.body();
                    showData();
                } else Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void showData() {
        textView.setText("");
        userData = userDataResponses.getData();
        for (UserData userData1 : userData) {
            textView.append(userData1.getId() + ") " + userData1.getFirstName() + " " + userData1.getLastName() + "\n");
        }

    }
}