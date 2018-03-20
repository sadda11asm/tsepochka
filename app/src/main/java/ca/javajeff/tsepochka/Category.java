package ca.javajeff.tsepochka;

/**
 * Created by Саддам on 15.03.2018.
 */

public class Category {
    private String category;
    private boolean isChosen;

    public Category (String s) {
        category = s;
        isChosen = true;
    }

    public String getCategory() {
        return category;
    }
    public boolean isSelected() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
