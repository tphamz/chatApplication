import { io } from "socket.io-client";
const URL = process.env.API_URL;
const CONNECTION_ERROR_EVT = "connect_error";
const CONNECTED_EVT = "connect"; 
const DISCONNECTED_EVT = "disconnect";
const USER_CONNECTED = "socket::USER_CONNECTED";
const USER_DISCONNECTED = "socket::USER_DISCONNECTED";
const LIST_USERS = "socket::USERS";

const PRIVATE_CHANNEL = "socket::PRIVATE_CHANNEL";
const PUBLIC_CHANNEL = "socket::PUBLIC_CHANNEL";

export default function ChatService(){
    const socket = io(URL, { autoConnect: false, transports: ['websocket'] });
    const subscribeError = (callback)=>socket.on("connection_error", callback);
    const unsubscribeError = ()=>socket.off("connection_error");
    const setAuth = token=> socket.auth = {token};

    const connect = ()=> socket.connect();
    const disconnect = ()=>socket.disconnect();
    const onConnected = (callback)=> socket.on(CONNECTED_EVT, callback);
    const onDisconnected = callback=>socket.on(DISCONNECTED_EVT, callback);
    const onUserConnect = callback=>socket.on(USER_CONNECTED, callback);
    const onUserDisconnect = callback=>socket.on(USER_DISCONNECTED, callback);
    const onListUsers = callback=>socket.on(LIST_USERS, callback);
    const onPublicMessage = callback=>socket.on(PUBLIC_CHANNEL, callback);
    const sendPrivate = (to, content)=>socket.emit(PRIVATE_CHANNEL, {content, to});
    const sendPublic = content=>socket.emit(PUBLIC_CHANNEL, {content});
    const listUsers = ()=>socket.emit(LIST_USERS);
    return {
        subscribeError, 
        unsubscribeError, 
        setAuth, 
        connect, 
        disconnect, 
        onConnected, 
        onDisconnected, 
        onUserConnect,
        onUserDisconnect,
        sendPrivate,
        sendPublic,
        listUsers,
        onListUsers,
        onPublicMessage
    };
}
