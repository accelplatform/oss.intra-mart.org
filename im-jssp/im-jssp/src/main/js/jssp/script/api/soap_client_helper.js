function normalize(value){
    if(!isJavaInstance(value)){
        return value;
    }
    else if(value instanceof Packages.java.lang.String){
        return value + "";
    }
    else if(value instanceof Packages.java.lang.Number){
        return value - 0;
    }
    else if(value instanceof Packages.java.lang.Boolean){
        return value == true;
    }
    else{
        return value;
    }
}

function setTimeOutInMilliSeconds(timeOutInMilliSeconds){
	this.stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(timeOutInMilliSeconds);
}

function SOAPFaultError(err){
	var axisFaultClassName = "org.apache.axis2.AxisFault";

	if(err.javaException == undefined 
	   ||
	   err.javaException.getClass().getName() != axisFaultClassName){

	   	var errMsg = "SOAPFaultError is '" + axisFaultClassName + "' wrapper. (Cannot wrap " + err.javaException + ".)";
		return new TypeError(errMsg);
	}

	this.name          = "SOAPFaultError";
	this.fileName      = err.fileName;
	this.lineNumber    = err.lineNumber;
	this.javaException = err.javaException;
	
	// getMessage() : java.lang.String
	this.message = normalize(this.javaException.getMessage());

	// getReason() : java.lang.String
	// getFaultReasonElement() : org.apache.axiom.soap.SOAPFaultReason
	this.reason = normalize(this.javaException.getReason());

	// getFaultCode() : javax.xml.namespace.QName
	// getFaultCodeElement() : org.apache.axiom.soap.SOAPFaultCode
	var tmpFaultCode = this.javaException.getFaultCode();
	this.faultCode = (tmpFaultCode != null) ? normalize(tmpFaultCode.toString()) : null;

	// getFaultSubCodes() : java.util.List
	// --- 'faultSubCodes' is not defined. --- 

	// getDetail() : org.apache.axiom.om.OMElement
	// getFaultDetailElement() : org.apache.axiom.soap.SOAPFaultDetail
	var tmpDetail = this.javaException.getDetail();
	this.detail = (tmpDetail != null) ? normalize(tmpDetail.toString()) : null;

	// getFaultNode() : java.lang.String
	// getFaultNodeElement() : org.apache.axiom.soap.SOAPFaultNode
	this.faultNode = normalize(this.javaException.getFaultNode());

	// getFaultRole() : java.lang.String
	// getFaultRoleElement() : org.apache.axiom.soap.SOAPFaultRole
	this.faultRole = normalize(this.javaException.getFaultRole());

	// getFaultAction() : java.lang.String
	this.faultAction = normalize(this.javaException.getFaultAction());

	// getNodeURI() : java.lang.String
	this.nodeURI = normalize(this.javaException.getNodeURI());
	
}

SOAPFaultError.prototype = Error.prototype;