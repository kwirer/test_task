package com.example.test.repository;

import com.example.test.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {
    Set<Match> findAllByTournamentId(UUID tournamentId);
}