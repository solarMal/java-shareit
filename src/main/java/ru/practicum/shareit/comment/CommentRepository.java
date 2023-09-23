package ru.practicum.shareit.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.comment.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByItemIdOrderByCreated(Integer itemId);
}