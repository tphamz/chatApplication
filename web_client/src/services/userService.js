import React from 'react';
import {register} from "./../services/apiService";
const TOKEN_KEY = "auth_token";
const USER_KEY = "username";
const AccountContext = React.createContext();

const AccountProvider = ({children})=>{
	const [accessToken, setAccessToken] = React.useState(window.localStorage.getItem(TOKEN_KEY));
	const user = window.localStorage.getItem(USER_KEY);	
	const isLoggedIn = ()=>(accessToken?true:false);
	
	
	const setAccount = (user={})=>{
		window.localStorage.setItem(TOKEN_KEY, user.token||"");
		window.localStorage.setItem(USER_KEY, user.username||"");
		setAccessToken(user.token);
	}

	const login = async (data)=>{
		const res = await register(data).catch(err=>logout());
		setAccount(res);
	}	

	const logout = ()=>{
		setAccount();
	}

	return (<AccountContext.Provider value={{isLoggedIn, login, logout, user, accessToken}}>
		{children}
	</AccountContext.Provider>);
}

export {AccountProvider, AccountContext}; 