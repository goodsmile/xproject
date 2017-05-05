var ADMIN_USER_TYPE_SYSTEM = 0;
var IMAGE_TYPES = ['image/jpeg', 'image/jpg', 'image/png'];
var vm = null;
Vue.onDocumentReady(function() {
	vm = new Vue({
		el: '#app',
		data: {
			userQuering: false,
			submiting: false,
			loadingUserList: false,
			userQueryForm: {
				userName: '',
				realName: '',
				userType: '',
				status: '',
				pageSize: 10,
				currentPage: 1
			},
			userQuerySort: {
				prop: 'createTime',
				order: 'descending'
			},
			userQueryTotal: 0,
			userList: [],
			editDialogVisible: false,
			viewConfigDialogVisible: false,
			viewConfigActiveTabName: 'userDetail',
			userIconUploadUrl: ADMIN_CONTEXT_PATH + '/upload/image/submit',
			userIconUploadParam: {
				formatLimit: 'jpeg,jpg,png',
				pixelLimit: '90x90'
			},
			userRoleDataList: [],
			checkedRoleIdList: [],
			loadingUserRoleDataList: false,
			currentActionType: 'add',
			userEditForm: {
				userId: '',
				userName: '',
				password: '',
				repassword: '',
				realName: '',
				mobilePhone: '',
				email: '',
				userIcon: '',
				status: ''
			},
			userViewConfigForm: {
				userId: '',
				userName: '',
				realName: '',
				mobilePhone: '',
				email: '',
				userIcon: '',
				status: '',
				userType: '',
				createBy: '',
				createTime: '',
				updateBy: '',
				updateTime: '',
				lastLoginTime: '',
				loginTimes: ''
			},
			userEditRules: {
				userName: [{
					required: true,
					message: '请输入用户名!'
				},{
					validator: regex,
					regex: /^[a-zA-Z]{1}[a-zA-Z0-9_]{4,19}$/,
					message: '用户名必须由字母开头,5~20个字母、数字及下划线组成!'
				}],
				password: [{
					required: true,
					message: '请输入账户密码!'
				},{
					validator: regex,
					regex: /^[a-zA-Z0-9]{6,20}$/,
					message: '账户密码由6~20个字母或数字组成!'
				}],
				repassword: [{
					required: true,
					message: '请再次输入账户密码!'
				},{
					validator: regex,
					regex: /^[a-zA-Z0-9]{6,20}$/,
					message: '账户密码由6~20个字母或数字组成!'
				},{
					validator: equalTo,
					compareTarget: {
						vue: 'vm',
						form: 'userEditForm',
						field: 'repassword'
					},
					message: '两次密码不一致!'
				}],
				realName: [{
					required: true,
					message: '请输入姓名!'
				}],
				mobilePhone: [{
					required: true,
					message: '请输入手机号码!'
				},{
					validator: regex,
					regex: /^1[0-9]{2}[0-9]{8}$/,
					message: '手机号码不合法!'
				}],
				email: [{
					required: true,
					message: '请输入EMAIL!'
				},{
					validator: email,
					message: 'EMAIL不合法!'
				}]
			}
		},
		computed: {
			showPager: function(){
				if(this.userQuering || this.userQueryTotal == 0){
					return false;
				}else{
					return true;
				}
			},
			editDialogTitle: function(){
				if(this.currentActionType == 'add'){
					return '新建用户';
				}else if(this.currentActionType == 'edit'){
					return '修改用户';
				}else{
					return '';
				}
			},
			viewConfigDialogTitle: function(){
				if(this.currentActionType == 'view'){
					return '查看用户详情';
				}else if(this.currentActionType == 'conf'){
					return '用户角色配置';
				}else{
					return '';
				}
			}
		},
		created: function(){
			this.queryUserList(0);
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
			getUserIconUrl: function(userIcon){
				if(userIcon){
					if(userIcon.indexOf('http') == 0){
						userIcon = userIcon;
					}else{
						userIcon = ADMIN_CONTEXT_PATH + userIcon;
					}
				}
				return userIcon;
			},
			queryUserList: function(loading){
				this.userQueryForm.currentPage = 1;
				this.doQueryUserList(loading);
			},
			doQueryUserList: function(loading){
				var _this = this;
				this.userQuering = true;
				var url = ADMIN_CONTEXT_PATH + '/admin/user/list';
				if(loading){
					this.loadingUserList = true;
				}
				setTimeout(function(){
					var params = Object.assign({}, _this.userQueryForm, convertToOrderBy(_this.userQuerySort));
					axios.get(url, {
						params: params
					}).then(function(response){
						var result = response.data;
						if(result.success){
							_this.userList = result.data;
							_this.userQueryTotal = result.totalRowCount;
						}else{
							_this.userList = [];
							_this.userQueryTotal = 0;
						}
						if(loading){
							_this.loadingUserList = false;
						}
						_this.userQuering = false;
					}).catch(function(error){
						_this.$message.error('请求出错!');
						_this.loadingUserList = false;
						_this.userQuering = false;
					});
				}, loading);
			},
			onUserQuerySortChange: function(parameter){
				if(this.userQuerySort.prop != parameter.prop || this.userQuerySort.order != parameter.order){
					this.userQuerySort.prop = parameter.prop;
					this.userQuerySort.order = parameter.order;
					this.queryUserList(1000);
				}
			},
			resetQueryForm: function(){
				this.$refs.userQueryForm.resetFields();
			},
			onPageSizeChange: function(event, pageSize){
		    	this.userQueryForm.pageSize = pageSize;
		    	this.queryUserList(1000);
		    },
		    onCurrentPageChange: function(currentPage){
		    	this.userQueryForm.currentPage = currentPage;
		    	this.doQueryUserList(1000);
		    },
		    handleUserCmd: function(cmd, row){
		    	if(cmd == 'edit'){
		    		this.openUserEditDialog(cmd, row);
		    	}else if(cmd == 'view'){
		    		this.openUserViewConfigDialog(cmd, row);
		    	}else if(cmd == 'del'){
		    		this.deleteUser(row);
		    	}else if(cmd == 'conf'){
		    		this.openUserViewConfigDialog(cmd, row);
		    	}
		    },
		    openUserEditDialog: function(cmd, row){
		    	this.currentActionType = cmd;
		    	if(cmd == 'edit'){
		    		this.userEditForm.userId = row.userId;
		    		this.userEditForm.userName = row.userName;
		    		this.userEditForm.userCode = row.userCode;
		    		this.userEditForm.description = row.description;
		    	}
		    	this.editDialogVisible = true;
		    },
		    openUserViewConfigDialog: function(cmd, row){
		    	this.currentActionType = cmd;
		    	this.userViewConfigForm.userId = row.userId;
		    	this.userViewConfigForm.userName = row.userName;
	    		this.userViewConfigForm.userCode = row.userCode;
	    		this.userViewConfigForm.description = row.description;
	    		this.userViewConfigForm.userType = row.userType;
	    		this.userViewConfigForm.createBy = row.createBy;
	    		this.userViewConfigForm.createTime = row.createTime;
	    		this.userViewConfigForm.updateBy = row.updateBy;
	    		this.userViewConfigForm.updateTime = row.updateTime;
	    		if(cmd == 'conf'){
	    			this.viewConfigActiveTabName = 'userRoleConfig';
	    			this.loadUserRoleDataList(1500, row.userId);
	    		}else{
	    			this.viewConfigActiveTabName = 'userDetail';
	    			this.loadUserRoleDataList(0, row.userId);
	    		}
	    		this.viewConfigDialogVisible = true;
		    },
		    saveUser: function(){
		    	var url = '';
				if(this.currentActionType == 'add'){
					url = ADMIN_CONTEXT_PATH + '/admin/user/add/submit';
				}else if(this.currentActionType == 'edit'){
					url = ADMIN_CONTEXT_PATH + '/admin/user/edit/submit';
				}
				if(url){
					var _this = this;
					this.$refs.userEditForm.validate(function(valid){
						if(valid && !_this.submiting){
							_this.submiting = true;
							setTimeout(function(){
								axios.post(url, _this.userEditForm).then(function(response){
									_this.submiting = false;
									var result = response.data;
									if(result.success){
										_this.$message.success('保存成功!');
										_this.closeEditDialog();
										_this.queryUserList(1000);
									}else{
										_this.$message.error(result.message);
									}
								}).catch(function(error){
									_this.submiting = false;
									_this.$message.error('请求出错!');
								});
							}, 1500);
						}else{
							return false;
						}
					});
				}
		    },
		    deleteUser: function(row){
		    	if(ADMIN_USER_TYPE_SYSTEM == row.userType){
					this.$message.error('系统用户不能删除!');
				}else{
					var url = ADMIN_CONTEXT_PATH + '/admin/user/del?id=' + row.userId;
					var _this = this;
					this.$confirm('你确定要删除该用户?', '提示', {
						confirmButtonText: '确定',
				        cancelButtonText: '取消',
				        type: 'warning',
				        callback: function(action, instance){
							if(action == 'confirm'){
								axios.get(url).then(function(response){
									var result = response.data;
									if(result.success){
										_this.$message.success('删除成功!');
										_this.queryUserList(1000);
									}else{
										_this.$message.error(result.message);
									}
								}).catch(function(error){
									_this.$message.error('请求出错!');
								});
							}
						}
					});
				}
		    },
		    saveUserRoleConfig: function(){
		    	var userId = this.userViewConfigForm.userId;
		    	var checkedKeys = this.$refs.userRoleViewTree.getCheckedKeys();
		    	var checkedRoleIds = checkedKeys.sort().join(",");
		    	if(checkedRoleIds == this.checkedRoleIdList.sort().join(",")){
		    		this.$message.warning('配置没有改变,无需保存!');
		    	}else{
		    		var url = ADMIN_CONTEXT_PATH + "/admin/user/config/submit";
		    		if(!this.submiting){
		    			this.submiting = true;
		    			var _this = this;
						setTimeout(function(){
							axios.post(url, {
								userId: userId,
								resourceIds: checkedRoleIds
							}).then(function(response){
								_this.submiting = false;
								var result = response.data;
								if(result.success){
									_this.$message.success('保存成功!');
									_this.loadUserRoleDataList(1500, userId);
								}else{
									_this.$message.error(result.message);
								}
							}).catch(function(error){
								_this.submiting = false;
								_this.$message.error('请求出错!');
							});
						}, 1500);
					}
		    	}
		    },
		    closeEditDialog: function(){
				if(this.editDialogVisible){
					this.editDialogVisible = false;
				}
				this.$refs.userEditForm.resetFields();
			},
			loadUserRoleDataList: function(loading, userId){
				var _this = this;
				var url = ADMIN_CONTEXT_PATH + '/admin/user/roles?userId=' + userId;
				if(loading){
					this.loadingUserRoleDataList = true;
				}
				setTimeout(function(){
					axios.get(url).then(function(response){
						var result = response.data;
						if(result.success){
							_this.userRoleDataList = result.data.allRoleList;
							_this.checkedRoleIdList = result.data.checkedRoleIdList;
							Vue.nextTick(function(){
								_this.$refs.userRoleViewTree.setCheckedKeys(result.data.checkedRoleIdList);
							});
						}else{
							_this.userRoleDataList = [];
							_this.checkedRoleIdList = [];
						}
						if(loading){
							_this.loadingUserRoleDataList = false;
						}
					}).catch(function(error){
						_this.$message.error('请求出错!');
						_this.userRoleDataList = [];
						_this.checkedRoleIdList = [];
						if(loading){
							_this.loadingUserRoleDataList = false;
						}
					});
				}, loading);
			},
			beforeUserIconUpload: function(file){
				var fileType = file.type;
				if(IMAGE_TYPES.indexOf(fileType) == -1){
					this.$message.error('上传图片只能是jpg、png格式!');
					return false;
				}
				if(file.size > 0 && (file.size / 1024) > 50){
					this.$message.error('上传图片大小不能超过50kb!');
					return false;
				}
				return true;
			},
			handleUserIconUploadSuccess: function(response, file, fileList){
				file.url = response.data.url;
				file.path = response.data.path;
				this.userEditForm.userIcon = response.data.url;
			},
			handleUserIconRemove: function(file, fileList){
				axios.get(ADMIN_CONTEXT_PATH + '/upload/remove/submit.do?path=' + file.path);
			},
			handleUploadError: function(error, response, file){
				this.$message.error(response.message);
			}
		}
	});
}, DEFAULT_VUE_RENDER_TIMEOUT);