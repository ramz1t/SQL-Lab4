package se.hkr.model;

public record Employee(
        long id,
        String address,
        String city,
        String first_name,
        String last_name,
        String postal_code
) {
}
