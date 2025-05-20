package se.hkr.model;

import java.util.Date;

public record OrderDetail(
        long id,
        Date order_date,
        long customer_id,
        String customer_name,
        long employee_id
) {
}
