package mainApp.customer;

import DTO.refresher.ForAdminRefresherDTO;
import DTO.refresher.ForCustomerRefresherDTO;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.util.Constants.GSON_INSTANCE;
import static client.util.popup.AlertPopUp.alertPopUp;

public class CustomerRefresher extends TimerTask {
    private final Consumer<ForCustomerRefresherDTO> customerRefresherConsumer;

    public CustomerRefresher(Consumer<ForCustomerRefresherDTO> customerRefresher) {
        this.customerRefresherConsumer = customerRefresher;
    }
    @Override
    public void run() {
        HttpClientUtil.runAsyncGet(Constants.CUSTOMER_REFRESHER, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                alertPopUp("Refresh Error", "Something went wrong", e.getMessage());
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
