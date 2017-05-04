
new Vue({
	el: '#app',
	data: {
		errorMessage: '',
		submiting: false,
		loginForm: {
			userName: '',
			password: ''
		},
		loginRules: {
			userName: [{
				required: true,
				message: '请输入用户名!'
			}],
			password: [{
				required: true,
				message: '请输入密码!'
			}]
		}
	},
	methods: {
		submitLogin: function(){
			var $this = this;
			this.$refs.loginForm.validate(function(valid){
				if(valid && !$this.submiting){
					$this.submiting = true;
					setTimeout(function(){
						axios.post(ADMIN_CONTEXT_PATH + '/login/submit', $this.loginForm).then(function(response){
							var result = response.data;
							if(result.success){
								window.location.href = ADMIN_CONTEXT_PATH + '/index';
							}else{
								$this.submiting = false;
								$this.errorMessage = result.message;
							}
						}).catch(function(error){
							$this.submiting = false;
							$this.$message.error('请求出错!');
						});
					}, 1500);
				}else{
					$this.errorMessage = '';
					return false;
				}
			});
		},
		submitLogin2: function(){
			alert(1234);
		}
	}
});