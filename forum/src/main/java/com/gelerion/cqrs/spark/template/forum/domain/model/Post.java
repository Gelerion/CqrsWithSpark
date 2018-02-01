package com.gelerion.cqrs.spark.template.forum.domain.model;

import lombok.Value;
import org.springframework.data.annotation.Id;

/**
 * Created by denis.shuvalov on 28/01/2018.
 */
@Value
public class Post {

    @Id
    String id;

    String date;

    String author;

    String message;

}
