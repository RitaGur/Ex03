package mainApp.admin;

import javafx.concurrent.Task;

public class LoanOptionsTask extends Task<Boolean> {
    private final int SLEEP_TIME = 5;

    @Override
    protected Boolean call() throws Exception {
        /*try {

        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
        return true;
    }

    private void sleepForAWhile(int sleepTime) {
        if (sleepTime != 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {

            }
        }
    }
}


