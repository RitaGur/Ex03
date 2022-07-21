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
        ForCustomerRefresherDTO forCustomerRefresherDTO = new ForCustomerRefresherDTO();
        synchronized (this) {
            setCustomerRefresherParams(forCustomerRefresherDTO, bankingSystem, customerFromSession);
        }
        String json = GSON_INSTANCE.toJson(forCustomerRefresherDTO);
        out.println(json);
        out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCustomerRefresherParams(ForCustomerRefresherDTO customerRefresherDTO, BankingSystem bankingSystem, String customerFromSession) throws Exception {
        customerRefresherDTO.setCustomerBalance(bankingSystem.getCustomerBalanceByName(customerFromSession));
        customerRefresherDTO.setCurrentYaz(bankingSystem.getCurrentTimeUnit().getCurrentTimeUnit());
        customerRefresherDTO.setCustomerPaymentNotificationsList(bankingSystem.getPaymentsNotificationInDTO(customerFromSession));
        customerRefresherDTO.setCustomerRecentTransactionsList(bankingSystem.getCustomerRecentTransactionByName(customerFromSession));
        customerRefresherDTO.setCustomerLenderLoansList(bankingSystem.getCustomerLenderLoans(customerFromSession));
        customerRefresherDTO.setCustomerLonerLoansList(bankingSystem.getCustomerLoanerLoans(customerFromSession));
        customerRefresherDTO.setCustomerPaymentLoanerLoansList(bankingSystem.getCustomerOpenLoansToPay(customerFromSession));
        customerRefresherDTO.setLoanCategoryList(bankingSystem.getLoanCategoryList());
        customerRefresherDTO.setLoansForSaleList(bankingSystem.getLoanForSaleForRefresher(customerFromSession));
    }
}