package com.businesspanda.verynote;

import android.os.Handler;
import android.os.Message;

import java.util.Stack;

/**
 * Created by Helene on 10.03.2015.
 */
public class PauseHandler extends Handler{

        Stack<Message> s = new Stack<Message>();
        public boolean is_paused = false;

        public synchronized void pause() {
            is_paused = true;
            System.out.println(is_paused + " is_paused is true <--");
        }

        public synchronized void resume() {
            is_paused = false;
            System.out.println(is_paused + " is_paused is s'posed to be false now");
            while (!s.empty()) {
                sendMessageAtFrontOfQueue(s.pop());
            }
        }

        @Override
        public void handleMessage(Message msg) {
            if (is_paused) {
                s.push(Message.obtain(msg));
                System.out.println(is_paused + " is_paused is true + msg " + msg);
                return;
            }
            System.out.println(is_paused + " normal handling");
            // otherwise handle message as normal
            // ...
        }
    }

