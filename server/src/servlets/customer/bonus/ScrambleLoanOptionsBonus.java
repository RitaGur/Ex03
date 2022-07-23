package servlets.customer.bonus;

import DTO.client.ClientInformationDTO;
import DTO.lists.LoanListDTO;
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
import java.util.Objects;

import static constants.Constants.LOAN;
import static utils.ServletUtils.GSON_INSTANCE;

@WebServlet(name = "ScrambleLoanOptionsBonus",urlPatterns = "/customer/bonus/scrambleLoansOptionsBonus")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class ScrambleLoanOptionsBonus extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
        List<ClientInformationDTO> clientList = ServletUtils.getCustomerList(getServletContext());
        Collection<Part> Parts = request.getParts();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        String partValue = null, customerName = "", amountOfMoney ="", loanInterest = "", minTotalYaz = "";
        String maxOpenLoans = "", maxOwnership = "";
        int amountOfMoneyInt = 0, minTotalYazInt = 0, maxOpenLoansInt = 0, loanInterestInt = 0, maxOwnershipInt = 0;
        List<String> categoryList = new ArrayList<>();

        for (Part part : Parts){
            if (part.getName().contains("category")) {
                partValue = new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
                categoryList.add(partValue);
            }
            else if (Objects.equals(part.getName(), "username")) {
                customerName = new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
            }
            else if (Objects.equals(part.getName(), "amountOfMoney")) {
                amountOfMoney = new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
            }
            else if (Objects.equals(part.getName(), "loanInterest")) {
                loanInterest = new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
            }
            else if (Objects.equals(part.getName(), "minTotalYaz")) {
                minTotalYaz = new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
            }
            else if (Objects.equals(part.getName(), "maxOpenLoans")) {
                maxOpenLoans = new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
            }
            else if (Objects.equals(part.getName(), "maxOwnership")) {
                maxOwnership = new BufferedReader(new InputStreamReader(part.getInputStream())).readLine();
            }
        }

        String errorMessage = "";
        if (customerName.equals("")) {
            errorMessage = "username field in required!\n";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println(errorMessage);
        }
        else if (amountOfMoney.equals("")) {
            errorMessage = "amount of money field in required!\n";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println(errorMessage);
        }
        else if (checkAllCategoriesAreNotEmpty(categoryList)) {
            errorMessage = "Please fill all category fields.\n";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println(errorMessage);
        }
        else if (!checkUserName(clientList, customerName)) {
            errorMessage = "username " + customerName + " does not exists in banking system.\n";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println(errorMessage);
        }
        else {
            try {
                checkCategoryList(bankingSystem, categoryList);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(e.getMessage());
                return;
            }

            try {
                amountOfMoneyInt = Integer.parseInt(amountOfMoney);
            } catch (NumberFormatException ex) {
                errorMessage = "Amount of money to invest needs to be numbers only.\n";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(errorMessage);
                return;
            }
            try {
                minTotalYazInt = Integer.parseInt(minTotalYaz);
            } catch (NumberFormatException ex) {
                if (minTotalYaz != null) {
                    errorMessage = "Min Total Yaz needs to be numbers only.\n";
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.println(errorMessage);
                    return;
                }
            }
            try {
                maxOpenLoansInt = Integer.parseInt(maxOpenLoans);
            } catch (NumberFormatException ex) {
                if (maxOpenLoans != null){
                    errorMessage = "Max open loans needs to be numbers only.\n";
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.println(errorMessage);
                    return;
                }
            }
            try {
                maxOwnershipInt = Integer.parseInt(maxOwnership);
            } catch (NumberFormatException ex) {
                if (maxOwnership != null) {
                    errorMessage = "Max ownership needs to be numbers only.\n";
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.println(errorMessage);
                    return;
                }
            }

            try {
                loanInterestInt = Integer.parseInt(loanInterest);
            } catch (NumberFormatException ex) {
                if (loanInterest != null) {
                    errorMessage = "loan interest needs to be numbers only.\n";
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.println(errorMessage);
                    return;
                }
            }

            try {
                checkLoanInterest(loanInterestInt);

                List<InvestmentLoanInformationDTO> investmentsListInfoDTO = ServletUtils.getInvestmentsListInfo(getServletContext());

                InvestmentLoanInformationDTO investmentLoanInfoDTO = new InvestmentLoanInformationDTO();
                setParamsToInvestmentLoanInfo(investmentLoanInfoDTO, maxOwnershipInt, maxOpenLoansInt, customerName,
                        loanInterestInt, maxOpenLoansInt, categoryList, amountOfMoneyInt, minTotalYazInt);

                investmentsListInfoDTO.add(investmentLoanInfoDTO);

                List<LoanInformationDTO> potentialLoans = bankingSystem.optionsForLoans(customerName, categoryList,
                        amountOfMoneyInt, loanInterestInt, minTotalYazInt, maxOpenLoansInt);

                if (potentialLoans.size() == 0) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    out.println("There are no adequate loans for your parameters");
                    return;
                }

                LoanListDTO potentialLoanListDTO = new LoanListDTO();
                potentialLoanListDTO.setLoanList(potentialLoans);
                String json = GSON_INSTANCE.toJson(potentialLoanListDTO);
                out.println(json);
                out.flush();

                response.setStatus(HttpServletResponse.SC_OK);
            }
             catch (Exception e) {
                out.println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    private void setParamsToInvestmentLoanInfo(InvestmentLoanInformationDTO investmentLoanInfoDTO, int maxOwnershipInt, int maxOpenLoansInt, String customerName, int loanInterestInt, int maxOpenLoansInt1, List<String> categoryList, int amountOfMoneyInt, int minTotalYazInt) {
        investmentLoanInfoDTO.setMaxOwnershipPercentage(maxOwnershipInt);
        investmentLoanInfoDTO.setCustomerOfInvestmentName(customerName);
        investmentLoanInfoDTO.setInterest(loanInterestInt);
        investmentLoanInfoDTO.setMaxOpenLoans(maxOpenLoansInt);
        investmentLoanInfoDTO.setChosenCategories(categoryList);
        investmentLoanInfoDTO.setAmountOfMoneyToInvest(amountOfMoneyInt);
        investmentLoanInfoDTO.setMinimumTotalTimeunits(minTotalYazInt);
    }

    private boolean checkAllCategoriesAreNotEmpty(List<String> categoryList) {
        for (String category : categoryList) {
            if (category.equals("")) {
                return true;
            }
        }

        return false;
    }

    private boolean checkCategoryList(BankingSystem bankingSystem, List<String> categoryList) throws Exception {
        for (String category : categoryList) {
            if (!bankingSystem.isCategoryExist(category)) {
                throw new Exception("Category " + category + " does not exists in banking system.\n");
            }
        }

        return true;
    }

    private Boolean checkUserName(List<ClientInformationDTO> clientList, String userName) {
        for (ClientInformationDTO customer : clientList) {
            if (customer.getClientName().equals(userName)) {
                return true;
            }
        }

        return false;
    }

    private void checkLoanInterest(int loanInterestInt) throws Exception {
        if (loanInterestInt < 0 || loanInterestInt > 100) {
            throw new Exception("loan interest needs to be a number between 1 to 100.");
        }
    }
}