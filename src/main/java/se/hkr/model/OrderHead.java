package se.hkr.model;

import java.util.Date;

public record OrderHead(
        long id,
        Date order_date,
        long customer_id,
        long employee_id
) {
}
