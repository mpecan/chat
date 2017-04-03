import api from 'api';

export const TEST_ACTION = 'TEST_ACTION';

export const TEST_ASYNC_ACTION_START = 'TEST_ASYNC_ACTION_START';
export const TEST_ASYNC_ACTION_ERROR = 'TEST_ASYNC_ACTION_ERROR';
export const TEST_ASYNC_ACTION_SUCCESS = 'TEST_ASYNC_ACTION_SUCCESS';
export const SET_USERNAME = "SET_USERNAME";
export const USER_SET = "USER_SET";
export const FAILED_TO_SET_USER = "FAILED_TO_SET_USER";
export const UNSET_USERNAME = "UNSET_USERNAME";
export const SEND_MESSAGE = "SEND_MESSAGE";
export const MESSAGE_SENT = "MESSAGE_SENT";
export const MESSAGE_RECEIVED = "MESSAGE_RECEIVED";
export const JOIN_ROOM = "JOIN_ROOM";
export const ROOM_JOINED = "ROOM_JOINED";
export const FAILED_TO_JOIN_ROOM = "FAILED_TO_JOIN_ROOM";
export const GETTING_USERS = "GETTING_USERS";
export const GOT_USERS = "GOT_USERS";
export const GOT_USER = "GOT_USER";
export const FAILED_TO_GET_USERS = "FAILED_TO_GET_USERS";
export const GOT_CHAT_ROOM = "GOT_CHAT_ROOM";
export const GETTING_CHAT_ROOMS = "GETTING_CHAT_ROOMS";
export const FAILED_TO_GET_CHAT_ROOMS = "FAILED_TO_GET_CHAT_ROOMS";


function setUsernameStart(username) {
    return {
        type: SET_USERNAME,
        username: username,
    };
}


function userSet(username, user) {
    return function (dispatch) {
        dispatch(userSetEnd(username, user));
        dispatch(connectToWs(username));
    }
}


function userSetEnd(username, user) {
    if (window.sessionStorage) {
        window.sessionStorage.setItem("username", username);
    }
    return {
        type: USER_SET,
        username,
        user,
    };
}

function connectToWs(username) {
    return function (dispatch) {
        api.client.connect({}, () => {
            dispatch(getUsers());
            dispatch(subscribeToUsers());
            dispatch(getChatRooms(username));
            dispatch(subscribeToChatRooms(username));
        });

    }
}

function gotChatRoom(chatRoom) {
    return function (dispatch) {
        dispatch(gotChatRoomEnd(chatRoom));
        dispatch(subscribeToRoom(chatRoom.id));
    };
}

function gotChatRoomEnd(chatRoom) {
    return {
        type: GOT_CHAT_ROOM,
        chatRoom
    };
}

function subscribeToChatRooms(username) {
    return function (dispatch) {
        api.client.subscribe("/topic/rooms/" + username, (message) => {
            dispatch(gotChatRoom(JSON.parse(message.body)));
        });
    }
}


function subscribeToUsers() {
    return function (dispatch) {
        api.client.subscribe("/topic/users", (message) => {
            dispatch(gotUser(message));
        })
    }
}

function startChatRoomGet(target) {
    return {
        type: JOIN_ROOM,
        target,
    };
}
function roomJoined(target, data) {
    return function (dispatch) {
        dispatch(subscribeToRoom(data.id));
        dispatch(notifyRoomJoined(target, data));
    };
}
function notifyRoomJoined(target, data) {
    return {
        type: ROOM_JOINED,
        target,
        data,
    };
}

function failedToJoinRoom(target, data) {
    return {
        type: FAILED_TO_JOIN_ROOM,
        target,
        data,
    };
}
function messageReceived(message) {
    return {
        type: MESSAGE_RECEIVED,
        message,
    }
}
export function subscribeToRoom(roomId) {
    return function (dispatch) {
        api.client.subscribe("/topic/post/" + roomId, (message) => {
            dispatch(messageReceived(message));
        });
    };
}


export function sendMessage(chatRoom, message) {
    api.client.send("/app/post/" + chatRoom, message);
    return {
        type: MESSAGE_SENT,
        message
    }
}

function gettingUsers() {
    return {
        type: GETTING_USERS,
    }
}

function gotUsers(users) {
    return {
        type: GOT_USERS,
        users,
    }
}

function failedToGetUsers() {
    return {
        type: FAILED_TO_GET_USERS,
    }
}

function gotUser(user) {
    return {
        type: GOT_USER,
        user: JSON.parse(user.body),
    }
}

export function getUsers() {
    return function (dispatch) {
        dispatch(gettingUsers());
        api.getUsers()
            .then((response) => response.json()
                .then((data) => {
                    return dispatch(gotUsers(data));
                }))
            .catch((data) => failedToGetUsers())
    }
}

function failedToGetChatRooms() {
    return {
        type: FAILED_TO_GET_CHAT_ROOMS
    }
}
function gettingChatRooms() {
    return {
        type: GETTING_CHAT_ROOMS
    }
}
export function getChatRooms(username) {
    return function (dispatch) {
        dispatch(gettingChatRooms());
        api.getChatRooms(username).then((response) => response.json()
            .then((data) => {
                data.map((room) => {
                    dispatch(gotChatRoom(room));
                });
            })
        ).catch((data) => {
            dispatch(failedToGetChatRooms(data))
        })
    };
}

export function getChatRoom(username, target) {
    return function (dispatch) {
        dispatch(startChatRoomGet(target));
        api.getChatRoom(username, target).then((response) => response.json().then((data) => {
                api.client.subscribe("/topic/post/" + data.id, function (message) {
                    dispatch(messageReceived(message));
                });
                dispatch(roomJoined(target, data));
            })
        ).catch((response) => response.json()
            .then((data) => {
                    dispatch(failedToJoinRoom(target, data))
                }
            )
        )
    };
}

function failedToSetUser(username, response) {
    return {
        type: FAILED_TO_SET_USER,
        username,
        response
    };
}

export function setUsername(username) {
    return function (dispatch) {
        dispatch(setUsernameStart(username));
        api.getUser(username)
            .then((response) => response.json()
                .then((data) => {
                        dispatch(userSet(username, data));
                    }
                ))
            .catch((response) => {
                    dispatch(failedToSetUser(username, response));
                }
            );
    };

}

// Update
