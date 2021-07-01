package com.company.urlContentDivision.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import org.hibernate.validator.constraints.URL;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UrlContentDivisionDto {
	@URL
	@NotBlank
    private String url;        //URL

	@NotBlank
    private String type;       //Type

	@Positive
	@NotBlank
    private int    divisor;    //출력 묶음 단위

    private String quotient;   //몫
    
    private String remainder;  //나머지

    private String errorMsg;   //오류 메세지

	@Builder
    public UrlContentDivisionDto(String url, String type, int divisor, String quotient, String remainder, String errorMsg) {
        this.url       = url;
        this.type      = type;
        this.divisor   = divisor;
        this.quotient  = quotient;
        this.remainder = remainder;
        this.errorMsg  = errorMsg;
    }
}
