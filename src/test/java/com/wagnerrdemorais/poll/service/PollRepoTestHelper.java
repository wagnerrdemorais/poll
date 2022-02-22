package com.wagnerrdemorais.poll.service;

import com.wagnerrdemorais.poll.model.Poll;
import com.wagnerrdemorais.poll.model.PollOption;
import com.wagnerrdemorais.poll.repository.OptionRepository;
import com.wagnerrdemorais.poll.repository.PollRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class PollRepoTestHelper {

    @Resource
    final PollRepository pollRepository;

    @Resource
    final OptionRepository optionRepository;
    final Map<Long, Poll> pollMap = new LinkedHashMap<>();

    public PollRepoTestHelper() {
        this.pollRepository = Mockito.mock(PollRepository.class);
        this.optionRepository = Mockito.mock(OptionRepository.class);
        mockPollRepo();
        mockPollOptionRepo();
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
    }

    private void mockOptionRepoGetById() {
        when(optionRepository.getById(Mockito.anyLong())).thenAnswer(invocationOnMock -> {
            Long argument = (Long) invocationOnMock.getArguments()[0];
            AtomicReference<PollOption> pollOption = new AtomicReference<>();
            pollMap.values().forEach(poll -> poll.getOptionList().forEach(po -> {
                if (po.getId().equals(argument)) {
                    pollOption.set(po);
                }
            }));
            return pollOption.get();
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
