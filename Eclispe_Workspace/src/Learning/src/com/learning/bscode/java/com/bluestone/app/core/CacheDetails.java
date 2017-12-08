package com.bluestone.app.core;


public class CacheDetails {

    private String name;
    private long cacheHits;
    private long totalSize;
    private long avgSize;
    private long totalElements;
    private long cacheMisses;
    
    public CacheDetails(String name, long cacheHits, long totalSize, long avgSize, long totalElements, long cacheMisses) {
        super();
        this.name = name;
        this.cacheHits = cacheHits;
        this.totalSize = totalSize;
        this.avgSize = avgSize;
        this.totalElements = totalElements;
        this.cacheMisses = cacheMisses;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CacheDetails [name=");
        builder.append(name);
        builder.append(", cacheHits=");
        builder.append(cacheHits);
        builder.append(", cacheMisses=");
        builder.append(cacheMisses);
        builder.append(", totalSize=");
        builder.append(totalSize);
        builder.append(", avgSize=");
        builder.append(avgSize);
        builder.append(", totalElements=");
        builder.append(totalElements);
        builder.append("]");
        return builder.toString();
    }

    public String getName() {
        return name;
    }

    public long getCacheHits() {
        return cacheHits;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public long getAvgSize() {
        return avgSize;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public long getCacheMisses() {
        return cacheMisses;
    }
}
