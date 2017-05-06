package answer.king;

import java.math.BigDecimal;

import org.json.JSONObject;

import answer.king.model.Item;

/**
 * Contains item test specific methods
 */
public class ItemTest {

    public static Item createGoodItem() {

        final String price = "1.99";

        // create item as per step 4
        Item item = new Item();
        item.setName("Burger");
        item.setPrice(new BigDecimal(price));
        item.setId(1000001L);

        return item;
    }

    public static JSONObject itemToJson(Item item) {

        JSONObject json = new JSONObject();

        Long id = item.getId();
        if(id != null) {
            json.put("id", id);
        }

        json.put("name", item.getName());
        json.put("price", item.getPrice());

        return json;
    }

}