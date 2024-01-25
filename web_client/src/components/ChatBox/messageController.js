import {createSlice} from "@reduxjs/toolkit";

const MessageController = createSlice({
	name: 'messages',
	initialState: {value: []},
	reducers: {
		addMessage(state, action){
            const {username, content} = action.payload;
            const value = [...state.value];
            const last = value.pop();
            if(last && last.username==username){
                last.contents.push(content);
                value.push(last);
            }
            else{
                if(last) value.push(last);
                value.push({username, contents:[content]});
            }
            
            state.value = value;
		},
		loadOldMessages(state, action){
            const value = [];
            let username = null;
            for(let message of action.payload){
                if(message.username==username) value[value.length-1].contents.push(message.content);
                else{
                    username = message.username;
                    value.push({username, contents: [message.content]});
                }
            }
            for(let message of state)
                value.push(message);
            state.value = value;
		},
        
        setMessages(state, action){
            const value = [];
            let username = null;
            for(let message of action.payload){
                if(message.username==username) value[value.length-1].contents.push(message.content);
                else{
                    username = message.username;
                    value.push({username, contents: [message.content]});
                }
            }
            state.value = value;
        }

	}
});

export const messageActions = MessageController.actions;
export const messageReducer = MessageController.reducer;