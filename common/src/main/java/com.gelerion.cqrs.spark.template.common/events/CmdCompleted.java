package com.gelerion.cqrs.spark.template.common.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

/**
 * Created by denis.shuvalov on 28/01/2018.
 */
@JsonDeserialize(builder = CmdCompleted.CmdCompletedBuilder.class)
@Value
@Builder
public class CmdCompleted implements Event<CmdCompleted> {

    String id;
    boolean succeeded;
    String error;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class CmdCompletedBuilder {
    }
}
