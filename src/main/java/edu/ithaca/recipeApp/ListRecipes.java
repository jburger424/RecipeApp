package edu.ithaca.recipeApp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ListRecipes {

    public List<Recipe> get_recipes() throws IOException{

        File file = new File("src/main/resources/recipe.json");
        System.out.println(file.exists());

        ObjectMapper objectMapper = new ObjectMapper();

        Cookbook cookbook = objectMapper.readValue(file, Cookbook.class);

        return cookbook.getRecipes();
    }

    public void list_all() throws IOException{

        List<Recipe> recipes= get_recipes();

        for(int i = 0; i < recipes.size(); i++){
            System.out.println("ID: " + recipes.get(i).getId() + " Name: " + recipes.get(i).getTitle());
        }



    }


    public static void main(String[] args)throws IOException{
        System.out.println("Hello World");
        ListRecipes listRecipes = new ListRecipes();
        listRecipes.list_all();


    }
}
class Cookbook{
    private List<Recipe> recipe;
    public List<Recipe> getRecipes(){
        return recipe;
    }
    public void setRecipes(List<Recipe> recipes){
        this.recipe = recipes;
    }

}

class Recipe{

    private String id;
    private String title;
    private String image;
    private String source;
    private String owner;
    private List<String> tags;
    private List<Ingredient> ingredients;
    private String recipe;
    private String servings;
    private String calsperserving;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getCalsperserving() {
        return calsperserving;
    }

    public void setCalsperserving(String calsperserving) {
        this.calsperserving = calsperserving;
    }

}

class Ingredient{
    private String name;
    private String quantity;
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getQuantity(){
        return this.quantity;
    }
    public void setQuantity(String quantity){
        this.quantity = quantity;
    }
}

