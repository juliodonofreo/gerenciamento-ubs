package com.ubs.ubs.dtos;

import com.ubs.ubs.entities.enums.ReminderTimeUnit;

public class ReminderConfigDto {
    private boolean reminderEnabled;
    private Integer reminderLeadTimeValue;
    private ReminderTimeUnit reminderLeadTimeUnit;
    private boolean useCustomReminderTemplate;
    private String customReminderTemplate;

    public ReminderConfigDto(){

    }

    public ReminderConfigDto(boolean reminderEnabled, Integer reminderLeadTimeValue, ReminderTimeUnit reminderLeadTimeUnit, boolean useCustomReminderTemplate, String customReminderTemplate) {
        this.reminderEnabled = reminderEnabled;
        this.reminderLeadTimeValue = reminderLeadTimeValue;
        this.reminderLeadTimeUnit = reminderLeadTimeUnit;
        this.useCustomReminderTemplate = useCustomReminderTemplate;
        this.customReminderTemplate = customReminderTemplate;
    }

    public boolean isReminderEnabled() {
        return reminderEnabled;
    }

    public void setReminderEnabled(boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }

    public Integer getReminderLeadTimeValue() {
        return reminderLeadTimeValue;
    }

    public void setReminderLeadTimeValue(Integer reminderLeadTimeValue) {
        this.reminderLeadTimeValue = reminderLeadTimeValue;
    }

    public boolean isUseCustomReminderTemplate() {
        return useCustomReminderTemplate;
    }

    public void setUseCustomReminderTemplate(boolean useCustomReminderTemplate) {
        this.useCustomReminderTemplate = useCustomReminderTemplate;
    }

    public ReminderTimeUnit getReminderLeadTimeUnit() {
        return reminderLeadTimeUnit;
    }

    public void setReminderLeadTimeUnit(ReminderTimeUnit reminderLeadTimeUnit) {
        this.reminderLeadTimeUnit = reminderLeadTimeUnit;
    }

    public String getCustomReminderTemplate() {
        return customReminderTemplate;
    }

    public void setCustomReminderTemplate(String customReminderTemplate) {
        this.customReminderTemplate = customReminderTemplate;
    }
}