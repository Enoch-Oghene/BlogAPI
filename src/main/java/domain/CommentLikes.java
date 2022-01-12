package domain;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "commentLikes")
public class CommentLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postLikeId;

    @ManyToOne
    private Comment comment;

    @ManyToOne
    private User user;

}
