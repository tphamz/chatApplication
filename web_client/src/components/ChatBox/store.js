import { configureStore } from '@reduxjs/toolkit'
import reducer, {messageActions} from "./messageController";

const store = configureStore({
  reducer: {
    messages: reducer
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

export default store;
