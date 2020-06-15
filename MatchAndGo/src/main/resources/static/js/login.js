
document.addEventListener("DOMContentLoaded", () => {
	document.getElementById("txt_email").addEventListener("focus",function() {
		let pass1 = document.getElementById("txt_password").value;
		let pass2 = document.getElementById("txt_password2").value;

		if(pass1 != pass2){
			document.getElementById("txt_password2").classList.add("inputRed");
			document.getElementById("txt_password2").classList.remove("inputGreen");
		}else{
			document.getElementById("txt_password2").classList.add("inputGreen");
			document.getElementById("txt_password2").classList.remove("inputRed");
		}
	});
	
	document.getElementById("txt_password").addEventListener("focus",function() {
		let usernameInput = document.getElementById("txt_username").value;

		go("/user/userName","POST",{username:usernameInput}).then(result =>checkUsername(result));
		
	});
	
});


function checkUsername(result){
	if(result == 0){//no puedo crear xq existe el username
		document.getElementById("txt_username").classList.add("inputRed");
	}else{
		document.getElementById("txt_username").classList.remove("inputRed");
	}
}
	
	