package mainApp.admin;

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

public class CustomerRefresher extends TimerTask {
    private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<String>> usersListConsumer;
    private final BooleanProperty shouldUpdate;

    public CustomerRefresher(Consumer<String> httpRequestLoggerConsumer, Consumer<List<String>> usersListConsumer, BooleanProperty shouldUpdate) {
        this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.usersListConsumer = usersListConsumer;
        this.shouldUpdate = shouldUpdate;
    }

    @Override
    public void run() {
        if (!shouldUpdate.get()) {
            return;
        }

        //httpRequestLoggerConsumer.accept("About to invoke: " + Constants.USERS_LIST);
        HttpClientUtil.runAsyncGet(Constants.USERS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                httpRequestLoggerConsumer.accept("Something went wrong: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfUsersNames = response.body().string();
               // httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Response: " + jsonArrayOfUsersNames);
                String[] usersNames = GSON_INSTANCE.fromJson(jsonArrayOfUsersNames, String[].class);
                usersListConsumer.accept(Arrays.asList(usersNames));
            }
        });
    }
}
