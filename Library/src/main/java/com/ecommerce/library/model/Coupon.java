package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
@ToString
@Entity
@Table(name="coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "coupon_id")
    private Long id;

    private String couponCode;
    private String couponDescription;
    private String offerPercentage;
    @DateTimeFormat(pattern = "yyyy/mm/dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy/mm/dd")
    private LocalDate expireDate;
    private Double minimumOrderAmount;
    private Double maximumOfferAmount;
    private int count;
    private boolean enabled;
    public boolean isExpired(){
        return (this.count == 0 || this.expireDate.isBefore(LocalDate.now()) || this.startDate.isAfter(LocalDate.now()));
    }

}
