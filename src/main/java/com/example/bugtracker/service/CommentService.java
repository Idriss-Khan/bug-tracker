package com.example.bugtracker.service;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.model.Comment;
import com.example.bugtracker.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;


    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsForBug(Bug bug) {
        return commentRepository.findByBugOrderByCreatedAtDesc(bug);
    }

    public Comment getCommentById(Integer commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElse(null);
    }


}