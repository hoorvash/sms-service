package org.smartech.smartech.model.elasticsearch;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.smartech.smartech.enumeration.SMSStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Getter
@Setter
@Builder
@ToString
@Document(indexName = "sms_log")
public class SMSLog {
    @Id
    private String id;
    @Field(type =Text)
    private String phone;
    @Field(type =Text)
    private String bodyText;
    @Field(type =FieldType.Integer)
    private Integer tryCount;
    @Field(type = FieldType.Date , format = DateFormat.date_hour_minute_second_millis)
    private Date dateTime;
    @Field(type = FieldType.Date , format = DateFormat.date_hour_minute_second_millis)
    private Date lastTryDateTime;
    @Field(type =Text)
    private SMSStatus smsStatus;
}
