package net.thumbtack.onlineshop.dto.responses;

import net.thumbtack.onlineshop.model.Category;
import net.thumbtack.onlineshop.model.Purchase;

import java.util.List;
import java.util.Map;

public class ConsolidatedStatementResponse {

    private List<Purchase> categoriesPurchases;
    private Map<Category, Integer> categoriesTotalPurchases;
    private List<Purchase> productsPurchases;
    private Map<Category, Integer> productsTotalPurchases;
    private List<Purchase> clientsPurchases;
    private Map<Category, Integer> clientsTotalPurchases;

    public ConsolidatedStatementResponse(){

    }

    public List<Purchase> getCategoriesPurchases() {
        return categoriesPurchases;
    }

    public void setCategoriesPurchases(List<Purchase> categoriesPurchases) {
        this.categoriesPurchases = categoriesPurchases;
    }

    public Map<Category, Integer> getCategoriesTotalPurchases() {
        return categoriesTotalPurchases;
    }

    public void setCategoriesTotalPurchases(Map<Category, Integer> categoriesTotalPurchases) {
        this.categoriesTotalPurchases = categoriesTotalPurchases;
    }

    public List<Purchase> getProductsPurchases() {
        return productsPurchases;
    }

    public void setProductsPurchases(List<Purchase> productsPurchases) {
        this.productsPurchases = productsPurchases;
    }

    public Map<Category, Integer> getProductsTotalPurchases() {
        return productsTotalPurchases;
    }

    public void setProductsTotalPurchases(Map<Category, Integer> productsTotalPurchases) {
        this.productsTotalPurchases = productsTotalPurchases;
    }

    public List<Purchase> getClientsPurchases() {
        return clientsPurchases;
    }

    public void setClientsPurchases(List<Purchase> clientsPurchases) {
        this.clientsPurchases = clientsPurchases;
    }

    public Map<Category, Integer> getClientsTotalPurchases() {
        return clientsTotalPurchases;
    }

    public void setClientsTotalPurchases(Map<Category, Integer> clientsTotalPurchases) {
        this.clientsTotalPurchases = clientsTotalPurchases;
    }
}
