package com.qcadoo.mes.beans.sample;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SampleSimpleDatabaseObject {

    private Long id;

    private String name;

    private Integer age;

    private Integer priority;

    private BigDecimal money;

    private Boolean retired;

    private Date birthDate;

    private boolean deleted;

    private SampleParentDatabaseObject belongsTo;

    private SampleParentDatabaseObject lazyBelongsTo;

    public SampleSimpleDatabaseObject() {
    }

    public SampleSimpleDatabaseObject(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }

    public SampleParentDatabaseObject getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(final SampleParentDatabaseObject belongsTo) {
        this.belongsTo = belongsTo;
    }

    public SampleParentDatabaseObject getLazyBelongsTo() {
        return lazyBelongsTo;
    }

    public void setLazyBelongsTo(final SampleParentDatabaseObject lazyBelongsTo) {
        this.lazyBelongsTo = lazyBelongsTo;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(final Integer priority) {
        this.priority = priority;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(final BigDecimal money) {
        this.money = money;
    }

    public Boolean getRetired() {
        return retired;
    }

    public void setRetired(final Boolean retired) {
        this.retired = retired;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(final Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
