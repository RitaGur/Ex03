package servlets.customer;

import DTO.client.ClientInformationDTO;
import DTO.lists.CustomersListDTO;
import DTO.lists.LoanListDTO;
import bankingSystem.BankingSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "OpenLoansToPayServlet", urlPatterns = "/admin/openLoansToPay")
public class OpenLoansToPayServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String customerFromSession = SessionUtils.getCustomer(request);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        LoanListDTO loanListDTO = new LoanListDTO();
        try {
            loanListDTO.setLoanList(bankingSystem.getCustomerOpenLoansToPay(customerFromSession));
            String json = GSON_INSTANCE.toJson(loanListDTO);
            out.println(json);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
