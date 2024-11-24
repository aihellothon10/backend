package com.example.memoservice.domain.apiclient.diffbot.dto;

import lombok.Data;

import java.util.List;

@Data
public class DiffbotResponse {
    private PageInfo request;
    private List<ObjectInfo> objects;

    @Data
    public static class PageInfo {
        private String pageUrl;
        private String api;
        private int version;
    }

    @Data
    public static class ObjectInfo {
        private String date;
        private List<ImageInfo> images;
        private String estimatedDate;
        private String publisherRegion;
        private String icon;
        private String diffbotUri;
        private String siteName;
        private String type;
        private String title;
        private String publisherCountry;
        private String humanLanguage;
        private String pageUrl;
        private String html;
        private List<CategoryInfo> categories;
        private String text;
    }

    @Data
    public static class ImageInfo {
        private int naturalHeight;
        private int width;
        private String diffbotUri;
        private String url;
        private int naturalWidth;
        private boolean primary;
        private int height;
    }

    @Data
    public static class CategoryInfo {
        private double score;
        private String name;
        private String id;
    }
}



