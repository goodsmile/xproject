/** 
 * 多页面应用时，页面跳转时的loading效果插件,
 * 只要引入改文件即可
 * <请紧跟在vue.js后面引入>
 */
/**
 * 页面中Vue的el挂载id,默认为app
 */
var PAGE_CONTAINER_ID = "app";
/**
 * 页面loading效果持续的最小时长
 */
var PAGE_LOADING_TIME = 2500;
/**
 * 页面loading文本,默认没有
 */
var PAGE_LOADING_TEXT = '';

var PAGE_LOADING_VM = null;
var pageLoadStartTimes = new Date().getTime();
var pageContainer = document.getElementById(PAGE_CONTAINER_ID);

function hasClass(el, cls) {
    return el.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
}

function addClass(el, cls) {
    if (!hasClass(el, cls)) el.className += " " + cls;
}

function removeClass(el, cls) {
    if (hasClass(el, cls)) {
        var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
        el.className = el.className.replace(reg, ' ');
    }
}

if(pageContainer){
	addClass(pageContainer, 'el-hidden');
}

var ElPageLoading = Vue.extend({
	template: '<transition name="el-fade" @leave="handleLeave">'
				+ '<div v-show="visible" class="el-loading-mask el-pageloading-mask is-fullscreen" :class="[customClass]">'
					+ '<div class="el-loading-spinner">'
			        	+ '<svg class="circular" viewBox="25 25 50 50"><circle class="path" cx="50" cy="50" r="20" fill="none"/></svg>'
			        	+ '<p v-if="loadingText" class="el-loading-text">{{loadingText}}</p>'
			        + '</div>'
		        + '</div>'
			+ '</transition>',
	data: function(){
		return {
			loadingText: '',
			customClass: '',
			visible: true
		};
	},
	methods: {
		handleLeave: function(){
			removeClass(pageContainer, 'el-hidden');
		}
	}
});

if(pageContainer){
	PAGE_LOADING_VM = new ElPageLoading({
	    el: document.createElement('div'),
	    data: {
	    	loadingText: ''
	    },
	    mounted: function(){
	    }
	});
	document.body.appendChild(PAGE_LOADING_VM.$el);
}

Vue.mixin({
	mounted: function(){
		if (PAGE_LOADING_VM && this.$el.id == PAGE_CONTAINER_ID) {
			pageContainer = this.$el;
			var $this = this;
			var pageLoadEndTimes = new Date().getTime();
			var timeout = PAGE_LOADING_TIME - (pageLoadEndTimes - pageLoadStartTimes);
			if(timeout > 0){
				setTimeout(function(){
					Vue.nextTick(function(){
						PAGE_LOADING_VM.visible = false;
					});
				}, timeout);
			}else{
				Vue.nextTick(function(){
					PAGE_LOADING_VM.visible = false;
				});
			}
		}
	}
})
