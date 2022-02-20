package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.model.PollOption;
import com.wagnerrdemorais.poll.repository.OptionRepository;
import org.springframework.stereotype.Service;

@Service
public class PollOptionService {

    private final OptionRepository optionRepository;

    public PollOptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }


    public PollOption getById(Long id) {
        return optionRepository.getById(id);
    }

    public PollOption save(PollOption pollOption) {
        return optionRepository.save(pollOption);
    }
}
