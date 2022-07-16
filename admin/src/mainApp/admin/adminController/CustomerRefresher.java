package mainApp.admin.adminController;

import DTO.client.ClientInformationDTO;
import DTO.lists.CustomersListDTO;
import client.util.Constants;
import client.util.http.HttpClientUtil;
import javafx.beans.property.BooleanProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.util.Constants.GSON_INSTANCE;
import static client.util.popup.AlertPopUp.alertPopUp;

public class CustomerRefresher extends TimerTask {
    private final Consumer<List<ClientInformationDTO>> usersListConsumer;

    public CustomerRefresher(Consumer<List<ClientInformationDTO>> usersListConsumer) {
        this.usersListConsumer = usersListConsumer;
    }

    @Override
    public void run() {
        HttpClientUtil.runAsyncGet(Constants.ADMIN_SHOW_CUSTOMERS, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                alertPopUp("Refresh Error", "Something went wrong", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfCustomersNames = response.body().string();
                CustomersListDTO customersListDTO  = GSON_INSTANCE.fromJson(jsonArrayOfCustomersNames, CustomersListDTO.class);
                usersListConsumer.accept(customersListDTO.getCustomerList());
            }
        });
    }
}
