package servlets.customer.bonus;

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

@WebServlet(name = "Add New Loan Bonus", urlPatterns = "/customer/bonus/newLoanBonus")
public class AddNewLoanBonus extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        List<ClientInformationDTO> clientList = ServletUtils.getCustomerList(getServletContext());
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        String errorMessage = "";

        String userName = request.getParameter("username");
        String loanID = request.getParameter("loanID");
        String loanCategory = request.getParameter("loanCategory");
        String loanAmount = request.getParameter("loanAmount");
        String totalYaz = request.getParameter("totalYaz");
        String yazBetweenPayment = request.getParameter("yazBetweenPayment");
        String loanInterest = request.getParameter("loanInterest");

        int loanAmountInt = 0, totalYazInt = 0, yazBetweenPaymentInt = 0, loanInterestInt = 0;

        if (userName.equals("") || loanID.equals("") || loanCategory.equals("") || loanAmount.equals("") ||
            totalYaz.equals("") || loanInterest.equals("")) {
            errorMessage = "Please fill all fields.\n";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println(errorMessage);
        }
        else if (!checkUserName(clientList, userName)) {
            errorMessage = "User name " + userName + " does not exists in banking system.\n";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println(errorMessage);
        }
        else if (checkLoanID(bankingSystem, loanID)) {
            errorMessage = "Loan ID " + loanID + " already exists in banking system.\n";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println(errorMessage);
        }
        else {
            try {
                loanAmountInt = Integer.parseInt(loanAmount);
            } catch (NumberFormatException ex) {
                errorMessage = "loan amount needs to be numbers only.\n";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(errorMessage);
                return ;
            }
            try {
                totalYazInt = Integer.parseInt(totalYaz);
            } catch (NumberFormatException ex) {
                errorMessage = "Total Yaz needs to be numbers only.\n";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(errorMessage);
                return ;
            }
            try {
                yazBetweenPaymentInt = Integer.parseInt(yazBetweenPayment);
            } catch (NumberFormatException ex) {
                errorMessage = "Yaz between payments needs to be numbers only.\n";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(errorMessage);
                return ;
            }
            try {
                loanInterestInt = Integer.parseInt(loanInterest);
                checkLoanInterest(loanInterestInt);

                bankingSystem.addLoan(loanID, userName, loanAmountInt, totalYazInt, yazBetweenPaymentInt, loanInterestInt, loanCategory);
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("The loan was added successfully!\n");
            } catch (NumberFormatException ex) {
                errorMessage = "loan interest needs to be numbers only\n.";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(errorMessage);
                return ;
            } catch (Exception e) {
                out.println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    private void checkLoanInterest(int loanInterestInt) throws Exception {
        if (loanInterestInt <= 0 || loanInterestInt > 100) {
            throw new Exception("loan interest needs to be a number between 1 to 100.");
        }
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
