function validateStrings(form){
	const badWords = "select.*from|delete.*from|update.*set|http|https|porno|viagra";
	const re = new RegExp(badWords);
	let inputs = form.querySelectorAll('input[type=text]');
	for (var i = 0; i < inputs.length;i++){
		if (re.test(inputs[i].value)){
			alert("bad Boy");
			return false;
		}
	}
	return true;
}
