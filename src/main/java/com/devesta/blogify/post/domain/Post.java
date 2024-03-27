package com.devesta.blogify.post.domain;

import com.devesta.blogify.comment.domain.Comment;
import com.devesta.blogify.community.domain.Community;
import com.devesta.blogify.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter
@Setter
@ToString
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

    private String body;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer votes = 0;

    @UpdateTimestamp
    private LocalDate lastUpdate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "posted_in_community_id")
    @ToString.Exclude
    private Community community;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private Set<Comment> comments;

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdate = LocalDate.now();
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
