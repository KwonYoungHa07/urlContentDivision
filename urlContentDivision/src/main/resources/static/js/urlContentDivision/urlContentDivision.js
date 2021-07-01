/***************************************************************************
 * @file        urlContentDivision.js
 * @description 입력, 버튼 클릭 관련 처리 정의
 * @since       2021.06.29
 * @author      KYH
 ***************************************************************************/

$(document).ready(function(){
	//출력 묶음 단위 숫자만 입력 처리(음수는 출력 버튼에서 메세지 노출 처리)
    $("#divisor").keyup(function(event){        
		var str;

		if(event.keyCode != 8) {
			if(!(event.keyCode >= 37 && event.keyCode <= 40)) {
				var inputVal = $(this).val();

				str = inputVal.replace(/[^-0-9]/gi, '');

				if(str.lastIndexOf("-") > 0) { //중간에 -가 있다면 replace
					if(str.indexOf("-") == 0) { //음수라면 replace 후 - 붙여준다.
						str = "-" + str.replace(/[-]/gi, '');
					} else {
						str = str.replace(/[-]/gi, '');
					}
				}

				$(this).val(str);
			}                    
		}
	});

	//출력 버튼 클릭
	$('#btnOutput').click(function(){
		fn_Output();
	})
});

/***************************************************************************
 * @funtion    : fn_Output
 * @description: 출력 버튼 클릭 시, 호출
 * @since      : 2021.06.29
 * @author     : KYH
 ***************************************************************************/
function fn_Output() {
	var url     = $('#url').val();
	var type    = $('#type').val();
	var divisor = $('#divisor').val();

	//입력값 유효성 체크
	if(!fn_isNull($("#url")     ,"URL를 입력하세요.")) {
		return;
	}

	if(!fn_isNull($('#divisor') ,"출력 단위 묶음을 입력하세요.")) {
		return;
	}

	if(divisor.length > 1 && divisor.substring(0, 1) == "-") {
		alert("출력 묶음 단위에는 음수는 입력할 수 없습니다. 양수를 입력하세요.");
		return;
	} else if(divisor.substring(0, 1) == "-") {
		alert("출력 묶음 단위에는 음수 기호만 입력할 수 없습니다. 양수를 입력하세요.");
		return;		
	}
	
	var params = 'url='   + $("#url").val()
               + '&type=' + $("#type").val()
               + '&divisor=' + $("#divisor").val();

	$.ajax({url     : '/main/urlContentDivision.do'
		  ,type     : 'POST'
		  ,data     : params
		  ,dataType : 'json'
	      ,success  : function(data, res) {
			if(res == "success" && data.errorMsg == null) {
				$('#quotient').text(data.quotient);    //몫
				$('#remainder').text(data.remainder);  //나머지	
			} else {
				alert(data.errorMsg);	
			}
		  }
          ,error    : function(data, textStatus) {
			alert('error: ' + textStatus );
		  }
	});
}