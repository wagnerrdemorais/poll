package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.model.PollOption;
import com.wagnerrdemorais.poll.repository.PollRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class PollServiceTest {

    PollService subject;
    PollRepository pollRepository;
    Map<Long, Poll> pollMap;

    @BeforeEach
    void setUp() {
        pollRepository = Mockito.mock(PollRepository.class);

        pollMap = Map.of(
                1L,
                new Poll(1L, "Poll1", "Poll1 Description",
                        List.of(new PollOption(1L, "Option1", 1),
                                new PollOption(2L, "Option2", 2))),
                2L,
                new Poll(2L, "Poll2", "Poll2 Description",
                        List.of(new PollOption(3L, "Option3", 3),
                                new PollOption(4L, "Option4", 4)))
        );

        mockRepoFindAll();
        mockRepoFindById();
        mockRepoSave();

        this.subject = new PollService(pollRepository);
    }

    @Test
    void getPollList() {
    }

    @Test
    void getPollById() {
    }

    @Test
    void savePoll() {
    }

    private void mockRepoFindAll() {
        List<Poll> pollList = new ArrayList<>(pollMap.values());
        Mockito.when(pollRepository.findAll()).thenReturn(pollList);
    }

    private void mockRepoFindById() {
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(pollRepository).getById(idCaptor.capture());

        Poll poll = pollMap.get(idCaptor.getValue());

        Mockito.when(pollRepository.getById(idCaptor.getValue())).thenReturn(poll);
    }

    private void mockRepoSave() {
        ArgumentCaptor<Poll> pollCaptor = ArgumentCaptor.forClass(Poll.class);
        Mockito.verify(pollRepository).save(pollCaptor.capture());
        pollMap.put(pollCaptor.getValue().getId(), pollCaptor.getValue());
    }
}