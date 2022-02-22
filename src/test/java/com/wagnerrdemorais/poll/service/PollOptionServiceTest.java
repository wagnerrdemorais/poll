package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.model.PollOption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
class PollOptionServiceTest extends PollRepoTestHelper {

    PollOptionService subject;

    @BeforeEach
    void setUp() {
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

        this.subject = new PollOptionService(optionRepository);
    }

    @Test
    void getById() {
        PollOption byId = subject.getById(3L);
        Assertions.assertEquals(3L, byId.getId());
    }

    @Test
    void save() {

    }
}