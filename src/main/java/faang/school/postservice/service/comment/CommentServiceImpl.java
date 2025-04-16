package faang.school.postservice.service.comment;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.dto.CommentDto;
import faang.school.postservice.mapper.CommentMapper;
import faang.school.postservice.model.Comment;
import faang.school.postservice.model.Post;
import faang.school.postservice.repository.CommentRepository;
import faang.school.postservice.repository.PostRepository;
import faang.school.postservice.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserServiceClient userServiceClient;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;

    public CommentDto createComment(CommentDto commentDto) {
        if (userServiceClient.getUser(commentDto.getAuthorId()) == null) {
            throw new EntityNotFoundException("Нет такого пользователя");
        }
        if (commentDto.getContent().length() > 4096) {
            throw new IllegalArgumentException("Слишком много символов");
        }

        Post post = postRepository.findById(commentDto.getPostId()).orElseThrow(
                () -> new EntityNotFoundException("Post not found"));

        Comment comment = commentMapper.toEntity(commentDto);
        post.getComments().add(comment);

        postRepository.save(post);

        return commentMapper.toDto(commentRepository.save(comment));
    }

    public CommentDto updateComment(CommentDto commentDto) {
        Comment commentUpdate = commentRepository.findById(commentDto.getId()).orElseThrow(
                () -> new EntityNotFoundException("Нет такого комментария"));

        commentUpdate.setContent(commentDto.getContent());
        Comment updateComment = commentRepository.save(commentUpdate);
        return commentMapper.toDto(updateComment);
    }

    public List<CommentDto> getCommentByPostId(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);

        return comments.stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .map(commentMapper::toDto)
                .toList();
    }

    public void deleteComment(CommentDto commentDto) {
        if (!commentRepository.existsById(commentDto.getId())) {
            throw new EntityNotFoundException("Комментарий не найден");
        }
        commentRepository.delete(commentMapper.toEntity(commentDto));
    }
}
