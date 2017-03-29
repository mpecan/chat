import 'es6-promise';
import webstomp from 'webstomp-client';
const SockJS = require('sockjs-client');
import 'whatwg-fetch';
import {User} from './user';

function testAsync() {
    return new Promise(resolve => {
        setTimeout(() => {
            const date = new Date();
            let seconds = date.getSeconds();
            let minutes = date.getMinutes();

            seconds = seconds < 10 ? `0${ seconds }` : seconds;
            minutes = minutes < 10 ? `0${ minutes }` : minutes;

            resolve(`Current time: ${ date.getHours() }:${ minutes }:${ seconds }`);
        }, (Math.random() * 1000) + 1000); // 1-2 seconds delay
    });
}

function getUser(username) {
    return fetch("/api/user", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username,
        }),
    });
}

function getUsers() {
    return fetch("/api/user");
}

function getChatRoom(username, target) {
    return fetch("/api/chat?username=" + username + "&partner=" + target, {
        method: 'GET',
    })
}


const client = webstomp.over(new SockJS("http://localhost:8080/messages"), {heartbeat: false});


export default {
    testAsync,
    getUser,
    getChatRoom,
    getUsers,
    client: client,
    subscribe: client.subscribe,
    send: client.send,
};
