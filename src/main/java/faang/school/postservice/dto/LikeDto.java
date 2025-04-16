package faang.school.postservice.dto;

import faang.school.postservice.model.Like;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDto {
    private Long id;
    private Long userId;
    private Long commentId;
    private Long postId;
}
