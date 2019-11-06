package signal9.springboot.restdoc.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Post {
    private String subject;
    private String content;

    @Builder
    public Post(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }
}
