package utils;

import DTO.client.ClientInformationDTO;
import bankingSystem.BankingSystem;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

	private static final String BANK_ATTRIBUTE_NAME = "BankingSystem";
	private static final String CUSTOMER_LIST_ATTRIBUTE_NAME = "CustomerList";

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
}
