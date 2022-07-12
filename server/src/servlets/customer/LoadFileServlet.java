package servlets.customer;

import DTO.client.ClientInformationDTO;
import DTO.loan.LoanInformationDTO;
import DTO.loan.scramble.InvestmentLoanInformationDTO;
import bankingSystem.BankingSystem;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebServlet(name = "LoadFileCustomerServlet",urlPatterns = "/customer/load-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadFileServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Collection<Part> parts = request.getParts();
        String customerFromSession = SessionUtils.getCustomer(request);
        List<ClientInformationDTO> customersList = ServletUtils.getCustomerList(getServletContext());

        if (customerFromSession != null) {
            synchronized (this) {
                try {
                    BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
                    for (Part part : parts) {
                        bankingSystem.readFromFile(part.getContentType(), part.getInputStream(), customerFromSession);
                        out.println("The file was uploaded successfully\n");
                    }
                } catch (FileNotFoundException fileNotFoundException) {
                    out.println("The system could not find the file, please check the file path again");
                } catch (Exception ex) {
                    if (ex.getMessage().equals(" ")) {
                        out.println("The file is not an xml file");
                    } else
                        out.println(ex.getMessage());
                }
            }
        } else {
            //user is not log-in
            out.println("Please be sure to log-in first");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    //TODO: delete
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            BankingSystem bankingSystem = ServletUtils.getBankingSystem(getServletContext());
            InvestmentLoanInformationDTO investmentLoanInformationDTO = new InvestmentLoanInformationDTO();
            investmentLoanInformationDTO.setAmountOfMoneyToInvest(2000);
            investmentLoanInformationDTO.setCustomerOfInvestmentName("avrum");
            ArrayList<String> categoriesList = new ArrayList<String>();
            categoriesList.add("damn the banks");
            investmentLoanInformationDTO.setChosenCategories(categoriesList);
            investmentLoanInformationDTO.setInterest(2);
            investmentLoanInformationDTO.setMaxOpenLoans(10);
            investmentLoanInformationDTO.setMinimumTotalTimeunits(3);
            investmentLoanInformationDTO.setMaxOwnershipPercentage(100);
            String json = gson.toJson(investmentLoanInformationDTO);
            out.println(json);
            out.flush();
        }
    }
}
