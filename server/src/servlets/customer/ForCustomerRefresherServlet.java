package servlets.customer;

import DTO.refresher.ForCustomerRefresherDTO;
import bankingSystem.BankingSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static constants.Constants.YAZ_NUMBER;
import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "ForCustomerRefresherServlet", urlPatterns = "/customer/customerRefresher")
public class ForCustomerRefresherServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        String customerFromSession = SessionUtils.getCustomer(request);
        String yazOfRefresher = request.getParameter(YAZ_NUMBER);
        int yazOfRefresherInt = Integer.parseInt(yazOfRefresher);
        ForCustomerRefresherDTO forCustomerRefresherDTO = bankingSystem.getCustomerRefresherFromList(yazOfRefresherInt, customerFromSession);

        if (forCustomerRefresherDTO == null) {
           forCustomerRefresherDTO = bankingSystem.getEmptyRefresher();
        }
        String json = GSON_INSTANCE.toJson(forCustomerRefresherDTO);
        out.println(json);
        out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}