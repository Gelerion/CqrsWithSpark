package com.gelerion.cqrs.spark.template.forum.domain.repository;

import com.gelerion.cqrs.spark.template.forum.domain.infrastructure.ReadOnlyRepository;
import com.gelerion.cqrs.spark.template.forum.domain.model.Post;
import org.springframework.stereotype.Repository;

/**
 * Created by denis.shuvalov on 28/01/2018.
 */
@Repository
public interface PostRepository extends ReadOnlyRepository<Post, String> {
}
