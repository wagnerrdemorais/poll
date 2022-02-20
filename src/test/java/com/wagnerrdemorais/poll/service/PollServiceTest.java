package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.model.PollOption;
import com.wagnerrdemorais.poll.repository.OptionRepository;
import com.wagnerrdemorais.poll.repository.PollRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PollServiceTest {

    PollService subject;
    PollRepository pollRepository;
    OptionRepository optionRepository;
    Map<Long, Poll> pollMap = new LinkedHashMap<>();

    @BeforeEach
    void setUp() {
        pollRepository = Mockito.mock(PollRepository.class);
        optionRepository = Mockito.mock(OptionRepository.class);

        pollMap.putAll(Map.of(
                1L,
                new Poll(1L, "Poll1", "Poll1 Description",
                        List.of(new PollOption(1L, "Option1", new ArrayList<>()),
                                new PollOption(2L, "Option2", new ArrayList<>()))),
                2L,
                new Poll(2L, "Poll2", "Poll2 Description",
                        List.of(new PollOption(3L, "Option3", new ArrayList<>()),
                                new PollOption(4L, "Option4", new ArrayList<>())))
        ));

        this.subject = new PollService(pollRepository, optionRepository);

        mockRepoFindAll();
        mockRepoGetById();
        mockRepoSave();
        mockRepoDeleteById();
        mockRepoExistsById();
    }

    @Test
    void givenTwoPolls_whenGetPollList_shouldReturnTwoPolls() {
        List<Poll> pollList = subject.getPollList();
        verify(pollRepository, times(1)).findAll();
        assertEquals(2, pollList.size());
    }

    @Test
    void givenPollList_whenGetPollById_shouldReturnCorrespondingPoll() {
        Poll pollById = subject.getPollById(1L);
        assertEquals("Poll1", pollById.getTitle());
        assertEquals("Poll1 Description", pollById.getDescription());

        assertEquals(2, pollById.getOptionList().size());
        assertEquals("Option1", pollById.getOptionList().get(0).getTitle());

        assertEquals("Option2", pollById.getOptionList().get(1).getTitle());
    }

    @Test
    void givenPollList_whenSavePoll_shouldReturnSavedPoll() {
        Poll pollToSave = new Poll(5L, "Test", "TestDescription",
                List.of(new PollOption(5L, "ChooseTest", new ArrayList<>()),
                        new PollOption(6L, "Option2", new ArrayList<>())));

        Poll savedPoll = subject.savePoll(pollToSave);
        assertEquals(pollToSave, savedPoll);

        Poll pollById = subject.getPollById(5L);
        assertEquals(pollToSave, pollById);
    }

    @Test
    void givenPollList_andNewPoll_whenDeletePollById_shouldDeleteAccordingly() {
        Poll pollToSave = new Poll(5L, "Test", "TestDescription",
                List.of(new PollOption(5L, "ChooseTest", new ArrayList<>()),
                        new PollOption(6L, "Option2", new ArrayList<>())));
        subject.savePoll(pollToSave);

        Poll pollById = subject.getPollById(5L);
        assertEquals(pollToSave, pollById);
        assertEquals(3L, subject.getPollList().size());

        subject.deleteById(5L);
        assertEquals(2L, subject.getPollList().size());

        Poll pollById1 = subject.getPollById(5L);
        assertNull(pollById1);

        subject.deleteById(1L);
        assertEquals(1L, subject.getPollList().size());
    }

    @Test
    void givenTwoPolls_whenRunPollExistsById_shouldReturnTrueForExisting_andFalseIfPollDoesNotExists() {
        assertTrue(subject.pollExistsById(1L));
        assertFalse(subject.pollExistsById(3L));
    }

    private void mockRepoFindAll() {
        when(pollRepository.findAll()).thenAnswer(invocationOnMock -> new ArrayList<>(pollMap.values()));
    }

    private void mockRepoGetById() {
        when(pollRepository.getById(Mockito.anyLong())).thenAnswer(invocationOnMock -> {
            Long argument = (Long) invocationOnMock.getArguments()[0];
            return pollMap.get(argument);
        });
    }

    private void mockRepoSave() {
        when(pollRepository.save(Mockito.any(Poll.class))).thenAnswer(invocationOnMock -> {
            Poll argument = (Poll) invocationOnMock.getArguments()[0];
            pollMap.put(argument.getId(), argument);
            return argument;
        });
    }

    private void mockRepoDeleteById() {
        doAnswer(invocationOnMock -> {
            Long id = (Long) invocationOnMock.getArguments()[0];
            pollMap.remove(id);
            return null;
        }).when(pollRepository).deleteById(Mockito.anyLong());
    }

    private void mockRepoExistsById() {
        when(pollRepository.existsById(Mockito.any(Long.class))).thenAnswer(invocationOnMock -> {
            Long argument = (Long) invocationOnMock.getArguments()[0];
            return pollMap.containsKey(argument);
        });
    }
}