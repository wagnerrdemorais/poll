package com.wagnerrdemorais.pool.controller;

import com.wagnerrdemorais.pool.form.PollForm;
import com.wagnerrdemorais.pool.model.Poll;
import com.wagnerrdemorais.pool.service.PoolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pool")
public class PollController {

    private final PoolService poolService;

    public PollController(PoolService poolService) {
        this.poolService = poolService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Poll>> getPollList() {
        List<Poll> poolList = poolService.getPoolList();
        return ResponseEntity.ok(poolList);
    }

    @PostMapping("/add")
    public ResponseEntity<Poll> addPoll(@RequestBody PollForm poll) {
        Poll poll1 = poolService.savePoll(poll.toEntity());
        return ResponseEntity.ok(poll1);
    }

}
