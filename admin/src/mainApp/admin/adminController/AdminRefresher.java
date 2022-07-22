package mainApp.admin.adminController;

import DTO.refresher.ForAdminRefresherDTO;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.util.Constants.GSON_INSTANCE;
import static client.util.popup.AlertPopUp.alertErrorPopUp;

public class AdminRefresher extends TimerTask {
    private final Consumer<ForAdminRefresherDTO> usersListConsumer;
    private int yazOfRefresher;

    public void setYazOfRefresher(int yazOfRefresher) {
        this.yazOfRefresher = yazOfRefresher;
    }

    public AdminRefresher(Consumer<ForAdminRefresherDTO> usersListConsumer) {
        yazOfRefresher = 1;
        this.usersListConsumer = usersListConsumer;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.ADMIN_REFRESHER)
                .newBuilder()
                .addQueryParameter("yazRefresher", String.valueOf(yazOfRefresher))
                .build()
                .toString();

        HttpClientUtil.runAsyncGet(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                alertErrorPopUp("Refresh Error", "Something went wrong", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfCustomersNames = response.body().string();
                ForAdminRefresherDTO adminRefresherDTO  = GSON_INSTANCE.fromJson(jsonArrayOfCustomersNames, ForAdminRefresherDTO.class);
                usersListConsumer.accept(adminRefresherDTO);
            }
        });
    }
}
