package servlets.customer;

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

@WebServlet(name ="CustomerLoginServlet", urlPatterns = "/customer/login")
public class CustomerLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out=response.getWriter();
        String customerFromSession = SessionUtils.getCustomer(request);

        List<ClientInformationDTO> customersList = ServletUtils.getCustomerList(getServletContext());
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());

        if (customerFromSession == null) {
            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    String finalUsernameFromParameter = usernameFromParameter;
                    if (customersList.stream().anyMatch(c -> c.getClientName().equals(finalUsernameFromParameter))) {
                        String errorMessage = "Username " + usernameFromParameter + " already exists. Please enter a different username.";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        out.println(errorMessage);
                        //response.getOutputStream().print(errorMessage);
                    } else {
                        BankClient newBankClient = new BankClient(0, finalUsernameFromParameter);

                        //add the new customer to the banking system
                        bankingSystem.addBankClientByClient(newBankClient);

                        //set the username in a session so it will be available on each request
                        //the true parameter means that if a session object does not exists yet
                        //create a new one
                        request.getSession(true).setAttribute(USERNAME, usernameFromParameter);

                        //redirect the request to the chat room - in order to actually change the URL
                        //TODO: delete!
                        out.println("Welcome " + usernameFromParameter+" to the bank app!");
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            //user is already logged in
            //TODO: delete!
            out.println("The user name is already in use!");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
