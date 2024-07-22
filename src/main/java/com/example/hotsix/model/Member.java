package com.example.hotsix.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
}
