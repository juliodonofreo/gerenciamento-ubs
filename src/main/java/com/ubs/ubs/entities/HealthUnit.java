package com.ubs.ubs.entities;

import com.ubs.ubs.entities.User;
import com.ubs.ubs.entities.enums.ReminderTimeUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("UNIT")
public class HealthUnit extends User {

    @NotBlank
    @Pattern(regexp = "\\d{10,11}")
    private String phone;

    @NotBlank
    @Size(max = 200)
    private String address;

    @OneToMany(mappedBy = "healthUnit")
    private List<Staff> staff = new ArrayList<>();

    private Boolean reminderEnabled;
    private Integer reminderLeadTimeValue;

    @Enumerated(EnumType.STRING)
    private ReminderTimeUnit reminderLeadTimeUnit; // HOURS ou MINUTES

    private Boolean useCustomReminderTemplate;

    @Lob // Indica que é um objeto grande, adequado para textos mais longos
    private String customReminderTemplate;

    public HealthUnit() {
    }

    public HealthUnit(Long id, String name, String email, String password, String phone, String address) {
        super(id, name, email, password);
        this.phone = phone;
        this.address = address;
    }

    public HealthUnit(Long id, String name, String email, String password, String phone, String address, Boolean reminderEnabled, ReminderTimeUnit reminderLeadTimeUnit, Integer reminderLeadTimeValue, Boolean useCustomReminderTemplate, String customReminderTemplate) {
        super(id, name, email, password);
        this.phone = phone;
        this.address = address;
        this.reminderEnabled = reminderEnabled;
        this.reminderLeadTimeUnit = reminderLeadTimeUnit;
        this.reminderLeadTimeValue = reminderLeadTimeValue;
        this.useCustomReminderTemplate = useCustomReminderTemplate;
        this.customReminderTemplate = customReminderTemplate;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<Staff> getStaff() {
        return staff;
    }

    public Boolean isReminderEnabled() {
        return reminderEnabled;
    }

    public void setReminderEnabled(Boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }

    public Integer getReminderLeadTimeValue() {
        return reminderLeadTimeValue;
    }

    public void setReminderLeadTimeValue(Integer reminderLeadTimeValue) {
        this.reminderLeadTimeValue = reminderLeadTimeValue;
    }

    public Boolean isUseCustomReminderTemplate() {
        return useCustomReminderTemplate;
    }

    public void setUseCustomReminderTemplate(Boolean useCustomReminderTemplate) {
        this.useCustomReminderTemplate = useCustomReminderTemplate;
    }

    public String getCustomReminderTemplate() {
        return customReminderTemplate;
    }

    public void setCustomReminderTemplate(String customReminderTemplate) {
        this.customReminderTemplate = customReminderTemplate;
    }

    public ReminderTimeUnit getReminderLeadTimeUnit() {
        return reminderLeadTimeUnit;
    }

    public void setReminderLeadTimeUnit(ReminderTimeUnit reminderLeadTimeUnit) {
        this.reminderLeadTimeUnit = reminderLeadTimeUnit;
    }
}