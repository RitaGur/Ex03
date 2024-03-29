package servlets.customer;

import DTO.lists.LoanListDTO;
import DTO.loan.LoanInformationDTO;
import DTO.loan.scramble.InvestmentLoanInformationDTO;
import bankingSystem.BankingSystem;
import bankingSystem.timeline.loan.InvestmentLoan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "ScrambleServlet",urlPatterns = "/customer/scramble")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class ScrambleServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        List<InvestmentLoanInformationDTO> investmentsListInfoDTO = ServletUtils.getInvestmentsListInfo(getServletContext());
        String customerFromSession = SessionUtils.getCustomer(request);
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain;charset=UTF-8");
        Collection<Part> Parts = request.getParts();
        List<LoanInformationDTO> listOfChosenLoansDTO = new ArrayList<>();
        int counter = 1;

        for (Part part : Parts){
            String partValue = new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
            listOfChosenLoansDTO.add(bankingSystem.getLoanDTOByLoanID(partValue, counter++));
        }

        synchronized (this) {
            try {
                InvestmentLoanInformationDTO investmentLoanInformationDTO = investmentsListInfoDTO.get(0);
                bankingSystem.loansDistribution(listOfChosenLoansDTO, investmentLoanInformationDTO.getAmountOfMoneyToInvest(),customerFromSession, investmentLoanInformationDTO.getMaxOwnershipPercentage());
                investmentsListInfoDTO.remove(investmentLoanInformationDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}