function validateStrings(form){
	const badWords = ['delete','select','rm','update'];
	let inputs = form.querySelectorAll('input[type=text]');
	for (var i = 0; i < inputs.length;i++){
		if (badWords.includes(inputs[i].value)){
			alert("bad Boy");
			return false;
		}
	}
	return true;
}
