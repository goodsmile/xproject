/**
 * index页面顶部NAV菜单的高度
 */
var TOP_NAV_HEIGHT = 50;
/**
 * 浏览器窗口高度
 */
var WINDOW_HEIGHT = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
/**
 * index页面IFRAME的最小高度
 */
var MIN_MAIN_FRAME_HEIGHT = WINDOW_HEIGHT - TOP_NAV_HEIGHT;
window.onresize = function(){
	WINDOW_HEIGHT = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
	MIN_MAIN_FRAME_HEIGHT = WINDOW_HEIGHT - TOP_NAV_HEIGHT;
}

var EMPTY_PAGE_URL = 'about:blank';
var AdminVue = null;
var AdminMainContent = Vue.extend({
    template: '<div class="admin-content">'
    			+ '<iframe id="adminMainContent" class="admin-content-iframe" scrolling="no" frameborder="0" @load="onContentLoaded"></iframe>'
    		+ '</div>',
    props: {
		url: {
			type: String,
			default: EMPTY_PAGE_URL
		}
	},
	data: function(){
		return {
			contentLoading: false,
			autoResizeInterval: null
		};
	},
    mounted: function(){
    	this.autoResizeInterval = setInterval(this.autoResizeMainFrameHeight, 200);
    },
    methods: {
    	getContentUrl: function(){
    		var src = EMPTY_PAGE_URL;
    		if(!this.url || this.url == '#'){
    			src = EMPTY_PAGE_URL;
    		}else if(/^http:\/\/.+$/.test(this.url)){
    			src = this.url;
    		}else{
    			if(this.url.charAt(0) != '/'){
    				src = ADMIN_CONTEXT_PATH + '/' + this.url;
    			}else{
    				src = ADMIN_CONTEXT_PATH + this.url;
    			}
    		}
    		return src;
    	},
    	onContentLoaded: function(){
    		this.contentLoading = false;
    	},
    	setMainFrameMinHeight: function(){
    		var appContainer = document.getElementById('app');
    		var mainFrame = document.getElementById('adminMainContent');
    		if(mainFrame){
    			mainFrame.style.height = MIN_MAIN_FRAME_HEIGHT + "px";
        		mainFrame.style.minHeight = MIN_MAIN_FRAME_HEIGHT + "px";
    		}
    		appContainer.style.minHeight = "100%";
    	},
    	autoResizeMainFrameHeight: function(){
			var appContainer = document.getElementById('app');
    		var mainFrame = document.getElementById('adminMainContent');
    		var mainFrameHeight = MIN_MAIN_FRAME_HEIGHT;
    		if(mainFrame){
    			var frameWindow = mainFrame.contentWindow;
    			try {
    				//alert(frameWindow.document.documentElement.scrollHeight + ' , ' + frameWindow.document.body.scrollHeight);
    				mainFrameHeight = Math.min(frameWindow.document.documentElement.scrollHeight, frameWindow.document.body.scrollHeight);
    			}catch(e){
    				mainFrameHeight = MIN_MAIN_FRAME_HEIGHT;
    			}
    			mainFrameHeight = Math.max(mainFrameHeight, MIN_MAIN_FRAME_HEIGHT);
    			if(!this.contentLoading){
        			mainFrame.style.height = mainFrameHeight + "px";
        			mainFrame.style.minHeight = MIN_MAIN_FRAME_HEIGHT + "px";
    			}
    		}
    		if(!this.contentLoading){
    			appContainer.style.minHeight = (mainFrameHeight + TOP_NAV_HEIGHT) + "px";
    		}
    	}
    },
    watch: {
    	url: function(curValue, oldValue){
    		this.contentLoading = true;
    		this.setMainFrameMinHeight();
    		var src = this.getContentUrl();
    		if(src != EMPTY_PAGE_URL){
    			this.showLoading = true;
    			this.loadingStartTime = new Date().getTime();
    		}else{ //载入空页面时不显示loading效果
    			this.showLoading = false;
    		}
    		var mainFrame = document.getElementById('adminMainContent');
    		if(mainFrame){
    			mainFrame.setAttribute("src", src);
    		}
    	}
    },
    beforeDestroy: function(){
    	if(this.autoResizeInterval){
    		clearInterval(this.autoResizeInterval);
    	}
    }
});
Vue.component('admin-maincontent', AdminMainContent); //注册全局组件

