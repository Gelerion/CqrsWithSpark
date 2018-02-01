package com.gelerion.cqrs.spark.template.common.commands.impl;

import com.gelerion.cqrs.spark.template.common.commands.CommandSkeleton;

/**
 * Created by denis.shuvalov on 16/01/2018.
 */
public class AddPost extends CommandSkeleton {
    public static final String TYPE = "AddPost";

    private String author;
    private String message;

    public String getAuthor() {
        return author;
    }

    public AddPost setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public AddPost setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getType() {
        return TYPE;
    }

    @Override
    public String type() {
        return TYPE;
    }
}
