"use client";
import React from "react";
import { useSelector } from "react-redux";
import {messageReducer, messageActions} from "./messageController";
import  {addMessage, loadOldMessages, setMessages}  from "./store";
import styles from './chatbox.module.css';
import { Provider } from "react-redux";

const ChatContent = React.forwardRef(function({username, hasNewMessage=true}, ref){
	const messages = useSelector(state=>state.messages.value);
    console.log(messages);
    const eonRef = React.useRef();
    const sentMessage = message=>(
        <div className={styles.rowflex}>
            <div className={styles.message_padding} />
            <div className={styles.message_container}>
                {message.contents.map((item, index)=><div key={index} className={styles.message_content + " " + styles.message_sent}>{item}</div>)}
            </div>    
        </div>
    );


    const receivedMessage = message=>(
        <div className={styles.columnflex}>
            <div className={styles.rowflex}>
                <div>
                    <div className={styles.message_avatar}>{message.username[0].toUpperCase()}</div>
                </div>
                <div className={styles.message_container}>
                    <div className={styles.message_username}>{message.username}</div>
                    <div></div>{message.contents.map((item, index)=><div key={index} className={styles.message_content + " " + styles.message_received}>{item}</div>)}
                </div>  
                <div className={styles.message_padding} />
            </div>
        </div>
    );

    const renderMessage =message=>{
        console.log("renderMessage", message);
        if(message.username==username) return sentMessage(message);
        return receivedMessage(message);
    }


    React.useImperativeHandle(ref, () => ({
        scrollToView(){eonRef.current.scrollIntoView({ behavior: "smooth" })},
    }));

    React.useEffect(()=>{
        eonRef.current.scrollIntoView({ behavior: "smooth" });
    }, []);
      
    return <div className={styles.chat_content}>
        <>{  
            (messages||[]).map((item,index)=><div key={index}>{renderMessage(item)}</div>)
         }</>
        <div ref={eonRef} style={{height:20}}></div>
    </div>

});

function ChatInput({username, onSubmit}){
    const [message, setMessage] = React.useState("");
    const [shiftHeld, setShiftHeld] = React.useState(false);
    const textRef = React.useRef();

    function downHandler(evt) {
        if (evt.key === 'Shift') 
           return setShiftHeld(true);
        if(evt.key=='Enter' && !shiftHeld) evt.target.form.requestSubmit();
    }

    function upHandler(evt) {
        if (evt.key === 'Shift') 
            setShiftHeld(false);
    }
    
    const onChange = evt=>{
        setMessage(evt.target.value);
    }
    
    const resetHeight = ()=>{
        textRef.current.style.height = "41px";

    }
    const autogrow = evt=>{
        resetHeight();
        textRef.current.style.height = textRef.current.scrollHeight + "px";
    }

    const submit = evt=>{
        evt.preventDefault();
        textRef.current.blur();
        if(message) onSubmit(message);
        setMessage("");
        setTimeout(()=>{
            textRef.current.focus();
            resetHeight();
        }, 100);
    }

    return <form className={styles.chat_form} onSubmit={submit} style={{width:"100%"}}><textarea ref={textRef} value={message} onInput={autogrow} onChange={onChange} onKeyDown={downHandler} onKeyUp={upHandler} placeholder="Type a message..." className={styles.chat_input}></textarea></form>
}

function ChatBox({username, onSend}){
    const contentRef = React.useRef();
    const onSubmit = val=>{
        const data= {username, content:val};
        onSend(data);
        contentRef.current.scrollToView();
    }

    return(
        <div className={styles.chatbox}>
            <ChatContent ref={contentRef} username={username}/>
            <ChatInput onSubmit={onSubmit} />
        </div>
    )
}

function ChatBoxWidget({username, onSend}){
    const contentRef = React.useRef();
    const onSubmit = val=>{
        const data= {username, content:val};
        onSend(data);
        addMessage(data);
        setTimeout(() => {
            contentRef.current.scrollToView();            
        }, 0);
    }
    return <Provider store={store}>
       <div className={styles.chatbox}>
            <ChatContent ref={contentRef} username={username}/>
            <ChatInput onSubmit={onSubmit} />
        </div>
    </Provider>
}

export {ChatBoxWidget, ChatBox, addMessage, loadOldMessages, setMessages, messageReducer, messageActions};

