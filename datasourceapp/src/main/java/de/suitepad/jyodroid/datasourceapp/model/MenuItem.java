package de.suitepad.jyodroid.datasourceapp.model;

/**
 * Created by johntangarife on 6/20/17.
 */

public final class MenuItem {

//    @StringDef({TYPE_APPETIZER, TYPE_MAIN, TYPE_DRINK})
//
//    @Retention(RetentionPolicy.SOURCE)
//    public @interface MenuItemType {
//    }
//
//    public static final String TYPE_APPETIZER = "appetizer";
//    public static final String TYPE_MAIN = "main course";
//    public static final String TYPE_DRINK = "drink";

    private String id;
    private String name;
    private Double price;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
