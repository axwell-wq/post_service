package faang.school.postservice.service.like;

import faang.school.postservice.dto.LikeCommentDto;
import faang.school.postservice.dto.LikeDto;
import faang.school.postservice.dto.LikePostDto;
import faang.school.postservice.mapper.LikeCommentMapper;
import faang.school.postservice.mapper.LikeMapper;
import faang.school.postservice.mapper.LikePostMapper;
import faang.school.postservice.model.Comment;
import faang.school.postservice.model.Like;
import faang.school.postservice.model.Post;
import faang.school.postservice.repository.CommentRepository;
import faang.school.postservice.repository.LikeRepository;
import faang.school.postservice.repository.PostRepository;
import faang.school.postservice.service.LikeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeCommentMapper likeCommentMapper;
    private final LikePostMapper likePostMapper;

    public void likePost(LikePostDto likeDto) {
        Post post = postRepository.findById(likeDto.getPostId()).orElseThrow(
                () -> new EntityNotFoundException("Нет такого поста"));

        Like existLike = likeRepository.findByPostIdAndUserId(likeDto.getPostId(),
                likeDto.getUserId()).orElse(null);

        if (existLike != null) {
            throw new EntityNotFoundException("Вы уже поставили лайк");
        }

        Like like = likePostMapper.toEntity(likeDto);
        like.setPost(post);
        likeRepository.save(like);

        post.getLikes().add(like);
        postRepository.save(post);
    }

    public void deleteLikePost(LikeDto likeDto) {
        likeRepository.deleteByPostIdAndUserId(likeDto.getPostId(), likeDto.getUserId());
    }

    public void likeComment(LikeCommentDto likeDto) {
        Comment comment = commentRepository.findById(likeDto.getCommentId()).orElseThrow(
                () -> new EntityNotFoundException("Нет такого комментария"));

        Like existComment = likeRepository.findByCommentIdAndUserId(likeDto.getCommentId(),
                likeDto.getUserId()).orElse(null);

        if (existComment != null) {
            throw new EntityNotFoundException("Вы уже поставили лайк");
        }

        Like like = likeCommentMapper.toEntity(likeDto);
        like.setComment(comment);
        likeRepository.save(like);

        comment.getLikes().add(like);
        commentRepository.save(comment);

    }

    public void deleteLikeComment(LikeDto likeDto) {
        likeRepository.deleteByCommentIdAndUserId(likeDto.getCommentId(), likeDto.getUserId());
    }
}
