import { configureStore } from '@reduxjs/toolkit'
import { useSelector } from "react-redux";

import {messageReducer, messageActions} from '@/components/ChatBox/ChatBox';
import {userReducer, userActions} from './userController';

const store = configureStore({
  reducer: {
    messages: messageReducer,
    users: userReducer
  },
});


export function loadOldMessages(val){
    store.dispatch(messageActions.loadOldMessages(val))
}

export function addMessage(val){
    store.dispatch(messageActions.addMessage(val))
}

export function setMessages(val){
    store.dispatch(messageActions.setMessages(val))
}

export function addUser(val){
    store.dispatch(userActions.addUser(val))
}

export function removeUser(val){
    store.dispatch(userActions.removeUser(val))
}

export function setUsers(val){
    store.dispatch(userActions.setUsers(val))
}
 
export default store;
