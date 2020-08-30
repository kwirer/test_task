package com.example.test.repository;

import com.example.test.entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
    int countByIdIn(Set<UUID> participantIds);

    int countByName(String name);
}