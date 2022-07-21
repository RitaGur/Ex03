package servlets.customer;

import DTO.client.ClientInformationDTO;
import DTO.loan.LoanForSaleDTO;
import bankingSystem.BankingSystem;
import bankingSystem.timeline.bankClient.BankClient;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static constants.Constants.LOAN;

@WebServlet(name = "SellLoanServlet",urlPatterns = "/customer/sellLoan")
public class SellLoanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String customer = SessionUtils.getCustomer(request);
        String loanID = request.getParameter(LOAN);
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());

        synchronized (this) {
            try {
                bankingSystem.addToSellLoanList(loanID, customer);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(e.getMessage());
            }
        }
    }
}
