package test.model;

public class Order {
    private String[] ingredients;
    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public Order(String[] ingredients) {
        this.ingredients = ingredients;
    }

}
