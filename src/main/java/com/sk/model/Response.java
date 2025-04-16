package com.sk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Response {

    private Role role;
    @JsonProperty("Items")
    private List<Items> items;
    private String modelId;
    //private Metadata metadata;

    // Getters and Setters
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    /*public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }*/

    public static class Role {
        private String label;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }



    /*public static class Metadata {
        private String id;
        private String createdAt;
        private String systemFingerprint;
        private Usage usage;
        private Object refusal;
        private String finishReason;
        private List<Object> contentTokenLogProbabilities;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getSystemFingerprint() {
            return systemFingerprint;
        }

        public void setSystemFingerprint(String systemFingerprint) {
            this.systemFingerprint = systemFingerprint;
        }

        public Usage getUsage() {
            return usage;
        }

        public void setUsage(Usage usage) {
            this.usage = usage;
        }

        public Object getRefusal() {
            return refusal;
        }

        public void setRefusal(Object refusal) {
            this.refusal = refusal;
        }

        public String getFinishReason() {
            return finishReason;
        }

        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }

        public List<Object> getContentTokenLogProbabilities() {
            return contentTokenLogProbabilities;
        }

        public void setContentTokenLogProbabilities(List<Object> contentTokenLogProbabilities) {
            this.contentTokenLogProbabilities = contentTokenLogProbabilities;
        }
    }

    public static class Usage {
        private int outputTokenCount;
        private int inputTokenCount;
        private int totalTokenCount;
        private Map<String, Integer> outputTokenDetails;
        private Map<String, Integer> inputTokenDetails;

        public int getOutputTokenCount() {
            return outputTokenCount;
        }

        public void setOutputTokenCount(int outputTokenCount) {
            this.outputTokenCount = outputTokenCount;
        }

        public int getInputTokenCount() {
            return inputTokenCount;
        }

        public void setInputTokenCount(int inputTokenCount) {
            this.inputTokenCount = inputTokenCount;
        }

        public int getTotalTokenCount() {
            return totalTokenCount;
        }

        public void setTotalTokenCount(int totalTokenCount) {
            this.totalTokenCount = totalTokenCount;
        }

        public Map<String, Integer> getOutputTokenDetails() {
            return outputTokenDetails;
        }

        public void setOutputTokenDetails(Map<String, Integer> outputTokenDetails) {
            this.outputTokenDetails = outputTokenDetails;
        }

        public Map<String, Integer> getInputTokenDetails() {
            return inputTokenDetails;
        }

        public void setInputTokenDetails(Map<String, Integer> inputTokenDetails) {
            this.inputTokenDetails = inputTokenDetails;
        }
    }*/
}