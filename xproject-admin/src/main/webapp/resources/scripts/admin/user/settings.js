var IMAGE_TYPES = ['image/jpeg', 'image/jpg', 'image/png'];

var vm = null;
Vue.onDocumentReady(function() {
	vm = new Vue({
		el: '#app',
		data: {
			submiting: false,
			userEditActiveTabName: 'userEdit',
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
			editDialogVisible: false,
			uploadHeaders: {
				Accept: 'application/json'
			},
			userIconUploadUrl: ADMIN_CONTEXT_PATH + '/upload/image/submit',
			userIconUploadParam: {
				formatLimit: 'jpeg,jpg,png',
				pixelLimit: '90x90'
			},
			userEditForm: {
				realName: '',
				mobilePhone: '',
				email: '',
				userIcon: '',
				userIconUrl: ''
			},
			changePwdForm: {
				oldpassword: '',
				password: '',
				repassword: ''
			},
			userEditRules: {
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
			changePwdRules: {
				oldpassword: [{
					required: true,
					message: '请输入原密码!'
				},{
					validator: regex,
					regex: /^[a-zA-Z0-9]{6,20}$/,
					message: '账户密码由6~20个字母或数字组成!'
				}],
				password: [{
					required: true,
					message: '请输入新密码!'
				},{
					validator: regex,
					regex: /^[a-zA-Z0-9]{6,20}$/,
					message: '账户密码由6~20个字母或数字组成!'
				}],
				repassword: [{
					required: true,
					message: '请再次输入新密码!'
				},{
					validator: regex,
					regex: /^[a-zA-Z0-9]{6,20}$/,
					message: '账户密码由6~20个字母或数字组成!'
				},{
					validator: equalTo,
					compareTarget: {
						vue: 'vm',
						form: 'changePwdForm',
						field: 'password'
					},
					message: '两次密码不一致!'
				}]
			}
		},
		computed: {
		},
		created: function(){
			this.loadUserInfo();
		},
		methods: {
			loadUserInfo: function(){
				var _this = this;
				var url = ADMIN_CONTEXT_PATH + '/admin/user/info/current';
				axios.get(url).then(function(response){
					var result = response.data;
					if(result.success){
						_this.currentUser = result.data;
						_this.userEditForm.userName = _this.currentUser.userName;
						_this.userEditForm.realName = _this.currentUser.realName;
						_this.userEditForm.mobilePhone = _this.currentUser.mobilePhone;
						_this.userEditForm.email = _this.currentUser.email;
						_this.userEditForm.userIcon = _this.currentUser.userIcon;
						_this.userEditForm.userIconUrl = _this.currentUser.userIconUrl;
					}
				}).catch(function(error){
					_this.$message.error('请求出错!');
				});
			},
			saveUser: function(){
		    	var url = ADMIN_CONTEXT_PATH + '/admin/user/edit/current';
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
									_this.loadUserInfo();
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
		    },
		    changePasswd: function(){
		    	var url = ADMIN_CONTEXT_PATH + '/admin/user/changepwd/current';
				var _this = this;
				this.$refs.changePwdForm.validate(function(valid){
					if(valid && !_this.submiting){
						_this.submiting = true;
						setTimeout(function(){
							axios.post(url, _this.changePwdForm).then(function(response){
								_this.submiting = false;
								var result = response.data;
								if(result.success){
									_this.$alert('登录密码修改成功，您需要重新登录！', '提示', {
										type: 'success',
										confirmButtonText: '确定',
										callback: function(action){
											window.top.location.href = ADMIN_CONTEXT_PATH + '/logout';
										}
									});
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
		}
	});
}, DEFAULT_VUE_RENDER_TIMEOUT);