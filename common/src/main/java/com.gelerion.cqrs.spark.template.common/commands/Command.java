package com.gelerion.cqrs.spark.template.common.commands;

/**
 * Created by denis.shuvalov on 16/01/2018.
 */
public interface Command {

    String id();

    String type();

}
