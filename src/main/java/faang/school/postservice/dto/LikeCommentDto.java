package faang.school.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeCommentDto {
    private Long id;
    private Long userId;
    private Long commentId;
}
