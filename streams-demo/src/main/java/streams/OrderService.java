package streams;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OrderService {

    private List<Order> orders = new ArrayList<>();


    public void saveOrder(Order order){
        orders.add(order);
    }

    public long countOrderByStatus(String status) {
        return orders.stream().filter(order -> order.getStatus().equals(status)).count();
    }

    public List<Order> collectOrdersWithProductCategory(String category) {
        return orders.stream()
                .filter(order -> order.getProducts()
                        .stream()
                        .anyMatch(p -> p.getCategory().equals(category)))
                .collect(Collectors.toList());
    }

    public List<Product> productsOverAmountPrice(int amount) {

        return orders.stream()
                .flatMap(order -> order.getProducts().stream())
                .filter(product -> product.getPrice() > amount)
                .collect(Collectors.toList());
    }

    public double amountBetweenTwoDates(LocalDate startDate, LocalDate endDate) {
        return orders.stream()
                .filter(order -> order.getOrderDate().isAfter(startDate) && order.getOrderDate().isBefore(endDate))
                .flatMap(order -> order.getProducts().stream())
                .mapToDouble(Product::getPrice)
                .sum();
    }

    public Order orderWithMostExpensiveProduct() {
        return orders.stream()
                .max(Comparator.comparing(order -> order.getProducts().stream().map(Product::getPrice).max(Comparator.comparing(Double::doubleValue)).orElseThrow()))
                .orElseThrow();

    }
}
