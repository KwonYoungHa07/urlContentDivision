/******************************************
 * @file        common.js
 * @description 공통 JavaScript
 * @since       2021.06.29
 * @author      KYH
 ******************************************/

/******************************************
 * @funtion    : fn_isNull
 * @description: 입력받은 값이 존재하는지 확인
 * @since      : 2021.06.29
 * @author     : KYH
 ******************************************/
function fn_isNull(checkItem, strMessage) {
	if(checkItem.val() == "") {
		alert(strMessage);

		checkItem.focus();

		return false;
	}

	return true;
}