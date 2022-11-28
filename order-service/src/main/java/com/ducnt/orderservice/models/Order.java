package com.ducnt.orderservice.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_line")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    private String orderNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderLineItem> lineItems;

    public void setLineItems(List<OrderLineItem> lineItems) {
        this.lineItems = lineItems;
        lineItems.forEach(lineItem -> lineItem.setOrder(this));
    }
}
