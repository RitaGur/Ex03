package servlets.customer;

import DTO.lists.LoanListDTO;
import DTO.loan.LoanInformationDTO;
import DTO.loan.scramble.InvestmentLoanInformationDTO;
import bankingSystem.BankingSystem;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "ScrambleLoansOptions",urlPatterns = "/customer/scrambleLoansOptions")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class ScrambleLoansOptions extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        List<InvestmentLoanInformationDTO> investmentsListInfoDTO = ServletUtils.getInvestmentsListInfo(getServletContext());
        String customerFromSession = SessionUtils.getCustomer(request);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        BufferedReader rd = request.getReader();
        String line = null;
        StringBuilder rawBody = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            rawBody.append(line);
        }
        synchronized (this) {
            InvestmentLoanInformationDTO investmentLoanInfoDTO = GSON_INSTANCE.fromJson(rawBody.toString(), InvestmentLoanInformationDTO.class);
            List<LoanInformationDTO> potentialLoans = null;
            try {
                potentialLoans = bankingSystem.optionsForLoans(investmentLoanInfoDTO.getCustomerOfInvestmentName(), investmentLoanInfoDTO.getChosenCategories(), investmentLoanInfoDTO.getAmountOfMoneyToInvest(), investmentLoanInfoDTO.getInterest(),
                        investmentLoanInfoDTO.getMinimumTotalTimeunits(), investmentLoanInfoDTO.getMaxOpenLoans());
                investmentsListInfoDTO.add(investmentLoanInfoDTO);
            } catch (Exception e) {
                out.println(e.getMessage());
            }
            LoanListDTO potentialLoanListDTO = new LoanListDTO();
            potentialLoanListDTO.setLoanList(potentialLoans);
            String json = GSON_INSTANCE.toJson(potentialLoanListDTO);
            out.println(json);
            out.flush();
        }
    }
}
