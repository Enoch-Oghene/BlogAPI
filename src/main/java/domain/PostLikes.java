package domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "likes")
public class PostLikes implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postLikeId;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User user;
}
