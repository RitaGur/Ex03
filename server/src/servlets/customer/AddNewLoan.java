package servlets.customer;

import DTO.loan.LoanInformationDTO;
import bankingSystem.BankingSystem;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "AddNewLoan",urlPatterns = "/customer/addNewLoan")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class AddNewLoan extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        BufferedReader rd = request.getReader();
        String line = null;
        StringBuilder rawBody = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            rawBody.append(line);
        }

        synchronized (this) {
            try {
                LoanInformationDTO loanToAdd = GSON_INSTANCE.fromJson(rawBody.toString(), LoanInformationDTO.class);
                checkLoanID(loanToAdd, bankingSystem);
                bankingSystem.addLoan(loanToAdd.getLoanNameID(),loanToAdd.getBorrowerName(), loanToAdd.getLoanStartSum(),
                        loanToAdd.getLoanSumOfTimeUnit(), loanToAdd.getTimeUnitsBetweenPayments(), loanToAdd.getLoanInterest(),
                        loanToAdd.getLoanCategory());
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(e.getMessage());
            }
        }
    }

    private void checkLoanID(LoanInformationDTO loanToAdd, BankingSystem bankingSystem) throws Exception {
        if(bankingSystem.isLoanIDExists(loanToAdd.getLoanNameID())) {
            throw new Exception("Loan ID already exists.");
        }
    }
}
