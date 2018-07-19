package sg.edu.rp.webservices.c302_p09_mcafe;

import java.io.Serializable;

public class Menu implements Serializable {
    private String menuId;
    private String description;
    private String unit_price;

    public Menu(String menuId, String description, String unit_price) {
        this.menuId = menuId;
        this.description = description;
        this.unit_price = unit_price;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    @Override
    public String toString() {
        return description;
    }
}
