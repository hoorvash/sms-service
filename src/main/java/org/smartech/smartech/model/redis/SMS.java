package org.smartech.smartech.model.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("sms")
public class SMS implements Serializable {
    @Id
    private String id;
    private String phone;
    private String bodyText;
    private int tryCount;
    private LocalDateTime dateTime;
    private LocalDateTime lastTryDateTime;
}
