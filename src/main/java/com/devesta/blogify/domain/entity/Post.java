package com.devesta.blogify.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "post")
@Getter
@Setter
@ToString(exclude = "comments")
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "post_generator"
    )
    @SequenceGenerator(name = "post_generator",
            sequenceName = "post_seq",
            allocationSize = 2
    )
    @Column(name = "post_id")
    private Long postId;

    private String title;

    private String body; //  todo add image

    private Integer votes = 0;

    @ManyToOne
    @JoinColumn(
            name = "author_id",
            referencedColumnName = "userId"
    )
    private User author;

    @ManyToOne
    @JoinColumn(
            name = "posted_in_community_id",
            referencedColumnName = "communityId"
    )
    private Community community;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdate = LocalDateTime.now();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Post post = (Post) o;
        return getPostId() != null && Objects.equals(getPostId(), post.getPostId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
