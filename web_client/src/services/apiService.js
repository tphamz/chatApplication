import axios from 'axios';

const URL = process.env.API_URL;

console.log("URL::" , URL);
const sendRequest = ({path, method="GET", body}) => 
axios(
	{
		method, 
		url: `${URL}/${path}`, 
		data: body, 
		headers: {'Content-Type': 'application/json'}
})
 .then(res=>res.data)
 .catch(err=>{
    console.log(err);
 	if(err.response) throw new Error(err.response.data);
 	throw new Error(err.message);
 });

export const register = ({username})=> sendRequest({method:"POST", path: `api/register`, body:{username}});


