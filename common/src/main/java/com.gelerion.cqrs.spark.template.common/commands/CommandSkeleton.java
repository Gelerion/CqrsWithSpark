package com.gelerion.cqrs.spark.template.common.commands;

import java.util.UUID;

/**
 * Created by denis.shuvalov on 16/01/2018.
 */
public abstract class CommandSkeleton implements Command {

    protected String id;

    protected CommandSkeleton() {
        this.id = UUID.randomUUID().toString();
    }

    protected CommandSkeleton(String id) {
        this.id = id;
    }

    @Override
    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
