package com.wagnerrdemorais.poll.repository;

import com.wagnerrdemorais.poll.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Poll entity
 */
@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
}
