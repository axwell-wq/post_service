package faang.school.postservice.service.post;

import faang.school.postservice.client.ProjectServiceClient;
import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.config.context.ThreadPoolConfig;
import faang.school.postservice.dto.CommentDto;
import faang.school.postservice.dto.LikeDto;
import faang.school.postservice.dto.PostDto;
import faang.school.postservice.mapper.CommentMapper;
import faang.school.postservice.mapper.LikeMapper;
import faang.school.postservice.mapper.PostMapper;
import faang.school.postservice.model.Post;
import faang.school.postservice.repository.PostRepository;
import faang.school.postservice.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserServiceClient userClient;
    private final ProjectServiceClient projectClient;
    private final PostMapper postMapper;
    private final LikeMapper likeMapper;
    private final CommentMapper commentMapper;
    private final Executor threadPool;

    public PostDto createDraft(PostDto postDto) {
        checkOwnerPost(postDto);

        Post post = postMapper.toEntity(postDto);
        post.setPublished(false);

        return postMapper.toDto(postRepository.save(post));
    }

    public PostDto createPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Нет такого черновика"));

        if (post.getPublishedAt() == null) {
            post.setPublished(true);
        }

        Post savePost = postRepository.save(post);
        return postMapper.toDto(savePost);
    }

    public PostDto updatePost(PostDto postDto) {
        Post postUpdate = postRepository.findById(postDto.getId()).orElseThrow(
                () -> new EntityNotFoundException("Нет такого поста"));

        postUpdate.setContent(postDto.getContent());
        Post updatedPost = postRepository.save(postUpdate);
        return postMapper.toDto(updatedPost);
    }

    public void softDeletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Нет поста")); {

            post.setDeleted(true);
            postRepository.save(post);
        }
    }

    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Нет такого поста"));

        return mapLikesAndComments(post);
    }

    public List<PostDto> getAllBlackPostsByAuthorId(Long authorId) {
        List<Post> posts = postRepository.findAllByAuthorId(authorId);

        return getPostDtos(posts);
    }

    public List<PostDto> getAllBlackProjectsByAuthorId(Long projectId) {
        List<Post> posts = postRepository.findByProjectId(projectId);

        return getPostDtos(posts);
    }

    public List<PostDto> getAllPublicPostsByAuthorId(Long authorId) {
        List<Post> posts = postRepository.findAllByAuthorId(authorId);

        return getPostPublic(posts);
    }

    public List<PostDto> getAllPublicProjectsByAuthorId(Long projectId) {
        List<Post> posts = postRepository.findByProjectId(projectId);

        return getPostPublic(posts);
    }

    private List<PostDto> getPostDtos(List<Post> posts) {
        List<Post> outPosts = new ArrayList<>();

        for (Post post : posts) {
            if (!post.isPublished()) {
                outPosts.add(post);
            }
        }

        return outPosts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt))
                .map(post -> mapLikesAndComments(post))
                .toList();
    }

    private List<PostDto> getPostPublic(List<Post> posts) {
        List<Post> outPosts = new ArrayList<>();

        for (Post post : posts) {
            if (post.isPublished()) {
                outPosts.add(post);
            }
        }

        return outPosts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt))
                .map(post -> mapLikesAndComments(post))
                .toList();
    }

    private void checkOwnerPost(PostDto postDto) {
        if (postDto.getAuthorId() != null) {
            checkAuthorPost(postDto);
        } else if (postDto.getProjectId() != null) {
            checkProjectPost(postDto);
        }
    }

    private void checkAuthorPost(PostDto postDto) {
        if (userClient.getUser(postDto.getAuthorId()) == null) {
            throw new EntityNotFoundException("Нет такого пользователя");
        }
    }

    private void checkProjectPost(PostDto postDto) {
        if (projectClient.getProject(postDto.getProjectId()) == null) {
            throw new EntityNotFoundException("Проект не существует");
        }
    }

    private PostDto mapLikesAndComments(Post post) {
        List<LikeDto> likeDtoList = post.getLikes().stream()
                .map(like -> likeMapper.toDto(like))
                .toList();

        List<CommentDto> commentDtoList = post.getComments().stream()
                .map(comment -> commentMapper.toDto(comment))
                .toList();

        PostDto postDto = postMapper.toDto(post);
        postDto.setLikes(likeDtoList);
        postDto.setComments(commentDtoList);

        return postDto;
    }
}
