//Programmer:   Ryan Admire
//File:         RestTemplate.js
//Description:  Template of a REST request that can change a few values to be used for any type of REST request
//              Node.js could be utilized to allow for command line arguments but of course would be server side and not client side
//              Angular or another framework could be used to simplify the below with built in functionality

//FUNCTIONS AND CALLBACKS

//Main callback function for when the request goes smoothly. Can be altered to do whatever operation desired
function printMessage(){
	console.log("We did it!");
}

//Callback function for request success. Will point to the main callback function
function onSuccess(){
	this.callback.apply(this);
}

//Callback function for when the request errors. Can be altered to do whatever operation desired
function onError(){
	console.error("We dun goofed!");
}

//Timeout callback function for when the request times out
function timesUp(){
	console.error("Request timed out while attempting to execute.");
}

//Function will wrap around our call so that callbacks can be altered without affecting the main body of code
//Will also handle request timeouts
function restRequest(method, url, payload, callback){
	
	//Initialize variables
	var httpMethod = method;         //What type of REST Request is being made
	var endPoint = url;              //To what location is the request being made
	var content = payload;           //What content, if any, is being sent with the request
	var req = new XMLHttpRequest();  //The request that will be made

	//Set the request with the passed in(or set) parameters
	req.callback = callback;               //Main callback function that will handle the response from the request
	req.onload = onSuccess;                //callback for when the request succeeds
	req.onerror = onError;                 //callback for when the request fails
	req.ontimeout = timesUp;               //callback for when the request times out
	req.open(httpMethod, endPoint, true);  //Last parameter must be true to indicate the request is asynchronous
	req.send(null);                        //send the request
}

//MAIN BODY

//call the function execute the request
restRequest(method, url, payload, printMessage); 
