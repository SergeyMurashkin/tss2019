package net.thumbtack.onlineshop.dto.responses;

import java.util.List;
import java.util.Map;

public class ConsolidatedStatementResponse {

    private Map<Integer, Integer> categoriesId_totalPurchases;
    private Map<Integer, List<PurchaseProductResponse>> categoriesId_purchases;

    private Map<Integer, Integer> productsId_totalPurchases;
    private Map<Integer, List<PurchaseProductResponse>> productsId_purchases;

    private Map<Integer, Integer> clientsId_totalPurchases;
    private Map<Integer, List<PurchaseProductResponse>> clientsId_purchases;

    public ConsolidatedStatementResponse() {
    }

    public Map<Integer, Integer> getCategoriesId_totalPurchases() {
        return categoriesId_totalPurchases;
    }

    public void setCategoriesId_totalPurchases(Map<Integer, Integer> categoriesId_totalPurchases) {
        this.categoriesId_totalPurchases = categoriesId_totalPurchases;
    }

    public Map<Integer, List<PurchaseProductResponse>> getCategoriesId_purchases() {
        return categoriesId_purchases;
    }

    public void setCategoriesId_purchases(Map<Integer, List<PurchaseProductResponse>> categoriesId_purchases) {
        this.categoriesId_purchases = categoriesId_purchases;
    }

    public Map<Integer, Integer> getProductsId_totalPurchases() {
        return productsId_totalPurchases;
    }

    public void setProductsId_totalPurchases(Map<Integer, Integer> productsId_totalPurchases) {
        this.productsId_totalPurchases = productsId_totalPurchases;
    }

    public Map<Integer, List<PurchaseProductResponse>> getProductsId_purchases() {
        return productsId_purchases;
    }

    public void setProductsId_purchases(Map<Integer, List<PurchaseProductResponse>> productsId_purchases) {
        this.productsId_purchases = productsId_purchases;
    }

    public Map<Integer, Integer> getClientsId_totalPurchases() {
        return clientsId_totalPurchases;
    }

    public void setClientsId_totalPurchases(Map<Integer, Integer> clientsId_totalPurchases) {
        this.clientsId_totalPurchases = clientsId_totalPurchases;
    }

    public Map<Integer, List<PurchaseProductResponse>> getClientsId_purchases() {
        return clientsId_purchases;
    }

    public void setClientsId_purchases(Map<Integer, List<PurchaseProductResponse>> clientsId_purchases) {
        this.clientsId_purchases = clientsId_purchases;
    }
}
