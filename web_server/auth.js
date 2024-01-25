const crypto = require("crypto");
var jwt = require('jsonwebtoken');
require('dotenv').config();

const error = require("./error");

const TOKEN_EXPIRE_TIME = 60 * 24 * 7;   // 7 days
const JWT_ALGORITHM = "HS256";

const generateToken = (payload, key, algorithm, expiresIn)=>{
	return jwt.sign(payload, key, {algorithm, expiresIn});
}

const Auth = ()=>{
    const token = (payload)=>{
        const expiration = 60*TOKEN_EXPIRE_TIME;
        return generateToken(payload, process.env.SERVER_KEY||"", JWT_ALGORITHM, expiration);
    }
    const authenticate = token =>{
        try{
            token = token.replace("Bearer", "").trim();
            const result = jwt.verify(token, process.env.SERVER_KEY||"");
            return result;
        }catch(err){
            console.log(err);
            return systemError("Invalid access token", 401);
        }
    }

    return {token, authenticate}
}

module.exports = Auth;