package answer.king.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_RECEIPT")
public class Receipt {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private BigDecimal payment;

	@OneToOne // TODO attributes
	private Order order;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public BigDecimal getChange() {
		BigDecimal totalOrderPrice = order.getTotal();
		return payment.subtract(totalOrderPrice);
	}
}
