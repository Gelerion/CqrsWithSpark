package com.gelerion.cqrs.spark.template.forum.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by denis.shuvalov on 28/01/2018.
 */
@EnableMongoRepositories
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${mongo.db}")
    private String mongodb;

    @Value("${mongo.host}")
    private String host;

    @Override
    protected String getDatabaseName() {
        return mongodb;
    }

    @Override
    public Mongo mongo() {
        return new MongoClient(host);
    }

}
