/**
 * 解决input readonly样式各浏览器之间的差异问题
 * @param event
 * @returns
 */
function onFocus4ReadOnly(event){
	event.target.blur();
}
//Element Validator begin
/**
 * 字符串最小长度验证
 * {
 * 		validator: minLength,
 * 		minLength: 5,
 * 		message: '商品名称至少5个字符!'
 * }
 * @param rule
 * @param value
 * @param callback
 * @returns
 */
function minLength(rule, value, callback){
	if(value && value.length < rule.minLength){
		return callback(new Error(rule.message));
	}else{
		callback();
	}
}
/**
 * 字符串最大长度验证
 * {
 * 		validator: maxLength,
 * 		maxLength: 15,
 * 		message: '商品名称不能超过15个字符!'
 * }
 * @param rule
 * @param value
 * @param callback
 * @returns
 */
function maxLength(rule, value, callback){
	if(value && value.length > rule.maxLength){
		return callback(new Error(rule.message));
	}else{
		callback();
	}
}
/**
 * 最小值验证
 * {
 * 		validator: minValue,
 * 		minValue: 0,
 * 		message: '库存数不能小于0'
 * }
 * @param rule
 * @param value
 * @param callback
 * @returns
 */
function minValue(rule, value, callback){
	if(value){
		if(typeof rule.minValue == 'string'){
			if(value < rule.minValue){
				return callback(new Error(rule.message));
			}
		}else if(typeof rule.minValue == 'number'){
			var val = new Number(value);
			if(!isNaN(val) && val < rule.minValue){
				return callback(new Error(rule.message));
			}
		}else if(rule.minValue instanceof Date){
			var val = new Date(Date.parse(value.replace(/-/g, "/")));
			if(val < rule.minValue){
				return callback(new Error(rule.message));
			}
		}
	}
	callback();
}
/**
 * 最大值验证
 * {
 * 		validator: maxValue,
 * 		maxValue: 999,
 * 		message: '库存数最多不能超过999'
 * }
 * @param rule
 * @param value
 * @param callback
 * @returns
 */
function maxValue(rule, value, callback){
	if(value){
		if(typeof rule.maxValue == 'string'){
			if(value > rule.maxValue){
				return callback(new Error(rule.message));
			}
		}else if(typeof rule.maxValue == 'number'){
			var val = new Number(value);
			if(!isNaN(val) && val > rule.maxValue){
				return callback(new Error(rule.message));
			}
		}else if(rule.maxValue instanceof Date){
			var val = new Date(Date.parse(value.replace(/-/g, "/")));
			if(val > rule.maxValue){
				return callback(new Error(rule.message));
			}
		}
	}
	callback();
}

/**
 * {
 * 		validator: email,
 * 		message: '不合法的email格式!'
 * }
 * 
 * 验证是否是email
 * @param rule
 * @param value
 * @param callback
 * @returns
 */
function email(rule, value, callback){
	if(value){
		if(!/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i.test(value)){
			return callback(new Error(rule.message));
		}
	}
	callback();
}

/**
 * {
 * 		validator: url,
 * 		message: '不合法的url格式!'
 * }
 * 
 * 验证是否是url
 * @param rule
 * @param value
 * @param callback
 * @returns
 */
