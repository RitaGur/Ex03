package servlets.customer.bonus;

import DTO.client.ClientInformationDTO;
import DTO.loan.LoanInformationDTO;
import DTO.loan.scramble.InvestmentLoanInformationDTO;
import bankingSystem.BankingSystem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebServlet(name = "ScrambleChooseLoansToInvestBonus",urlPatterns = "/customer/bonus/scrambleBonus")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class ScrambleChooseLoansToInvestBonus extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        List<InvestmentLoanInformationDTO> investmentsListInfoDTO = ServletUtils.getInvestmentsListInfo(getServletContext());
        List<ClientInformationDTO> clientList = ServletUtils.getCustomerList(getServletContext());
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Collection<Part> Parts = request.getParts();
        List<LoanInformationDTO> listOfChosenLoansDTO = new ArrayList<>();
        int counter = 1;
        String username = "";

        for (Part part : Parts){
            if (part.getName().contains("loan")) {
                String partValue = new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
                if (!checkLoanID(bankingSystem, partValue)) {
                    String errorMessage = "Loan ID " + partValue + " does not exists in banking system.\n";
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.println(errorMessage);
                    return;
                }
                listOfChosenLoansDTO.add(bankingSystem.getLoanDTOByLoanID(partValue, counter++));
            }
            else {
                username = new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
                if (!checkUserName(clientList, username)) {
                    String errorMassage = "username " + username + " does not exists in banking system.\n";
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.println(errorMassage);
                    return;
                }
            }
        }

        synchronized (this) {
            try {
                InvestmentLoanInformationDTO investmentLoanInformationDTO = investmentsListInfoDTO.get(0);
                bankingSystem.loansDistribution(listOfChosenLoansDTO, investmentLoanInformationDTO.getAmountOfMoneyToInvest(), username, investmentLoanInformationDTO.getMaxOwnershipPercentage());
                investmentsListInfoDTO.remove(investmentLoanInformationDTO);
                out.println("Investment is done!");
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //response.getOutputStream().println("Investment is done!");
       // response.setStatus(HttpServletResponse.SC_OK);
    }

    private Boolean checkUserName(List<ClientInformationDTO> clientList, String userName) {
        for (ClientInformationDTO customer : clientList) {
            if (customer.getClientName().equals(userName)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkLoanID(BankingSystem bankingSystem, String loanID) {
        return bankingSystem.isLoanIDExists(loanID);
    }
}
