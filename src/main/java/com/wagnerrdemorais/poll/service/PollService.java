package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.repository.PollRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollService {

    private final PollRepository pollRepository;

    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public List<Poll> getPollList() {
        return this.pollRepository.findAll();
    }

    public Poll getPollById(Long id) {
        return this.pollRepository.getById(id);
    }

    public Poll savePoll(Poll poll) {
        return this.pollRepository.save(poll);
    }

    public void deletePoll(Poll poll) {
        this.pollRepository.delete(poll);
    }

}
