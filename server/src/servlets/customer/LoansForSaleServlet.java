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
import java.util.List;

import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "LoansForSaleServlet",urlPatterns = "/customer/loansForSale")
public class LoansForSaleServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String customer = SessionUtils.getCustomer(request);
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        LoanForSaleListDTO loanForSaleListDTO = new LoanForSaleListDTO();
        List<LoanForSaleDTO> listToSend = bankingSystem.getLoansToSellInDTOByCustomer(customer);
        loanForSaleListDTO.setLoanForSaleDTOList(listToSend);
        loanForSaleListDTO.setLoanForSaleListInformationDTO(bankingSystem.loanInformationForSale(listToSend));
        String json = GSON_INSTANCE.toJson(loanForSaleListDTO);
        out.println(json);
        out.flush();
    }
}