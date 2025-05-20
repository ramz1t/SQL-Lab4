package se.hkr.model;

public record OrderLineDetail(
        long id,
        long furniture_id,
        String furniture_name,
        long order_id,
        int quantity
) {
}
