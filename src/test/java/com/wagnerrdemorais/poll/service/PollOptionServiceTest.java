package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.model.PollOption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Map;

@Transactional
class PollOptionServiceTest extends PollRepoTestHelper {

    PollOptionService subject;

    @BeforeEach
    void setUp() {
        optionMap.putAll(Map.of(1L, new PollOption(1L, "Option1", new ArrayList<>()),
                2L, new PollOption(2L, "Option2", new ArrayList<>())));

        this.subject = new PollOptionService(optionRepository);
    }

    @Test
    void givenPollWithId2_whenGetGyId_thenShouldReturnCorrespondingPoll() {
        PollOption byId = subject.getById(2L);
        Assertions.assertEquals(2L, byId.getId());
    }

    @Test
    void givenNewPoll_whenSave_thenPollShouldBeSaved() {
        PollOption option = new PollOption(1L, "Option1", new ArrayList<>());
        PollOption save = subject.save(option);
        Assertions.assertEquals(1L, save.getId());
    }
}