package servlets.customer;

import DTO.loan.LoanForSaleDTO;
import DTO.loan.LoanInformationDTO;
import bankingSystem.BankingSystem;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static constants.Constants.LOAN;
import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "BuyLoanServlet",urlPatterns = "/customer/buyLoan")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class BuyLoanServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        BufferedReader rd = request.getReader();
        String line = null;
        StringBuilder rawBody = new StringBuilder();
        String customerFromSession = SessionUtils.getCustomer(request);

        while ((line = rd.readLine()) != null) {
            rawBody.append(line);
        }
        synchronized (this) {
            LoanForSaleDTO loanToBuy = GSON_INSTANCE.fromJson(rawBody.toString(), LoanForSaleDTO.class);
            try {
                bankingSystem.buyLoan(loanToBuy.getLoanForSaleID(), loanToBuy.getLoanSellerName(), customerFromSession);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(e.getMessage());
            }
        }
    }
}
