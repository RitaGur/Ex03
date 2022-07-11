package servlets.admin;

import DTO.client.ClientInformationDTO;
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

@WebServlet(name = "LoansInformationServlet", urlPatterns = "/admin/LoansInformation")
public class LoansInformationServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
            List<LoanInformationDTO> loansList = bankingSystem.showLoansInformation();
            String json = gson.toJson(loansList);
            out.println(json);
            out.flush();
        }
    }
}
