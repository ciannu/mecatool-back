package com.svf.mecatool.integration.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "work_orders")
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    private String description;

    @Enumerated(EnumType.STRING)
    private WorkOrderStatus status;

    private LocalDate startDate;
    private LocalDate endDate;

    private BigDecimal total;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "work_order_mechanics",
            joinColumns = @JoinColumn(name = "work_order_id"),
            inverseJoinColumns = @JoinColumn(name = "mechanic_id")
    )
    private List<Mechanic> mechanics = new ArrayList<>();

    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkOrderItem> items = new ArrayList<>();
}
