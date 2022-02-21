package com.wagnerrdemorais.poll.repository;

import com.wagnerrdemorais.poll.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Vote entity
 */
@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
}
