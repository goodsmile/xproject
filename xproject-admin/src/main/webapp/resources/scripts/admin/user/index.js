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
			uploadHeaders: {
				Accept: 'application/json'
			},
			userIconUploadUrl: ADMIN_CONTEXT_PATH + '/upload/image/submit',
			userIconUploadParam: {
				formatLimit: 'jpeg,jpg,png',
				pixelLimit: '90x90'
			},
			userRoleList: [],
			loadingUserRoleList: false,
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
				userIconUrl: ''
			},
			userViewConfigForm: {
				userId: '',
				userName: '',
				realName: '',
				mobilePhone: '',
				email: '',
				userIcon: '',
				userIconUrl: '',
				status: '',
				statusName: '',
				userType: '',
				userTypeName: '',
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
						field: 'password'
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
			},
			roleQuering: false,
			userRoleQueryForm: {
				userId: '',
				roleName: '',
				roleCode: ''
			},
			roleQueryForm: {
				roleName: '',
				roleCode: '',
				pageSize: 10,
				currentPage: 1,
				orderby: 'createTime',
				order: 'desc'
			},
			roleList: [],
			selectedRoleIds: [],
			loadingRoleList: false,
			roleQueryTotal: 0
		},
		computed: {
			showUserQueryPager: function(){
				if(this.userQuering || this.userQueryTotal == 0){
					return false;
				}else{
					return true;
				}
			},
			showRoleQueryPager: function(){
				if(this.roleQuering || this.roleQueryTotal == 0){
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
			getRoleTypeTagType: function(roleType){
				if(roleType == 0){
					return "success";
				}else{
					return "primary";
				}
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
			onUserQueryPageSizeChange: function(event, pageSize){
		    	this.userQueryForm.pageSize = pageSize;
		    	this.queryUserList(1000);
		    },
		    onUserQueryCurrentPageChange: function(currentPage){
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
		    		this.userEditForm.realName = row.realName;
		    		this.userEditForm.mobilePhone = row.mobilePhone;
		    		this.userEditForm.email = row.email;
		    		this.userEditForm.userIcon = row.userIcon;
		    		this.userEditForm.userIconUrl = row.userIconUrl;
		    	}
		    	this.editDialogVisible = true;
		    },
		    openUserViewConfigDialog: function(cmd, row){
		    	this.currentActionType = cmd;
		    	this.userViewConfigForm.userId = row.userId;
		    	this.userViewConfigForm.userName = row.userName;
	    		this.userViewConfigForm.realName = row.realName;
	    		this.userViewConfigForm.mobilePhone = row.mobilePhone;
	    		this.userViewConfigForm.email = row.email;
	    		this.userViewConfigForm.userIcon = row.userIcon;
	    		this.userViewConfigForm.userIconUrl = row.userIconUrl;
	    		this.userViewConfigForm.userType = row.userType;
	    		this.userViewConfigForm.userTypeName = row.userTypeName;
	    		this.userViewConfigForm.status = row.status;
	    		this.userViewConfigForm.statusName = row.statusName;
	    		this.userViewConfigForm.lastLoginTime = row.lastLoginTime;
	    		this.userViewConfigForm.loginTimes = row.loginTimes;
	    		this.userViewConfigForm.createBy = row.createBy;
	    		this.userViewConfigForm.createTime = row.createTime;
	    		this.userViewConfigForm.updateBy = row.updateBy;
	    		this.userViewConfigForm.updateTime = row.updateTime;
	    		if(cmd == 'conf'){
	    			this.viewConfigActiveTabName = 'userRoleConfig';
	    			this.queryUserRoleList(1500);
	    		}else{
	    			this.viewConfigActiveTabName = 'userDetail';
	    			this.queryUserRoleList(0);
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
		    closeEditDialog: function(){
				this.editDialogVisible = false;
				this.$refs.userEditForm.resetFields();
			},
			queryUserRoleList: function(loading){
				this.userRoleQueryForm.userId = this.userViewConfigForm.userId;
				var _this = this;
				var url = ADMIN_CONTEXT_PATH + '/admin/user/roles';
				if(loading){
					this.loadingUserRoleList = true;
				}
				setTimeout(function(){
					axios.get(url, {
						params: _this.userRoleQueryForm
					}).then(function(response){
						var result = response.data;
						if(result.success){
							_this.userRoleList = result.data;
						}else{
							_this.userRoleList = [];
						}
						if(loading){
							_this.loadingUserRoleList = false;
						}
					}).catch(function(error){
						_this.$message.error('请求出错!');
						_this.userRoleList = [];
						if(loading){
							_this.loadingUserRoleList = false;
						}
					});
				}, loading);
			},
			resetUserRoleQueryForm: function(){
				this.$refs.userRoleQueryForm.resetFields();
			},
			showUserRoleAddTab: function(){
				this.viewConfigActiveTabName = 'userRoleAdd';
			},
			onRoleQueryCurrentPageChange: function(currentPage){
		    	this.roleQueryForm.currentPage = currentPage;
		    	this.doQueryRoleList(1000);
		    },
		    queryRoleList: function(loading){
				this.roleQueryForm.currentPage = 1;
				this.doQueryRoleList(loading);
			},
			doQueryRoleList: function(loading){
				var _this = this;
				this.roleQuering = true;
				var url = ADMIN_CONTEXT_PATH + '/admin/role/list';
				if(loading){
					this.loadingRoleList = true;
				}
				setTimeout(function(){
					var params = _this.roleQueryForm;
					axios.get(url, {
						params: params
					}).then(function(response){
						var result = response.data;
						if(result.success){
							_this.roleList = result.data;
							_this.roleQueryTotal = result.totalRowCount;
						}else{
							_this.roleList = [];
							_this.roleQueryTotal = 0;
						}
						if(loading){
							_this.loadingRoleList = false;
						}
						_this.roleQuering = false;
					}).catch(function(error){
						_this.$message.error('请求出错!');
						_this.loadingRoleList = false;
						_this.roleQuering = false;
					});
				}, loading);
			},
		    resetRoleQueryForm: function(){
				this.$refs.roleQueryForm.resetFields();
			},
			closeUserViewConfigDialog: function(){
				this.viewConfigDialogVisible = false;
				this.resetUserRoleQueryForm();
				this.userRoleList = [];
				if(this.currentActionType == 'conf'){
					this.resetRoleQueryForm();
					this.roleList = [];
					this.selectedRoleIds = [];
				}
			},
			delUserRoleConfig: function(){
				if(this.selectedRoleIds.length){
					var userId = this.userViewConfigForm.userId;
		    		var url = ADMIN_CONTEXT_PATH + "/admin/user/config/del";
		    		if(!this.submiting){
		    			this.submiting = true;
		    			var _this = this;
						setTimeout(function(){
							axios.post(url, {
								userId: userId,
								roleIds: _this.selectedRoleIds.join(',')
							}).then(function(response){
								_this.submiting = false;
								var result = response.data;
								if(result.success){
									_this.$message.success('删除成功!');
									_this.queryUserRoleList(1000);
								}else{
									_this.$message.error(result.message);
								}
							}).catch(function(error){
								_this.submiting = false;
								_this.$message.error('请求出错!');
							});
						}, 1500);
					}
				}else{
					this.$message.error('请选择需要移除的角色!');
				}
			},
			addUserRoleConfig: function(){
				if(this.selectedRoleIds.length){
					var userId = this.userViewConfigForm.userId;
		    		var url = ADMIN_CONTEXT_PATH + "/admin/user/config/add";
		    		if(!this.submiting){
		    			this.submiting = true;
		    			var _this = this;
						setTimeout(function(){
							axios.post(url, {
								userId: userId,
								roleIds: _this.selectedRoleIds.join(',')
							}).then(function(response){
								_this.submiting = false;
								var result = response.data;
								if(result.success){
									_this.$message.success('添加成功!');
									_this.viewConfigActiveTabName = 'userRoleConfig';
									Vue.nextTick(function(){
										_this.queryUserRoleList(1000);
										_this.$refs.roleTable.clearSelection();
									});
								}else{
									_this.$message.error(result.message);
								}
							}).catch(function(error){
								_this.submiting = false;
								_this.$message.error('请求出错!');
							});
						}, 1500);
					}
				}else{
					this.$message.error('请选择需要添加的角色!');
				}
			},
			onUserRoleSelectionChange: function(selectedRows){
				if(selectedRows && selectedRows.length){
					this.selectedRoleIds = [];
					selectedRows.forEach(function(row){
						this.selectedRoleIds.push(row.roleId);
					}, this);
				}else{
					this.selectedRoleIds = [];
				}
			},
			onRoleSelectionChange: function(selectedRows){
				if(selectedRows && selectedRows.length){
					this.selectedRoleIds = [];
					selectedRows.forEach(function(row){
						this.selectedRoleIds.push(row.roleId);
					}, this);
				}else{
					this.selectedRoleIds = [];
				}
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
				this.userEditForm.userIcon = response.data.path;
				this.userEditForm.userIconUrl = response.data.url;
			},
			handleUserIconRemove: function(file, fileList){
				if(file && file.path){
					axios.get(ADMIN_CONTEXT_PATH + '/upload/remove/submit.do?path=' + file.path);
				}
			},
			handleUploadError: function(error, file, fileList){
				try {
					var result = JSON.parse(error.message);
					this.$message.error(result.message);
				} catch (e) {
					this.$message.error(error.message);
				}
			}
		},
		watch: {
			viewConfigActiveTabName: function(curVal, oldVal){
				this.selectedRoleIds = [];
			}
		}
	});
}, DEFAULT_VUE_RENDER_TIMEOUT);