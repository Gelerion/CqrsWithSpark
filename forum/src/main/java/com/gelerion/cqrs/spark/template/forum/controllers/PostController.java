package com.gelerion.cqrs.spark.template.forum.controllers;

import com.gelerion.cqrs.spark.template.common.commands.impl.AddPost;
import com.gelerion.cqrs.spark.template.common.events.CmdCompleted;
import com.gelerion.cqrs.spark.template.common.message.bus.MessageBus;
import com.gelerion.cqrs.spark.template.forum.domain.model.Post;
import com.gelerion.cqrs.spark.template.forum.domain.repository.PostRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by denis.shuvalov on 28/01/2018.
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;
    private final MessageBus messageBus;

    public PostController(PostRepository postRepository, MessageBus messageBus) {
        this.postRepository = postRepository;
        this.messageBus = messageBus;
    }

    @GetMapping
    public Iterable<Post> getAll() {
        return postRepository.findAll();
    }

    @PostMapping
    public DeferredResult<Post> create(@RequestBody AddPost newPost) {
        DeferredResult<Post> result = new DeferredResult<>();
        messageBus.send(newPost, cmdCompleted -> onCompleted(cmdCompleted.getId(), cmdCompleted, result));
        return result;
    }

    private void onCompleted(final String cmdId, final CmdCompleted cmdCompleted, final DeferredResult<Post> result) {
        if(cmdCompleted.isSucceeded()) result.setResult(postRepository.findOne(cmdId));
        else result.setErrorResult(new RuntimeException(cmdCompleted.getError()));

    }
}
