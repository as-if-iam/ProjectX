package techbie.projectx;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserList extends AppCompatActivity {
    Context mContext;
    UserDataResponse userDataResponses;
    ArrayList<UserData> userData = new ArrayList<>();
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.rv_main)
    RecyclerView recyclerView;
    int MIN = 1, MAX = 12;
    private String TAG = this.getClass().getSimpleName();
    private ConnectivityManager connectivityManager;
    private boolean connected;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                System.exit(0);
                break;
            case R.id.refresh:
                MIN = 1;
                Toast.makeText(mContext, "Refresh", Toast.LENGTH_SHORT).show();
                makeServiceCall(1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        mContext = this;
        ButterKnife.bind(this);
        if (isOnline())
            makeServiceCall(1);
        else noInternetDialog();

    }

    private void noInternetDialog() {
        new MaterialDialog.Builder(this)
                .title("No Internet")
                .content("Please Connect to Internet to load data")
                .positiveText("Retry")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        makeServiceCall(1);
                    }
                })
                .negativeText("Exit")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(mContext, "Thank you", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        System.exit(0);
                    }
                })
                .show();
    }

    private void makeServiceCall(int page) {
        if (!isOnline()) {
            noInternetDialog();
            return;
        }
        APIclient.getApi(mContext).callUserList("users", page).enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.code() == 200) {
                    userDataResponses = response.body();
                    assert userDataResponses != null;
                    if (userDataResponses.getData().size() > 0) {
                        userData.addAll(userDataResponses.getData());
                        showData();
                    }
                    if (MIN <= MAX)
                        makeServiceCall(++MIN);
                } else Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void showData() {
        textView.setText("");

//        userData = userDataResponses.getData();
//        for (UserData userData1 : userData) {
//            textView.append(userData1.getId() + ") " + userData1.getFirstName() + " " + userData1.getLastName() + "\n");
//            Picasso.with(mContext).load(userData1.getAvatar()).into(imageView);
//        }
        UserAdapter userAdapter = new UserAdapter(userData, mContext);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));


    }


    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }
}
