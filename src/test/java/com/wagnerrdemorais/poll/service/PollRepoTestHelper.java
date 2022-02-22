package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.model.PollOption;
import com.wagnerrdemorais.poll.model.Vote;
import com.wagnerrdemorais.poll.repository.OptionRepository;
import com.wagnerrdemorais.poll.repository.PollRepository;
import com.wagnerrdemorais.poll.repository.VoteRepository;
import org.mockito.Mockito;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class PollRepoTestHelper {

    @Resource
    final PollRepository pollRepository;

    @Resource
    final OptionRepository optionRepository;
    final VoteRepository voteRepository;
    final Map<Long, Poll> pollMap = new LinkedHashMap<>();
    final Map<Long, PollOption> optionMap = new LinkedHashMap<>();
    final Map<Long, Vote> voteMap = new LinkedHashMap<>();

    public PollRepoTestHelper() {
        this.pollRepository = Mockito.mock(PollRepository.class);
        this.optionRepository = Mockito.mock(OptionRepository.class);
        this.voteRepository = Mockito.mock(VoteRepository.class);
        mockPollRepo();
        mockPollOptionRepo();
        mockPollVoteRepo();
    }

    private void mockPollRepo() {
        mockPollRepoFindAll();
        mockPollRepoGetById();
        mockPollRepoSave();
        mockPollRepoDeleteById();
        mockPollRepoExistsById();
    }

    private void mockPollOptionRepo() {
        mockOptionRepoGetById();
        mockOptionRepoSave();
    }

    private void mockPollVoteRepo() {
        mockVoteSave();
        mockVoteGetById();
    }

    private void mockVoteSave() {
        when(voteRepository.save(Mockito.any(Vote.class))).thenAnswer(invocationOnMock -> {
            Vote argument = (Vote) invocationOnMock.getArguments()[0];
            voteMap.put(argument.getId(), argument);
            optionMap.put(argument.getPollOption().getId(), argument.getPollOption());
            return argument;
        });
    }

    private void mockVoteGetById() {
        when(voteRepository.getById(Mockito.anyLong())).thenAnswer(invocationOnMock -> {
            Long argument = (Long) invocationOnMock.getArguments()[0];
            return voteMap.get(argument);
        });
    }

    private void mockOptionRepoGetById() {
        when(optionRepository.getById(Mockito.anyLong())).thenAnswer(invocationOnMock -> {
            Long argument = (Long) invocationOnMock.getArguments()[0];
            return optionMap.get(argument);
        });
    }

    private void mockOptionRepoSave() {
        when(optionRepository.save(Mockito.any(PollOption.class))).thenAnswer(invocationOnMock -> {
            PollOption argument = (PollOption) invocationOnMock.getArguments()[0];
            optionMap.put(argument.getId(), argument);
            return argument;
        });
    }

    void mockPollRepoFindAll() {
        when(pollRepository.findAll()).thenAnswer(invocationOnMock -> new ArrayList<>(pollMap.values()));
    }

    void mockPollRepoGetById() {
        when(pollRepository.getById(Mockito.anyLong())).thenAnswer(invocationOnMock -> {
            Long argument = (Long) invocationOnMock.getArguments()[0];
            return pollMap.get(argument);
        });
    }

    void mockPollRepoSave() {
        when(pollRepository.save(Mockito.any(Poll.class))).thenAnswer(invocationOnMock -> {
            Poll argument = (Poll) invocationOnMock.getArguments()[0];
            pollMap.put(argument.getId(), argument);
            return argument;
        });
    }

    void mockPollRepoDeleteById() {
        doAnswer(invocationOnMock -> {
            Long id = (Long) invocationOnMock.getArguments()[0];
            pollMap.remove(id);
            return null;
        }).when(pollRepository).deleteById(Mockito.anyLong());
    }

    void mockPollRepoExistsById() {
        when(pollRepository.existsById(Mockito.any(Long.class))).thenAnswer(invocationOnMock -> {
            Long argument = (Long) invocationOnMock.getArguments()[0];
            return pollMap.containsKey(argument);
        });
    }

}
