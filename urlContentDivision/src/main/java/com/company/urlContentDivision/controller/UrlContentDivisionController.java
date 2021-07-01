package com.company.urlContentDivision.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.company.urlContentDivision.domain.UrlContentDivisionDto;
import com.company.urlContentDivision.service.UrlContentDivisionService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/main")
public class UrlContentDivisionController {
	@Inject
	UrlContentDivisionService urlContentDivisionService;

	/*****************************************************************************************************************************
	 * @function    urlContentDivision
	 * @description URL을 호출하여 페이지 내용을 조회하여 알파벳과 숫자만 필터링하여 교차 출력하여 몫과 나머지를 구함
	 * @param       HttpServletRequest(request), UrlContentDivisionDto(dto)
	 * @return      ResponseEntity
	 * @since       2021.06.29
	 * @author      KYH
	 *****************************************************************************************************************************/
	@RequestMapping(value="/urlContentDivision.do", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity urlContentDivision(HttpServletRequest request ,@Valid UrlContentDivisionDto dto) throws Exception {
		UrlContentDivisionDto resultDto = urlContentDivisionService.getUrlContentDivision(dto);

		return ResponseEntity.ok(resultDto);
	}
}
