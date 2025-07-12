package com.microservices.twitter.runner.impl;

import com.microservices.twitter.config.TwitterServiceConfigData;
import com.microservices.twitter.listeners.TwitterStatusListener;
import com.microservices.twitter.runner.StreamRunner;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.Arrays;

@Component
public class TwitterStreamRunner implements StreamRunner {

    private final TwitterServiceConfigData twitterServiceConfigData;
    private final TwitterStatusListener twitterStatusListener;

    private TwitterStream twitterStream;

    private static final Logger LOG =  LoggerFactory.getLogger(TwitterStreamRunner.class);

    public TwitterStreamRunner(TwitterServiceConfigData configData, TwitterStatusListener statusListener) {
        twitterServiceConfigData = configData;
        twitterStatusListener = statusListener;
    }

    @Override
    public void start() throws TwitterException {
        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(twitterStatusListener);
        addFilter();
    }

    @PreDestroy
    public void shutdown() {
        if(twitterStream != null) {
            LOG.info("Shutting down twitter stream");
            twitterStream.shutdown();
        }
    }

    private void addFilter() {
        String[] keywords = twitterServiceConfigData.getTwitterKeywords().toArray(new String[0]);
        FilterQuery filterQuery = new FilterQuery(keywords);
        twitterStream.filter(filterQuery);
        LOG.info("Starting twitter stream with keywords: {}", Arrays.toString(keywords));
    }
}
