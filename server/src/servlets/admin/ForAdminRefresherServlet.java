package servlets.admin;

import DTO.lists.CustomersListDTO;
import DTO.refresher.ForAdminRefresherDTO;
import bankingSystem.BankingSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "ForAdminRefresherServlet", urlPatterns = "/admin/adminRefresher")
public class ForAdminRefresherServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        ForAdminRefresherDTO forAdminRefresherDTO = new ForAdminRefresherDTO();
        setAdminRefresherParams(forAdminRefresherDTO, bankingSystem);
        String json = GSON_INSTANCE.toJson(forAdminRefresherDTO);
        out.println(json);
        out.flush();
    }

    private void setAdminRefresherParams(ForAdminRefresherDTO adminRefresherDTO, BankingSystem bankingSystem) {
        adminRefresherDTO.setAdminLoanList(bankingSystem.showLoansInformation());
        adminRefresherDTO.setAdminCustomerList(bankingSystem.showClientsInformation());
        adminRefresherDTO.setCurrentYaz(bankingSystem.getCurrentTimeUnit().getCurrentTimeUnit());
    }
}
