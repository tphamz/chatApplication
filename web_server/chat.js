const Auth = require("./auth");

const CONNECTION_ERROR_EVT = "connection_error";
const CONNECTED_EVT = "connection"; 
const DISCONNECTED_EVT = "disconnect";
const USER_CONNECTED = "socket::USER_CONNECTED";
const USER_DISCONNECTED = "socket::USER_DISCONNECTED";
const LIST_USERS = "socket::USERS";

const PRIVATE_CHANNEL = "socket::PRIVATE_CHANNEL";
const PUBLIC_CHANNEL = "socket::PUBLIC_CHANNEL";

const auth = Auth();
const users = new Map();

const Chat = (io)=>{
    io.use((socket, next) => {
        const token = socket.handshake.auth.token;
        if (!token) 
          return next(new Error("invalid token"));
        try{
          const user = auth.authenticate(token);
          socket.user = {...user, socketId: socket.id};
        }catch(err){
          return next(err);
        }
        next();
    });
    
    io.on(CONNECTED_EVT, socket=>{
        console.log("user connected::", socket.user.username);
        users.set(socket.user.id,socket.user);
        socket.broadcast.emit(USER_CONNECTED, socket.user);
        io.emit(LIST_USERS, Array.from(users.values()));
        socket.on(DISCONNECTED_EVT, evt=>{
            console.log("user disconnected::", socket.user.username);
            users.delete(socket.user.id);
            socket.broadcast.emit(LIST_USERS, Array.from(users.values()));
        });

        socket.on(PRIVATE_CHANNEL, ({to, content})=>{
            socket.to(to).emit(PRIVATE_CHANNEL, {content, from: socket.user});
        });

        socket.on(PUBLIC_CHANNEL, ({content})=>{
            socket.broadcast.emit(PUBLIC_CHANNEL, {content, username: socket.user.username});
        });
    });
}

module.exports = Chat;