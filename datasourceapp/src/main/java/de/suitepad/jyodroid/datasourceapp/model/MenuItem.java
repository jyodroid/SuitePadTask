package de.suitepad.jyodroid.datasourceapp.model;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by johntangarife on 6/20/17.
 */

public final class MenuItem {

    @StringDef({TYPE_APPETIZER, TYPE_MAIN, TYPE_DRINK})

    @Retention(RetentionPolicy.SOURCE)
    public @interface MenuItemType {
    }

    public static final String TYPE_APPETIZER = "appetizer";
    public static final String TYPE_MAIN = "main course";
    public static final String TYPE_DRINK = "drink";

    private String itemId;
    private String itemName;
    private Double itemPrice;
    private MenuItemType Type;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public MenuItemType getType() {
        return Type;
    }

    public void setType(MenuItemType type) {
        Type = type;
    }
}
