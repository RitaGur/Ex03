package servlets.admin;

import DTO.client.ClientInformationDTO;
import DTO.lists.CustomersListDTO;
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

@WebServlet(name = "CustomersInformationServlet", urlPatterns = "/admin/CustomersInformation")
public class CustomersInformationServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        List<ClientInformationDTO> clientInformationDTO = ServletUtils.getCustomerList(getServletContext());
        CustomersListDTO customersListDTO = new CustomersListDTO();
        customersListDTO.setCustomerList(clientInformationDTO);
        String json = GSON_INSTANCE.toJson(customersListDTO);
        out.println(json);
        out.flush();
    }
}
