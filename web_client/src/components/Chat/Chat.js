import React from "react";
import Stack from '@mui/material/Stack';
import Box from '@mui/material/Box';
import Divider from '@mui/material/Divider';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';

import {ChatBox} from "@/components/ChatBox/ChatBox";
import { AccountContext } from "@/services/userService";
import ChatService from "@/services/chatService";
import { useSelector } from "react-redux"
import  {setUsers, addUser, removeUser, addMessage, setMessages}  from '@/controllers/store';

const {
    setAuth, 
    disconnect, 
    onConnected, 
    onDisconnected, 
    onUserConnect,
    onUserDisconnect,
    sendPrivate,
    sendPublic,
    listUsers,
    onListUsers,
    connect,
    onPublicMessage 
} = ChatService();

const Users = ({onPrivateRoom})=>{
    const users = useSelector(state=>state.users.value);
    console.log(users);
    return(
        <div style={{minWidth: '200px'}}>
            <Toolbar />
            <Divider />
            <List>
            {users.map((user, index) => (
                <ListItemButton key={index} sx={{ py: 0, minHeight: 32}} onDoubleClick={evt=>onPrivateRoom(user)}>
                    <ListItemText primary={user.username} primaryTypographyProps={{ fontSize: 14, fontWeight: 'medium' }}/>
                </ListItemButton>
            ))}
            </List>
        </div>
    );
}

export default function Chat(){
    const {isLoggedIn, user, accessToken} = React.useContext(AccountContext);
    const loggedIn = isLoggedIn();
    const onPrivateRoom = user=>{
        console.log("onPrivateRoom::", user);
    }

    const onSend = data=>{
        console.log("onSend::", data);
        addMessage(data);
        sendPublic(data.content);
    }   

    const init = ()=>{
        setAuth(accessToken);
        connect();
        onListUsers(users=>{
            console.log("onListUsers::", users);
            setUsers(users);
        });
        listUsers();
        onPublicMessage(val=>{
            console.log("onPublicMessage::val::", val);
            addMessage(val)
        });

    }

    
    React.useEffect(()=>{
        if(loggedIn) init();
    }, [loggedIn]);
    return(
        <Box sx={{ width: '100%', height: '80vh' }}>
            <Stack direction="row" sx={{height: '100%'}} >
                <div style={{flex:1}}/>
                <Users onPrivateRoom={onPrivateRoom} onSend></Users>
                <ChatBox username={user} onSend={onSend}/>
                <div style={{flex:1}}/>
            </Stack>
        </Box>
    );
}