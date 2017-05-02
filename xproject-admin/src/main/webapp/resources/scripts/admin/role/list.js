
Vue.onDocumentReady(function() {
	new Vue({
		el: '#app',
		data: {
			roleQuering: false,
			loadingRoleList: false,
			roleQueryForm: {
				roleName: '',
				roleCode: '',
				pageSize: 10,
				currentPage: 1
			},
			roleQuerySort: {
				prop: 'createTime',
				order: 'descending'
			},
			roleList: []
		},
		created: function(){
			this.queryRoleList(0);
		},
		methods: {
			formatRoleType: function(roleType){
				if(roleType == 0){
					return "系统角色";
				}else{
					return "普通角色";
				}
			},
			queryRoleList: function(loading){
				var _this = this;
				var url = ADMIN_CONTEXT_PATH + '/admin/role/list';
				if(loading){
					this.loadingRoleList = true;
				}
				setTimeout(function(){
					var params = Object.assign({}, _this.roleQueryForm, convertToOrderBy(_this.roleQuerySort));
					axios.get(url, {
						params: params
					}).then(function(response){
						var result = response.data;
						if(result.success){
							_this.roleList = result.data;
						}else{
							_this.roleList = [];
						}
						if(loading){
							_this.loadingRoleList = false;
						}
					}).catch(function(error){
						_this.$message.error('请求出错!');
					});
				}, loading);
			},
			onRoleQuerySortChange: function(parameter){
				if(this.roleQuerySort.prop != parameter.prop || this.roleQuerySort.order != parameter.order){
					//alert(parameter.prop + ' , ' + parameter.order);
					this.roleQuerySort.prop = parameter.prop;
					this.roleQuerySort.order = parameter.order;
					this.queryRoleList(1000);
				}
			},
			resetQueryForm: function(){
				this.$refs.roleQueryForm.resetFields();
			}
		}
	});
}, DEFAULT_VUE_RENDER_TIMEOUT);