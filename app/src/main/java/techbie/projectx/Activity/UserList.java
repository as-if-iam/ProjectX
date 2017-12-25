package techbie.projectx.Activity;

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
import techbie.projectx.APIclient;
import techbie.projectx.PaginationScrollListener;
import techbie.projectx.R;
import techbie.projectx.adapter.UserAdapter;
import techbie.projectx.pojo.UserData;
import techbie.projectx.pojo.UserDataResponse;

public class UserList extends AppCompatActivity {
    public LinearLayoutManager linearLayoutManager;
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
    boolean loading = false;
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
                userData = new ArrayList<>();
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
        linearLayoutManager = new LinearLayoutManager(mContext);
        ButterKnife.bind(this);
        if (isOnline())
            makeServiceCall(MIN);
        else noInternetDialog();

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.d(TAG, "loadMoreItems: " + MIN);
                makeServiceCall(MIN);
            }

            @Override
            public int getTotalPageCount() {
                return 0;
            }

            @Override
            public boolean isLastPage() {
                return MIN >= 12;
            }

            @Override
            public boolean isLoading() {
                return loading;
            }
        });

    }

    private void noInternetDialog() {
        new MaterialDialog.Builder(this)
                .title("No Internet")
                .content("Please Connect to Internet to load data")
                .positiveText("Retry")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        userData = new ArrayList<>();
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
        if (page <= 12) {
            loading = true;
            APIclient.getApi(mContext).callUserList("users", page).enqueue(new Callback<UserDataResponse>() {
                @Override
                public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                    if (response.code() == 200) {
                        MIN++;
                        userDataResponses = response.body();
                        assert userDataResponses != null;
                        if (userDataResponses.getData().size() > 0) {
                            userData.addAll(userDataResponses.getData());
                            showData();
                        }
                        loading = false;
//                    if (MIN <= MAX)
//                        makeServiceCall(++MIN);
                    } else loading = false;
                }

                @Override
                public void onFailure(Call<UserDataResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                    loading = false;
                }
            });
        } else loading = false;
    }

    private void showData() {

        UserAdapter userAdapter = new UserAdapter(userData, mContext);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);


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
