package faang.school.postservice.service;

import faang.school.postservice.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto);

    CommentDto updateComment(CommentDto commentDto);

    List<CommentDto> getCommentByPostId(Long postId);

    void deleteComment(CommentDto commentDto);


}
