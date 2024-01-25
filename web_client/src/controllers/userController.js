import {createSlice, createSelector, nanoid} from "@reduxjs/toolkit";

const UserController = createSlice({
	name: 'users',
	initialState: {value: []},
	reducers: {
		addUser(state, action){
            const {username} = action.payload;
            const find = state.value.find(item=>item.username==username);
            if(!find) state.value = [...state.value, action.payload];
		},
		removeUser(state, action){
            const {username} = action.payload;
            const value = [];
            for(let user of state.value){
                if(user.username!=username) value.push(user); 
            }
            if(value.length !=state.value.length)
            state.value = [...value];
		},
        
        setUsers(state, action){
            state.value = action.payload;
        }

	}
});

export const userActions = UserController.actions;
export const userReducer = UserController.reducer;