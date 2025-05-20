package se.hkr.model;

import java.util.Date;

public record Furniture(
        long id,
        String color,
        String comment,
        String name,
        Double price,
        Date purchase_date,
        int shelf_nbr,
        Double weight
) {
}
