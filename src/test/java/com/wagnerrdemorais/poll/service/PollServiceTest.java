package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.model.PollOption;
import com.wagnerrdemorais.poll.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PollServiceTest extends RepoTestHelper {

    PollService subject;

    @BeforeEach
    void setUp() {
        createsInitialDataForPollRepo();
        this.subject = new PollService(super.pollRepository, super.optionRepository, super.userRepository);
    }

    @Test
    void givenTwoPolls_whenGetPollList_shouldReturnTwoPolls() {
        List<Poll> pollList = subject.getPollList();
        verify(super.pollRepository, times(1)).findAll();
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
                        new PollOption(6L, "Option2", new ArrayList<>())),
                null);

        Poll savedPoll = subject.savePoll(pollToSave);
        assertEquals(pollToSave, savedPoll);

        Poll pollById = subject.getPollById(5L);
        assertEquals(pollToSave, pollById);
    }

    @Test
    void givenPollList_andNewPoll_whenDeletePollById_shouldDeleteAccordingly() {
        Poll pollToSave = new Poll(5L, "Test", "TestDescription",
                List.of(new PollOption(5L, "ChooseTest", new ArrayList<>()),
                        new PollOption(6L, "Option2", new ArrayList<>())),
                null);
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

    @Test
    void givenPollWithUser_thenCheckIfPollHasUser_thenShouldReturnTrue() {
        assertTrue(subject.hasUser(1L));
    }

    @Test
    void givenPollWithoutUser_thenCheckIfPollHasUser_thenShouldReturnFalse() {
        assertFalse(subject.hasUser(2L));
    }

    //TODO mock findAllByuserId in repo
//    @Test
    void givenPollWithUser_whenGetPollListByUser_thenShouldReturnPollList() {
        createsInitialDataForPollRepo();
        List<Poll> poolListByUserId = subject.findAllByUserId(1L);
        assertEquals(1L, poolListByUserId.size());
    }

    /**
     * Initializes pollMap with test data
     */
    private void createsInitialDataForPollRepo() {
        super.pollMap.putAll(Map.of(
                1L,
                new Poll(1L, "Poll1", "Poll1 Description",
                        List.of(new PollOption(1L, "Option1", new ArrayList<>()),
                                new PollOption(2L, "Option2", new ArrayList<>())),
                        new User(1L, "user", "user")),
                2L,
                new Poll(2L, "Poll2", "Poll2 Description",
                        List.of(new PollOption(3L, "Option3", new ArrayList<>()),
                                new PollOption(4L, "Option4", new ArrayList<>())),
                        null)
        ));
    }
}