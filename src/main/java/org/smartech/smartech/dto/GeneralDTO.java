package org.smartech.smartech.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneralDTO {
    private String resultMsg;
    private Integer resultCode;
    private Object result;
}
