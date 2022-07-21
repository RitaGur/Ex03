package utils;

import DTO.client.ClientInformationDTO;
import DTO.loan.scramble.InvestmentLoanInformationDTO;
import bankingSystem.BankingSystem;
import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {
	public final static Gson GSON_INSTANCE = new Gson();
	private static final String BANK_ATTRIBUTE_NAME = "BankingSystem";
	private static final String ADMIN_ATTRIBUTE_IS_LOGGED_IN = "adminLogin";
	private static final String ADMIN_ATTRIBUTE_NAME = "adminName";
	private static final String ADMIN_ATTRIBUTE_NAME_IS_USED = "adminNameUsed";
	private static final String CUSTOMER_LIST_ATTRIBUTE_NAME = "CustomerList";
	private static final String INLAYS_ATTRIBUTE_NAME = "inlaysList";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object bankManagerLock = new Object();
	//private static final Object customerManagerLock = new Object();

	public static BankingSystem getBankingSystem(ServletContext servletContext) {

		synchronized (bankManagerLock) {
			if (servletContext.getAttribute(BANK_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(BANK_ATTRIBUTE_NAME, new BankingSystem());
			}
		}
		return (BankingSystem) servletContext.getAttribute(BANK_ATTRIBUTE_NAME);
	}

	public static List<ClientInformationDTO> getCustomerList(ServletContext servletContext) {
		return getBankingSystem(servletContext).showClientsInformation();
	}

	public static String getAdminName(ServletContext servletContext, String adminName) {
		synchronized (bankManagerLock) {
			if (servletContext.getAttribute(ADMIN_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(ADMIN_ATTRIBUTE_NAME, adminName);
				servletContext.setAttribute(ADMIN_ATTRIBUTE_NAME_IS_USED, true);
				return adminName;
			}
			return (String) servletContext.getAttribute(ADMIN_ATTRIBUTE_NAME);
		}
	}

	public static Boolean isAdminNameUsed(ServletContext servletContext) {
		return (boolean) servletContext.getAttribute(ADMIN_ATTRIBUTE_NAME_IS_USED);
	}

	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}

	public static ArrayList<InvestmentLoanInformationDTO> getInvestmentsListInfo(ServletContext servletContext) {
		synchronized (bankManagerLock) {
			if (servletContext.getAttribute(INLAYS_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(INLAYS_ATTRIBUTE_NAME, new ArrayList<InvestmentLoanInformationDTO>());
			}
		}
		return (ArrayList<InvestmentLoanInformationDTO>)servletContext.getAttribute(INLAYS_ATTRIBUTE_NAME);
	}

	public static boolean isAdminLoggedIn(ServletContext servletContext) {
		synchronized (bankManagerLock) {
			if (servletContext.getAttribute(ADMIN_ATTRIBUTE_IS_LOGGED_IN) == null) {
				servletContext.setAttribute(ADMIN_ATTRIBUTE_IS_LOGGED_IN, true);
				return false;
			}
			return (boolean) servletContext.getAttribute(ADMIN_ATTRIBUTE_IS_LOGGED_IN);
		}
	}
}
