'use client';

import Image from 'next/image';
import styles from './page.module.css';
import { Provider } from "react-redux";
import  store from '@/controllers/store';
import Register from "@/components/Register/Register";
import Chat from "@/components/Chat/Chat";
import {AccountProvider} from "@/services/userService";
export default function Home() {
  return (
    <AccountProvider>
      <Provider store={store}>
        <main className={styles.main}>
          <Register></Register>
          <Chat></Chat>
        </main> 
      </Provider>
    </AccountProvider>
  )
}
