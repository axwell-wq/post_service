package faang.school.postservice.dto;

import faang.school.postservice.model.Post;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @NotNull
    private Long id;

    private String content;
    private Long authorId;

    @NotNull
    private Long postId;
    private List<LikeDto> likes;
}
