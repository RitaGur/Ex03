package servlets.customer;

import DTO.lists.LoanListDTO;
import DTO.lists.PaymentNotificationsListDTO;
import bankingSystem.BankingSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name ="PaymentNotificationsServlet", urlPatterns = "/customer/paymentNotifications")
public class PaymentNotificationsServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String customerFromSession = SessionUtils.getCustomer(request);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        PaymentNotificationsListDTO paymentNotificationsListDTO = new PaymentNotificationsListDTO();
        try {
            paymentNotificationsListDTO.setPaymentsNotificationsDTOList(bankingSystem.getPaymentsNotificationInDTO(customerFromSession));
            String json = GSON_INSTANCE.toJson(paymentNotificationsListDTO);
            out.println(json);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
