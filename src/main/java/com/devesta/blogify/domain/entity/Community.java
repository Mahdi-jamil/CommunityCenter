package com.devesta.blogify.domain.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(
            name = "creator_id",
            referencedColumnName = "userId"
    )
    private User createdBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "community_tag",
            joinColumns = @JoinColumn(
                    name = "community_id",
                    referencedColumnName = "communityId"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "tag_id",
                    referencedColumnName = "tagId"
            )
    )
    private List<Tag> tags;

    @OneToMany(mappedBy = "community")
    private List<Post> posts;

}
