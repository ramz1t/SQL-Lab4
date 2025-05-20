package se.hkr.model;

public record OrderLine(
        long id,
        long furniture_id,
        long order_id,
        int quantity
) {
}
