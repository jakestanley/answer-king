package answer.king;

import java.math.BigDecimal;

public class InsufficientFundsException extends Exception {

	private static final long serialVersionUID = 1L;

	public InsufficientFundsException( 	BigDecimal required, 
										BigDecimal payment) {
		super(String.format(
				"Insufficient funds! Attempted to pay an invoice of %s with %s", 
				required.toString(), payment.toString()));
	}

}