var AdminSideMenu = Vue.extend({
	render: function(h){
		var _this = this;
		return h('el-menu', {
			'class': _this.menuClass,
			props: {
				theme: _this.menuTheme,
				router: true
			}
		}, _this.recurisiveLoadSubMenu(h, _this.menuList));
	},
	props: {
		menuClass: {
			type: Array,
			default: ['el-menu-admin-side', 'el-menu-vertical-dark', 'el-menu-small']
		},
		menuTheme: {
			type: String,
			default: 'dark'
		},
		menuList: {
			type: Array,
			default: []
		}
	},
	methods: {
		/**
		 * 递归处理加载渲染侧边菜单树结构
		 */
		recurisiveLoadSubMenu: function(h, menus){
			var _this = this;
			var elements = [];
			if(menus && menus.length){
				menus.forEach(function(menu){
					var item = null;
					if(menu.subMenuList && menu.subMenuList.length){
						item = h('el-submenu', {
							props: {
								index: menu.menuPath
							},
							scopedSlots: {
								title: function(){
									return [h('i', {'class': [_this.formatMenuIcon(menu.menuIcon)]}), menu.menuName];
								}
							}
						}, _this.recurisiveLoadSubMenu(h, menu.subMenuList));
						elements.push(item);
					}else{
						item = h('el-menu-item', {
							props: {
								index: menu.menuPath
							}
						},[h('i', {'class': [_this.formatMenuIcon(menu.menuIcon)]}), menu.menuName]);
						elements.push(item);
					}
				});
			}
			return elements;
		},
		formatMenuIcon: function(menuIcon){
			if(menuIcon){
				if(menuIcon.toLowerCase().indexOf('el-icon-') == -1){
					menuIcon = 'el-icon-' + menuIcon;
				}
			}else{
				menuIcon = 'el-icon-fa-circle-o';
			}
			return menuIcon;
		}
	}
});
Vue.component('admin-sidemenu', AdminSideMenu); //注册全局组件

var router = new VueRouter({
	mode: 'abstract', //在abstract模式下浏览器地址栏不变
	routes: [{
		path: '/admin/user/profile', //用户资料
		components: {
			sideMenuView: AdminSideMenu,
			mainContentView: AdminMainContent
		},
		beforeEnter: function(to, from, next){
			AdminVue.mainContentUrl = to.path;
			next();
		}
	},{
		path: '/admin/user/settings', //用户设置
		components: {
			sideMenuView: AdminSideMenu,
			mainContentView: AdminMainContent
		},
		beforeEnter: function(to, from, next){
			AdminVue.mainContentUrl = to.path;
			next();
		}
	},{
		path: '/logout', //用户退出
		components: {
			sideMenuView: AdminSideMenu,
			mainContentView: AdminMainContent
		},
		beforeEnter: function(to, from, next){
			next(false); //终止原本导航
			window.location.href = ADMIN_CONTEXT_PATH + to.path;
		}
	}]
});

AdminVue = new Vue({
	el: '#app',
	router: router,
	created: function(){
		var _this = this;
		axios.get(ADMIN_CONTEXT_PATH + '/login/user/current').then(function(response){
			var result = response.data;
			if(result && result.success){
				_this.loginUser = result.data;
			}else{
				_this.$message.error('请求出错!');
			}
		}).catch(function(error){
			_this.$message.error('请求出错!');
		});
		axios.get(ADMIN_CONTEXT_PATH + '/login/user/menus').then(function(response){
			if(response.data){
				var topNavMenus = [], routes = [];
				_this.recurisiveHandleMenus(response.data, routes, topNavMenus);
				_this.topNavMenus = topNavMenus;
				_this.$router.addRoutes(routes);
				_this.menuDataLoaded = true;
			}
		}).catch(function(error){
			_this.$message.error('请求出错!');
		});
	},
	data: {
		menuDataLoaded: false,
		topNavMenus: [],
		sideNavMenus: [],
		mainContentUrl: '',
		topNavMenuDefaultActiveIndex: '',
		loginUser: {}
	},
	methods: {
		/**
		 * 递归处理服务端的menu数据
		 */
		recurisiveHandleMenus: function(datas, routes, topNavMenus){
			if(datas && datas.length){
				var _this = this;
				datas.forEach(function(node){
					var isTopMenuNode = node.menuLevel == TOP_NAV_MENU_LEVEL;
					var route = {
						path: node.menuPath,
						components: {
							sideMenuView: AdminSideMenu,
							mainContentView: AdminMainContent
						},
						meta: {
							subMenuList: [],
							menuUrl: null
						}
					};
					node['menuRoute'] = {
						path: node.menuPath
					};
					if(isTopMenuNode){
						route.meta.subMenuList = node.subMenuList;
						route['beforeEnter'] = function(to, from, next){
							_this.sideNavMenus = to.meta.subMenuList;
							_this.mainContentUrl = null;
							next();
						};
						topNavMenus.push(node);
					}else{
						route.meta.menuUrl = node.menuUrl;
						route['beforeEnter'] = function(to, from, next){
							_this.mainContentUrl = to.meta.menuUrl;
							next();
						};
					}
					routes.push(route);
					if(node.subMenuList){
						_this.recurisiveHandleMenus(node.subMenuList, routes, topNavMenus);
					}
				});
			}
		}
	},
	watch: {
		/**
		 * 获取服务端的menu数据&处理完毕后
		 */
		menuDataLoaded: function(curValue, oldValue){
			if(curValue){
				var activeNode = null;
				if(this.topNavMenus && this.topNavMenus.length){
					activeNode = this.topNavMenus[0];
				}
				if(activeNode){
					this.topNavMenuDefaultActiveIndex = activeNode.menuPath; //默认选中第一个NAV
					this.$router.push(activeNode.menuRoute); //通过vue-router加载选中第一个NAV后的视图
				}
			}
		}
	}
});