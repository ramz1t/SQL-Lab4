package se.hkr.model;

import java.sql.Date;

public record Customer(
        long id,
        String address,
        Date birth_date,
        String city,
        String first_name,
        String last_name,
        String postal_code
) {
}
