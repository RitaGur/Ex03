package servlets.customer;

import DTO.lists.LoanForSaleListDTO;
import DTO.lists.LoanListDTO;
import DTO.loan.LoanForSaleDTO;
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

@WebServlet(name = "LoansForSaleServlet",urlPatterns = "/customer/allLoansForSale")
public class AllLoansForSale extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String customer = SessionUtils.getCustomer(request);
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        LoanListDTO loanListDTO = new LoanListDTO();
        loanListDTO.setLoanList(bankingSystem.allLoansForSaleByCustomer(customer));
        String json = GSON_INSTANCE.toJson(loanListDTO);
        out.println(json);
        out.flush();
    }
}
