package servlets.admin;

import DTO.client.ClientInformationDTO;
import bankingSystem.BankingSystem;
import bankingSystem.timeline.bankClient.BankClient;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static constants.Constants.USERNAME;

@WebServlet(name ="AdminLoginServlet", urlPatterns = "/admin/login")
public class AdminLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out=response.getWriter();
        String adminFromSession = SessionUtils.getCustomer(request);

        if (adminFromSession == null) {
            String usernameFromParameter = request.getParameter(USERNAME);

            if (usernameFromParameter == null || usernameFromParameter.isEmpty() || (!usernameFromParameter.equals("Admin") && !usernameFromParameter.equals("admin"))) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    if (ServletUtils.isAdminLoggedIn(getServletContext())) {
                        String errorMessage = "Admin is already logged-in!";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    } else {
                        out.println("Welcome " + "Admin"+" to the bank app!");
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            //user is already logged in
            out.println("The Admin name is already in use!");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
