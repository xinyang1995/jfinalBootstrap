/**
 * 首页相关js
 */
  $(function(){
    	
	  	//切换iframe页面
    	new Vue({
		  el: '.menu',
		  methods: {
		    menu: function (url) {
		    	$('iframe').attr('src',url);
		    }
		  }
		})
    	
    	//退出登录
		new Vue({
		  el: '#logout',
		  methods: {
		    logout: function (base) {
		    	$.ajax({
			       url:"./logout",
			       type:"post",
			       dataType:"json",
			       data:"",
			       success:function(data){
			          if(data.status == 1) {
			        	//退出成功
			             window.location.href=base;
				       }else{
			        	//退出失败
			        	alert("登陆失败，请重新退出");
			          }
			       }
			    });
		    }
		  }
		})
    	
  })