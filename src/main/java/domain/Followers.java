package domain;


import lombok.Data;

import javax.persistence.*;


@Data
@Entity(name = "followers")
public class Followers {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long followerId;

    @Column(nullable = false)
    private Long followeeId;

}
