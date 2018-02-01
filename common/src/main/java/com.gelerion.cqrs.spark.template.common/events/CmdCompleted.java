package com.gelerion.cqrs.spark.template.common.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Created by denis.shuvalov on 28/01/2018.
 */
@Value
@Builder
@AllArgsConstructor //mandatory for jackson
public class CmdCompleted implements Event<CmdCompleted> {

    String id;
    boolean succeeded;
    String error;

}
