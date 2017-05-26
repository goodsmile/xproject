
Vue.onDocumentReady(function() {
	vm = new Vue({
		el: '#app',
		data: {
			userProfileActiveTabName: 'userBasicInfo',
			userAccessLogQueryForm: {
				title: '',
				startAccessTime: '',
				endAccessTime: '',
				pageSize: 10,
				currentPage: 1
			},
			userAccessLogQuerySort: {
				prop: 'accessTime',
				order: 'descending'
			},
			userAccessLogQueryTotal: 0,
			userAccessLogLoading: false,
			currentUser: {
				userId: '',
				userName: '',
				realName: '',
				mobilePhone: '',
				email: '',
				userType: '',
				userTypeName: '',
				status: '',
				statusName: '',
				lastLoginTime: '',
				loginTimes: 0,
				createTime: '',
				updateTime: '',
				userIcon: '',
				userIconUrl: '',
				userRoleNames: '',
				roles: 0
			},
			currentUserRoles: [],
			currentUserAccessLogs: []
		},
		computed: {
			showAccessLogQueryPager: function(){
				if(this.userAccessLogQueryTotal == 0){
					return false;
				}else{
					return true;
				}
			}
		},
		created: function(){
			this.loadUserInfo();
			this.queryUserRoleList();
			this.queryUserAccessLogList(0);
		},
		methods: {
			getUserTypeTagType: function(userType){
				if(userType == 0){
					return "success";
				}else{
					return "primary";
				}
			},
			getStatusTagType: function(status){
				if(status == 0){
					return "danger";
				}else{
					return "success";
				}
			},
			getRoleTypeTagType: function(roleType){
				if(roleType == 0){
					return "success";
				}else{
					return "primary";
				}
			},
			formatProcessTime: function(row, column){
				return row.processTime1 + '毫秒';
			},
			loadUserInfo: function(){
				var _this = this;
				var url = ADMIN_CONTEXT_PATH + '/admin/user/info/current';
				axios.get(url).then(function(response){
					var result = response.data;
					if(result.success){
						_this.currentUser = result.data;
					}
				}).catch(function(error){
					_this.$message.error('请求出错!');
				});
			},
			queryUserRoleList: function(){
				var _this = this;
				var url = ADMIN_CONTEXT_PATH + '/admin/user/roles/current';
				axios.get(url).then(function(response){
					var result = response.data;
					if(result.success){
						_this.currentUserRoles = result.data;
					}else{
						_this.currentUserRoles = [];
					}
				}).catch(function(error){
					_this.$message.error('请求出错!');
					_this.currentUserRoles = [];
				});
			},
			queryUserAccessLogList: function(loading){
				this.userAccessLogQueryForm.currentPage = 1;
				this.doQueryUserAccessLogList(loading);
			},
			doQueryUserAccessLogList: function(loading){
				var _this = this;
				this.userAccessLogQueryTotal = 0;
				var url = ADMIN_CONTEXT_PATH + '/admin/user/accesslogs/current';
				if(loading){
					this.userAccessLogLoading = true;
				}
				setTimeout(function(){
					var params = Object.assign({}, _this.userAccessLogQueryForm, convertToOrderBy(_this.userAccessLogQuerySort));
					axios.get(url, {
						params: params
					}).then(function(response){
						var result = response.data;
						if(result.success){
							_this.currentUserAccessLogs = result.data;
							_this.userAccessLogQueryTotal = result.totalRowCount;
						}else{
							_this.currentUserAccessLogs = [];
							_this.userAccessLogQueryTotal = 0;
						}
						if(loading){
							_this.userAccessLogLoading = false;
						}
					}).catch(function(error){
						_this.$message.error('请求出错!');
						_this.userAccessLogLoading = false;
					});
				}, loading);
			},
			resetUserAccessLogQueryForm: function(){
				this.$refs.userAccessLogQueryForm.resetFields();
			},
			onUserAccessLogQuerySortChange: function(parameter){
				if(this.userAccessLogQuerySort.prop != parameter.prop || this.userAccessLogQuerySort.order != parameter.order){
					this.userAccessLogQuerySort.prop = parameter.prop;
					this.userAccessLogQuerySort.order = parameter.order;
					this.queryUserAccessLogList(1000);
				}
			},
			onUserAccessLogQueryCurrentPageChange: function(currentPage){
		    	this.userAccessLogQueryForm.currentPage = currentPage;
		    	this.doQueryUserAccessLogList(1000);
		    },
		    goToUserSettings: function(){
		    	window.location.href = ADMIN_CONTEXT_PATH + '/admin/user/settings.html';
		    }
		},
		watch: {
			'userAccessLogQueryForm.startAccessTime': function(curVal, oldVal){
				if(curVal){
					if(curVal instanceof Date){
						this.userAccessLogQueryForm.startAccessTime = moment(curVal).format('YYYY-MM-DD HH:mm:ss');
					}
				}
			},
			'userAccessLogQueryForm.endAccessTime': function(curVal, oldVal){
				if(curVal){
					if(curVal instanceof Date){
						this.userAccessLogQueryForm.endAccessTime = moment(curVal).format('YYYY-MM-DD HH:mm:ss');
					}
				}
			}
		}
	});
}, DEFAULT_VUE_RENDER_TIMEOUT);