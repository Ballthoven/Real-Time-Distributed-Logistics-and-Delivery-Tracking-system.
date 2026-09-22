package com.FinalYearProject.AssignmentService.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Table(name = "delivery_agents")
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    private String name;

    private String phone;

    private boolean available;

    private Double currentLatitude;
    private Double currentLongitude;
}
