/**
 * 全局公共js
 */
/**
 * 系统管理应用上下文路径
 */
var ADMIN_CONTEXT_PATH = '/xproject-admin';
/**
 * 默认系统管理-根资源ID
 */
var DEFAULT_ROOT_RESOURCE_ID = '0';
/**
 * 系统管理-首页页面展示的顶部NAV菜单的菜单级别
 */
var TOP_NAV_MENU_LEVEL = 1;
/**
 * 文档加载完毕后,setTimeout中初始化Vue组件的timeout时间
 */
var DEFAULT_VUE_RENDER_TIMEOUT = 500;

/**
 * axios全局配置
 */
if(axios){
	axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
	/**
	 * 后端依此标识为异步请求
	 */
	axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	/**
	 * 异步请求响应拦截
	 */
	axios.interceptors.response.use(function (response) {
	    return response;
	}, function (error) {
		var statusCode = error.response.status;
		if(statusCode == 401){
			window.top.location.href = ADMIN_CONTEXT_PATH + '/login';
			/*if(window.parent){
				window.parent.location.href = ADMIN_CONTEXT_PATH + '/login';
			}else{
				window.location.href = ADMIN_CONTEXT_PATH + '/login';
			}*/
			return Promise.reject(error);
		}
	});
}