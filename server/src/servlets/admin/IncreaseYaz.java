package servlets.admin;

import DTO.client.ClientInformationDTO;
import bankingSystem.BankingSystem;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "IncreaseYaz",urlPatterns = "/admin/increaseYaz")
public class IncreaseYaz extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());

        synchronized (this) {
            try {
                bankingSystem.addNewAdminRefresher();
                List<ClientInformationDTO> clientInformationDTO = ServletUtils.getCustomerList(getServletContext());
                for (ClientInformationDTO client : clientInformationDTO) {
                    bankingSystem.addNewCustomerRefresher(client.getClientName());
                }

                bankingSystem.promoteTimeline();
                out.println(bankingSystem.getCurrentTimeUnit().getCurrentTimeUnit()); //current yaz
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
