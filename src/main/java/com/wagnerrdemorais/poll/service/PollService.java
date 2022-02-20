package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.controller.form.PollForm;
import com.wagnerrdemorais.poll.controller.form.PollOptionForm;
import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.model.PollOption;
import com.wagnerrdemorais.poll.repository.OptionRepository;
import com.wagnerrdemorais.poll.repository.PollRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollService {

    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;

    public PollService(PollRepository pollRepository, OptionRepository optionRepository) {
        this.pollRepository = pollRepository;
        this.optionRepository = optionRepository;
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

    public Poll savePoll(PollForm pollForm) {

        Poll poll;
        if (pollForm.getId() == null) {
            poll = new Poll();
        } else {
            poll = this.getPollById(pollForm.getId());
        }

        poll.setTitle(pollForm.getTitle());
        poll.setDescription(pollForm.getDescription());

        List<PollOption> optionList = updatePollOptions(pollForm.getOptionList(), poll);

        poll.setOptionList(optionList);

        return this.savePoll(poll);
    }

    public void deleteById(Long id) {
        this.pollRepository.deleteById(id);
    }

    public boolean pollExistsById(Long id) {
        return this.pollRepository.existsById(id);
    }

    private List<PollOption> updatePollOptions(List<PollOptionForm> optionFormList, Poll poll) {
        return optionFormList.stream()
                .map(opt -> {
                    PollOption pollOption;
                    if (opt.getId() == null) {
                        pollOption = new PollOption(null, opt.getTitle(), new ArrayList<>());
                        pollOption.setPoll(poll);
                    } else {
                        pollOption = optionRepository.getById(opt.getId());
                        pollOption.setTitle(opt.getTitle());
                    }
                    return optionRepository.save(pollOption);
                }).collect(Collectors.toList());
    }
}
