package com.devesta.blogify.repository;

import com.devesta.blogify.domain.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}