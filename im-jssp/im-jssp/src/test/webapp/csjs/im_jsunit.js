function init() {
	if (!document.getElementsByTagName) {
		return;
	}
	var objs = document.getElementsByTagName("div");
  	for (i = 0; i < objs.length; i++) {
		if (objs[i].className == "titem") {
			objs[i].style.display = "none";
		}
	}
}
	
function func(id) {
	if (!document.getElementsByTagName) {
		return false;
	}
	
	var obj = document.getElementById(id);
	if (obj.style.display == "block") {
		obj.style.display = "none";
	} 
	else {
		obj.style.display = "block";
	}
	var img = document.getElementById("image_" + id);
	if (obj.style.display == "block") {
		img.src = "images/jsunit/jsunit_minus.gif";
	} 
	else {
		img.src = "images/jsunit/jsunit_plus.gif";
	}
	return false;
}

function func_suite(id) {
	if (!document.getElementsByTagName) {
		return false;
	}
	
	var obj = document.getElementById(id);
	if (obj.style.display == "block") {
		obj.style.display = "none";
	} 
	else {
		obj.style.display = "block";
	}
	var img = document.getElementById("image_" + id);
	if (obj.style.display == "block") {
		img.src = "images/jsunit/jsunit_suite_minus.gif";
	} 
	else {
		img.src = "images/jsunit/jsunit_suite_plus.gif";
	}
	return false;
}
