package servlets.customer;

import DTO.client.ClientInformationDTO;
import bankingSystem.BankingSystem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Add New Loan Bonus", urlPatterns = "/customer/newLoanBonus")
public class AddNewLoanBonus extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        List<ClientInformationDTO> clientList = ServletUtils.getCustomerList(getServletContext());
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        String errorMessage;

        String userName = request.getParameter("username");
        String loanID = request.getParameter("loanID");
        String loanCategory = request.getParameter("loanCategory");
        String loanAmount = request.getParameter("loanAmount");
        String totalYaz = request.getParameter("totalYaz");
        String yazBetweenPayment = request.getParameter("yazBetweenPayment");
        String loanInterest = request.getParameter("loanInterest");

        if (!checkUserName(clientList, userName)) {
            errorMessage = "User name " + userName + " does not exists in banking system.";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println(errorMessage);
        }
        else if (checkLoanID(bankingSystem, loanID)) {
            errorMessage = "Loan ID " + loanID + " already exists in banking system.";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println(errorMessage);
        }
        else {
            try {
                int loanAmountInt = Integer.parseInt(loanAmount);
            } catch (NumberFormatException ex) {
                errorMessage = "loan amount needs to be numbers only.";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(errorMessage);
            }
            try {
                int totalYazInt = Integer.parseInt(totalYaz);
            } catch (NumberFormatException ex) {
                errorMessage = "Total Yaz needs to be numbers only.";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(errorMessage);
            }
            try {
                int yazBetweenPaymentInt = Integer.parseInt(yazBetweenPayment);
            } catch (NumberFormatException ex) {
                errorMessage = "Yaz between payments needs to be numbers only.";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(errorMessage);
            }
            try {
                int loanInterestInt = Integer.parseInt(loanInterest);
            } catch (NumberFormatException ex) {
                errorMessage = "loan interest needs to be numbers only.";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(errorMessage);
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getOutputStream().print("Work!");
        //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);*/

        response.getOutputStream().print("WORK");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private boolean checkLoanID(BankingSystem bankingSystem, String loanID) {
        return bankingSystem.isLoanIDExists(loanID);
    }

    private Boolean checkUserName(List<ClientInformationDTO> clientList, String userName) {
        for (ClientInformationDTO customer : clientList) {
            if (customer.getClientName().equals(userName)) {
                return true;
            }
        }

        return false;
    }
}
