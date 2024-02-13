package com.littlebird.userservice.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorBox {

    private String errorCode;

    private String errorDescription;

}
