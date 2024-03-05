package com.devesta.blogify.community.domain;

import com.devesta.blogify.tag.Tag;
import com.devesta.blogify.post.domain.Post;
import com.devesta.blogify.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "community")
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "community_generator"
    )
    @SequenceGenerator(name = "community_generator",
            sequenceName = "community_seq",
            allocationSize = 1
    )
    @Column(name = "community_id")
    private Long communityId;

    @Column(unique = true)
    private String name;

    private String description;

    @Column(name = "number_of_members" , columnDefinition = "INT DEFAULT 1")
    private Integer numberOfMembers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User createdBy;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "community_tag",
            joinColumns = @JoinColumn(
                    name = "community_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "tag_id"
            )
    )
    private List<Tag> tags;

    @OneToMany(mappedBy = "community" , cascade = CascadeType.REMOVE , orphanRemoval = true)
    private List<Post> posts;

}