function url(rule, value, callback){
	if(value){
		if(!/^(https?|s?ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(value)){
			return callback(new Error(rule.message));
		}
	}
	callback();
}

/**
 * {
 * 		validator: date,
 * 		message: '不合法的日期格式!'
 * }
 * 
 * 验证是否是date
 * @param rule
 * @param value
 * @param callback
 * @returns
 */
function date(rule, value, callback){
	if(value){
		if(!/^\d{4}[\/\-]\d{2}[\/\-]\d{2}$/.test(value)){
			return callback(new Error(rule.message));
		}
	}
	callback();
}

/**
 * {
 * 		validator: datetime,
 * 		message: '不合法的日期时间格式!'
 * }
 * 
 * 验证是否是datetime
 * @param rule
 * @param value
 * @param callback
 * @returns
 */
function datetime(rule, value, callback){
	if(value){
		if(!/^\d{4}[\/\-]\d{2}[\/\-]\d{2} \d{2}:\d{2}:\d{2}$/.test(value)){
			return callback(new Error(rule.message));
		}
		
	}
	callback();
}
/**
 * {
 * 		validator: number,
 * 		message: '不是数值格式!'
 * }
 * 
 * 验证是否是数值
 * @param rule
 * @param value
 * @param callback
 * @returns
 */
function number(rule, value, callback){
	if(value){
		if(!/^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(value)){
			return callback(new Error(rule.message));
		}
	}
	callback();
}

/**
 * {
 * 		validator: digits,
 * 		message: '不是整数!'
 * }
 * 
 * 验证是否是整数
 * @param rule
 * @param value
 * @param callback
 * @returns
 */
function digits(rule, value, callback){
	if(value){
		if(!/^\d+$/.test(value)){
			return callback(new Error(rule.message));
		}
	}
	callback();
}

/**
 * {
 * 		validator: length,
 * 		length: 11,
 * 		message: '手机号码必须是11位!'
 * }
 * 
 * 验证字符串位数
 * @param rule
 * @param value
 * @param callback
 * @returns
 */
function length(rule, value, callback){
	if(value){
		if(value.length != rule.length){
			return callback(new Error(rule.message));
		}
	}
	callback();
}
/**
 * 相等验证
 * {
 *  	validator: equalTo,
 *		compareTarget: {
 *			vue: 'vm',
 *			form: 'form1',
 *			field: 'repassword'
 *		},
 *		message: '两次密码不一致!'
 * }
 * @param rule
 * @param value
 * @param callback
 * @returns
 */
function equalTo(rule, value, callback){
	if(value){
		var compareTargetValue = window[rule.compareTarget.vue]['$data'][rule.compareTarget.form][rule.compareTarget.field];
		if(value != compareTargetValue){
			return callback(new Error(rule.message));
		}
	}
	callback();
}

/**
 * 正则表达式验证
 * 
 * {
 *  	validator: regex,
 *		regex: /^\d+$/,
 *		message: '商品库存必须是数字'
 * }
 * 
 * @param rule
 * @param value
 * @param callback
 * @returns
 */
function regex(rule, value, callback){
	if(value){
		var regex = rule.regex;
		if(regex instanceof RegExp){
		}else{
			regex = new RegExp(regex);
		}
		if(!regex.test(value)){
			return callback(new Error(rule.message));
		}
	}
	callback();
}
/**
 * 时间日期比较验证
 * 
 * {
 *  	validator: dateCompare,
 *		compareType: 'lt',
 *		compareTarget: {
 *			vue: 'vm',
 *			form: 'form1',
 *			field: 'promoteSaleEndTime'
 *		},
 *		message: '促销开始时间必须小于促销结束时间!'
 * }
 * {
 *  	validator: dateCompare,
 *		compareType: 'gt',
 *		compareTarget: {
 *			vue: 'vm',
 *			form: 'form1',
 *			field: 'promoteSaleStartTime'
 *		},
 *		message: '促销结束时间必须大于促销开始时间!'
 * }
 * 
 * @param rule
 * @param value
 * @param callback
 * @returns
 */
function dateCompare(rule, value, callback){
	if(value){
		var compareTargetValue = window[rule.compareTarget.vue]['$data'][rule.compareTarget.form][rule.compareTarget.field];
		if(compareTargetValue){
			if('gt' == rule.compareType){ //大于
				if(value > compareTargetValue){
					callback();
				}else{
					return callback(new Error(rule.message));
				}
			}else if('gte' == rule.compareType){ //大于等于
				if(value >= compareTargetValue){
					callback();
				}else{
					return callback(new Error(rule.message));
				}
			}else if('lt' == rule.compareType){ //小于
				if(value < compareTargetValue){
					callback();
				}else{
					return callback(new Error(rule.message));
				}
			}else if('lte' == rule.compareType){ //小于等于
				if(value <= compareTargetValue){
					callback();
				}else{
					return callback(new Error(rule.message));
				}
			}else{
				callback();
			}
		}else{
			callback();
		}
	}else{
		callback();
	}
}
//Element Validator end