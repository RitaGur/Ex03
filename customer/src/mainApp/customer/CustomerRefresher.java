package mainApp.customer;

import DTO.refresher.ForCustomerRefresherDTO;
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

public class CustomerRefresher extends TimerTask {
    private final Consumer<ForCustomerRefresherDTO> customerRefresherConsumer;
    private int yazOfRefresher;

    public CustomerRefresher(Consumer<ForCustomerRefresherDTO> customerRefresher) {
        this.customerRefresherConsumer = customerRefresher;
        yazOfRefresher = 1;
    }

    public void setYazOfRefresher(int yazOfRefresher) {
        this.yazOfRefresher = yazOfRefresher;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.CUSTOMER_REFRESHER)
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
                ForCustomerRefresherDTO customerRefresherDTO  = GSON_INSTANCE.fromJson(jsonArrayOfCustomersNames, ForCustomerRefresherDTO.class);
                customerRefresherConsumer.accept(customerRefresherDTO);
            }
        });
    }
}
