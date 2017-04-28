/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tmf.dsmapi.service.qualification.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.tmf.dsmapi.commons.utils.CustomJsonDateSerializer;
import org.tmf.dsmapi.service.qualification.enity.ServiceQualificationReqEntity;
import org.tmf.dsmapi.service.qualification.model.ServiceQualificationReq;

/**
 *
 * @author Lin
 */
@XmlRootElement
@Entity
@Table(name="SERVICE_QUALIFICATION_EVENT")
@JsonPropertyOrder(value = {"eventId","eventTime", "eventType", "resource"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceQualificationEvent implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomJsonDateSerializer.class)
    private Date eventTime;
    
    @Enumerated(value = EnumType.STRING)
    private QualificationEventTypeEnum eventType;
    
    private ServiceQualificationReqEntity resource;

    @JsonProperty("eventId")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public QualificationEventTypeEnum getEventType() {
        return eventType;
    }

    public void setEventType(QualificationEventTypeEnum eventType) {
        this.eventType = eventType;
    }


    @Override
    public String toString() {
        return "ServiceQualificationEvent{" + "id=" + id + ", eventTime=" + eventTime + ", eventType=" + eventType + ", resource=" + getResource() + '}';
    }

    /**
     * @return the resource
     */
    public ServiceQualificationReqEntity getResource() {
        return resource;
    }

    /**
     * @param resource the resource to set
     */
    public void setResource(ServiceQualificationReqEntity resource) {
        this.resource = resource;
    }
    
}
