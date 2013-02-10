function submitFund(id) {
	var form = document.createElement("form");
    form.setAttribute("method", "POST");
    form.setAttribute("action", "researchfund.do");
    
    var hiddenField = document.createElement("input");
    hiddenField.setAttribute("type", "hidden");
    hiddenField.setAttribute("name", "id");
    hiddenField.setAttribute("value", id);

    form.appendChild(hiddenField);
    document.body.appendChild(form);
    form.submit();
}

function submitCustomer(username) {
	var form = document.createElement("form");
    form.setAttribute("method", "POST");
    form.setAttribute("action", "viewcustomeraccount.do");
    
    var hiddenField = document.createElement("input");
    hiddenField.setAttribute("type", "hidden");
    hiddenField.setAttribute("name", "userName");
    hiddenField.setAttribute("value", username);

    form.appendChild(hiddenField);
    document.body.appendChild(form);
    form.submit();
}

function showCustomer(str) {
    var xmlhttp;    
    if (str=="") {
        document.getElementById("txtHint").innerHTML="";
        return;
    }
    if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    }
    else {
        // code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
    xmlhttp.onreadystatechange=function() {
        if (xmlhttp.readyState==4 && xmlhttp.status==200) {
            document.getElementById("txtHint").innerHTML=xmlhttp.responseText;
        }
    };
    xmlhttp.open("GET","getcustomers.do?query="+str,true);
    xmlhttp.send();
}

function showFund(str) {
    var xmlhttp;    
    if (str=="") {
        document.getElementById("txtHint").innerHTML="";
        return;
    }
    if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    }
    else {
        // code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
    xmlhttp.onreadystatechange=function() {
        if (xmlhttp.readyState==4 && xmlhttp.status==200) {
            document.getElementById("txtHint").innerHTML=xmlhttp.responseText;
        }
    };
    xmlhttp.open("POST","getfunds.do?query="+str,true);
    xmlhttp.send();
}


function sellfund(fundId) {
	var form = document.createElement("form");
    form.setAttribute("method", "POST");
    form.setAttribute("action", "cus_sellFund.do");
    
    var hiddenField = document.createElement("input");
    hiddenField.setAttribute("type", "hidden");
    hiddenField.setAttribute("name", "fundId");
    hiddenField.setAttribute("value", fundId);

    form.appendChild(hiddenField);
    document.body.appendChild(form);
    form.submit();
}