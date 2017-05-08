package answer.king;

import answer.king.model.Order;

/**
 * Contains order test specific methods
 */
public class OrderTest {

    public static Order createGoodOrder(Long id) {

        Order order = new Order();

        // depends on POST or PUT
        if(id != null) {
            order.setId(id);
        }

        return order;
    }

}