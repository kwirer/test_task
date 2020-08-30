package com.example.test.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Tournament extends BaseEntity {

    private int maxParticipants;

    private boolean finished;

    private UUID winner;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Set<UUID> participants = new HashSet<>();
}