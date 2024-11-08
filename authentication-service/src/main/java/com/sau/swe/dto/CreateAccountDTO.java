package com.sau.swe.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateAccountDTO {

    private Long userId;
    private String accountType;
}
