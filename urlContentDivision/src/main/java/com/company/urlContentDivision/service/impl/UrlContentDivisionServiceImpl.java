package com.company.urlContentDivision.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.company.urlContentDivision.domain.UrlContentDivisionDto;
import com.company.urlContentDivision.service.UrlContentDivisionService;

@Service
public class UrlContentDivisionServiceImpl implements UrlContentDivisionService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/***********************************************************************************************
	 * @function    getUrlContentDivision
	 * @description 
	 * @param       UrlContentDivisionDto(dto)
	 * @return      UrlContentDivisionDto
	 * @since       2021.06.30
	 * @author      KYH
	 ***********************************************************************************************/
	@Override
	public UrlContentDivisionDto getUrlContentDivision(UrlContentDivisionDto resultDto) throws Exception {
		String content      = "";

        try {
        	//입력받은 URL의 페이지의 내용을 StringBuilder에 담은 후, String 형태로 받기
            content = getHtmlContent(resultDto.getUrl());
        } catch(UnknownHostException e) {
        	resultDto.setErrorMsg("사이트를 찾을 수 없습니다.");
        	
        	return resultDto;
        } catch(Exception e) {
        	resultDto.setErrorMsg("오류가 발생 하였습니다.");
        	
        	return resultDto;
        }

        //Type 값이 HTML 태그 제외인 경우
        if(resultDto.getType().equals("tagExclusion")) {
        	//페이지의 내용 중 HTML 태그를 제거
            content = removeTags(content);
        }

        //페이지의 내용 중 알파벳만 추출 및 추출된 알파벳 정렬(오름차순)
        List   alphabetList = sortAlphabet(alphabetFilter(content));

        //페이지의 내용 중 숫자만 추출 및 추출된 숫자 정렬(오름차순)
        List   numberList   = sortNumber(numberFilter(content));

        //추출한 알파벳 리스트 결과와 숫자 리스트 결과를 알파벳, 숫자 교차 처리
        List   mixList      = mix(alphabetList, numberList);

        //출력 단위 묶음
        int    divisor      = Integer.valueOf(resultDto.getDivisor());

        //몫과 나머지 계산하기
        resultDto = division(mixList, divisor, resultDto);

		return resultDto;
	}

	/*****************************************************************************************************
	 * @function    alphabetFilter
	 * @description 내용 중 알파벳 패턴에 따른 알파벳 필터 처리
	 * @param       String(content)
	 * @return      String(result)
	 * @since       2021.06.30
	 * @author      KYH
	 *****************************************************************************************************/
    public String alphabetFilter(String content) {
    	String        result          = null;

    	//내용 중 알파벳만 필터 처리를 위해 추출할 알파벳 패턴(대소문자) 정의
    	Pattern       alphabetPattern = Pattern.compile("[a-zA-Z]");

    	//내용 중 알파벳만 필터 처리
        Matcher       matcher         = alphabetPattern.matcher(content);

        StringBuilder sb              = new StringBuilder();

        while(matcher.find()) {
            sb.append(matcher.group());
        }

        result = sb.toString(); 
        
        return result;
    }

	/*****************************************************************************************************
	 * @function    division
	 * @description 문자로 된 몫과 나머지 값 구하여 UrlContentDivisionDto 몫과 나머지에 값을 Set한 후 반환
	 * @param       List(mixList), int(divisor), UrlContentDivisionDto(resultDto)
	 * @return      UrlContentDivisionDto(resultDto)
	 * @since       2021.06.30
	 * @author      KYH
	 *****************************************************************************************************/
	private UrlContentDivisionDto division(List mixList, int divisor, UrlContentDivisionDto resultDto) {
        String strMix       = String.join("", mixList);

        //숫자
        int    remainderNum = strMix.length() % divisor;                            //나머지 값(숫자)

        //문자
        String quotient     = strMix.substring(0, strMix.length() - remainderNum);  //몫(문자)
        String remainder    = strMix.substring(strMix.length() - remainderNum);     //나머지(문자)

        resultDto.setQuotient(quotient);
        resultDto.setRemainder(remainder);
        
        return resultDto;
	}

	/*****************************************************************************************************
	 * @function    getHtmlContent
	 * @description 입력받은 URL의 페이지의 내용을 StringBuilder에 담은 후, String 형태로 변환
	 * @param       String(strUrl)
	 * @return      String(result)
	 * @since       2021.06.30
	 * @author      KYH
	 *****************************************************************************************************/
    public String getHtmlContent(String strUrl) throws UnknownHostException, Exception {
    	String        result      = null;

    	//입력받은 URL의 페이지의 내용을 StringBuilder에 담기 
        String        pageContent = "";
        StringBuilder content     = new StringBuilder();

        try {
            URL               url       = new URL(strUrl);
            URLConnection     urlCon    = (URLConnection) url.openConnection();
            InputStreamReader insReader = new InputStreamReader(urlCon.getInputStream(), "UTF-8");
            BufferedReader    buffer    = new BufferedReader(insReader);

            while((pageContent = buffer.readLine()) != null) {
                content.append(pageContent);
            }

            buffer.close();
        } catch (UnknownHostException e) {
            logger.error("Exception", e);
            throw e;
        } catch (Exception e) {
            logger.error("Exception", e);
            throw e;
        }

        result = content.toString();

        return result;
    }

	/*****************************************************************************************************
	 * @function    mix
	 * @description 알파벳과 숫자 순으로 교차 출력을 위해 List에 알파벳, 숫자순으로 담기
	 * @param       List(alphabetList), List(numberList)
	 * @return      List(resultList)
	 * @since       2021.06.30
	 * @author      KYH
	 *****************************************************************************************************/
	private List mix(List alphabetList, List numberList) {
        List resultList = new ArrayList();

        int  i          = 0;

        while(i < alphabetList.size() && i < numberList.size()) {
        	resultList.add(alphabetList.get(i));  //알파벳
        	resultList.add(numberList.get(i));    //숫자

            i++;
        }

        if(i < alphabetList.size()) {
        	resultList.addAll(alphabetList.subList(i, alphabetList.size()));
        }

        if(i < numberList.size()) {
        	resultList.addAll(numberList.subList(i, numberList.size()));
        }

        return resultList;
	}

	/*****************************************************************************************************
	 * @function    numberFilter
	 * @description 내용 중 숫자 패턴에 따른 숫자 필터 처리
	 * @param       String(content)
	 * @return      String(result)
	 * @since       2021.06.30
	 * @author      KYH
	 *****************************************************************************************************/
    public String numberFilter(String content) {
    	String        result          = null;

    	//내용 중 숫자만 필터 처리를 위해 추출할 숫자 패턴 정의
    	Pattern       numberPattern   = Pattern.compile("[0-9]");

        Matcher       matcher         = numberPattern.matcher(content);

    	//내용 중 숫자만 필터 처리
        StringBuilder sb              = new StringBuilder();

        while(matcher.find()) {
            sb.append(matcher.group());
        }

        result = sb.toString(); 
        
        return result;
    }

	/*****************************************************************************************************
	 * @function    removeTags
	 * @description Type 값이 HTML 태그 제외인 경우, 내용 중 HTML 태그를 제거
	 * @param       String(content)
	 * @return      String(result)
	 * @since       2021.06.30
	 * @author      KYH
	 *****************************************************************************************************/
    public String removeTags(String content) {
    	String result = null;

    	//내용 중 HTML 태그를 제거
    	result = Jsoup.parse(content).text();
    	
        return result;
    }

	/*****************************************************************************************************
	 * @function    sortAlphabet
	 * @description 알파벳 필터 처리 후, 알파벳 정렬(오름차순)
	 *              ※ 알파벳 정렬 방법
	 *                 1.알파벳 순서
	 *                 2.알파벳 대문자, 소문자 순으로 정렬  
	 * @param       String(content)
	 * @return      List(resultList)
	 * @since       2021.06.30
	 * @author      KYH
	 *****************************************************************************************************/
    public List sortAlphabet(String content) {
        List resultList = new ArrayList(Arrays.asList(content.split("")));

        //알파벳 순서로 정렬
        Collections.sort(resultList, new Comparator<String>() {
        	//알파벳 대문자, 소문자 순으로 정렬(대문자인지 소문자인지 비교)
            @Override
            public int compare(String str1, String str2) {
                int i = str1.toUpperCase().compareTo(str2.toUpperCase());

                if(i == 0) {
                    i = str1.compareTo(str2);
                }

                return i;
            }
        });

        return resultList;
    }

	/*****************************************************************************************************
	 * @function    sortNumber
	 * @description 숫자 필터 처리 후, 숫자 정렬(오름차순)  
	 * @param       String(content)
	 * @return      List(resultList)
	 * @since       2021.06.30
	 * @author      KYH
	 *****************************************************************************************************/
    public List sortNumber(String content) {
        List resultList = new ArrayList(Arrays.asList(content.split("")));

        //숫자 순서로 정렬
        Collections.sort(resultList);

        return resultList;
    }
}
