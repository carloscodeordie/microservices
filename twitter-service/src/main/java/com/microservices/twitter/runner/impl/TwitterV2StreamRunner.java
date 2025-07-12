package com.microservices.twitter.runner.impl;

import com.microservices.twitter.config.TwitterServiceConfigData;
import com.microservices.twitter.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnExpression("${twitter-service.enable-v2-tweets} && not ${twitter-service.enable-mock-tweets}")
public class TwitterV2StreamRunner implements StreamRunner {

    Logger LOG = LoggerFactory.getLogger(TwitterV2StreamRunner.class);

    private final TwitterServiceConfigData twitterServiceConfigData;
    private final TwitterStreamHelper twitterStreamHelper;

    public TwitterV2StreamRunner(TwitterServiceConfigData configData, TwitterStreamHelper streamHelper) {
        this.twitterServiceConfigData = configData;
        this.twitterStreamHelper = streamHelper;
    }

    @Override
    public void start() {
        String bearerToken = twitterServiceConfigData.getTwitterV2BearerToken();
        if (bearerToken != null) {
            try {
                twitterStreamHelper.setupRules(bearerToken, getRules());
                twitterStreamHelper.connectStream(bearerToken);
            } catch(IOException | URISyntaxException e) {
                LOG.error("Error when streaming tweets: ", e);
                throw new RuntimeException("Error when streaming tweets: ", e);
            }

        } else {
            LOG.error("Twitter Bearer token is empty");
            throw new RuntimeException("Twitter Bearer token is empty");
        }
    }

    private Map<String, String> getRules() {
        List<String> keywords = twitterServiceConfigData.getTwitterKeywords();
        Map<String, String> rules = new HashMap<>();
        for (String keyword : keywords) {
            rules.put(keyword, "Keyword: " + keyword);
        }
        return rules;
    }
}
