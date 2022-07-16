package servlets.admin;

import DTO.client.ClientInformationDTO;
import DTO.lists.LoanListDTO;
import DTO.loan.LoanInformationDTO;
import bankingSystem.BankingSystem;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "LoansInformationServlet", urlPatterns = "/admin/LoansInformation")
public class LoansInformationServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        LoanListDTO loanListDTO = new LoanListDTO();
        loanListDTO.setLoanList(bankingSystem.showLoansInformation());
        String json = GSON_INSTANCE.toJson(loanListDTO);
        out.println(json);
        out.flush();
    }
}
