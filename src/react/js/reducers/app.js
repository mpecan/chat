import {Map} from 'immutable';
import 'react/lib/update';
import {User} from '../api/user';
import {ChatRoom} from '../api/chat_room';
import moment from 'moment';
import {
    SET_USERNAME,
    UNSET_USERNAME,
    USER_SET,
    FAILED_TO_SET_USER,
    JOIN_ROOM,
    ROOM_JOINED,
    FAILED_TO_JOIN_ROOM,
    GET_USERS,
    GOT_USERS,
    FAILED_TO_GET_USERS,
    GOT_USER,
    MESSAGE_RECEIVED,
    GOT_CHAT_ROOM
} from 'actions/app';

const initialState = Map({
    counter: 0,
    asyncLoading: false,
    asyncError: null,
    asyncData: null,
    user: null,
    chatRooms: Map({}),
    users: Map({}),
    currentChat: null
});

const actionsMap = {

    [SET_USERNAME]: (state) => {
        return state.merge({
            asyncLoading: true,
        })
    },

    [FAILED_TO_SET_USER]: (state) => {
        return state.merge({
            asyncLoading: false,
        });
    },

    [USER_SET]: (state, action) => {
        return state.merge({
            user: new User(action.user),
            asyncLoading: false,
        });
    },

    [UNSET_USERNAME]: (state) => {
        return state.merge({
            user: null
        });
    },

    [ROOM_JOINED]: (state, action) => {
        return state.withMutations((input) => {
            input.mergeIn(["chatRooms"],{[action.data.id]: Map(new ChatRoom(action.data))});
            input.set("currentChat", action.data.id);
        });
    },

    [GOT_USERS]: (state, action) => {
        return state.mergeIn(['users'], action.users.reduce((agg, item) => {
            agg[item.id] = new User(item);
            return agg;
        }, {}));
    },

    [GOT_USER]: (state, action) => {
        return state.mergeIn(['users'], {[action.user.id]: new User(action.user)});
    },

    [MESSAGE_RECEIVED]: (state, action) => {
        let data = JSON.parse(action.message.body);
        let currentChat = data.chat_id;
        data.created = moment(data.created);
        return state.mergeIn(['chatRooms', currentChat , 'messages'], {[data.id]: data});
    },

    [GOT_CHAT_ROOM]: (state, action) => {
        let room = action.chatRoom;
        return state.withMutations((input) => {
            input.mergeIn(["chatRooms"], {[room.id]: Map(new ChatRoom(room))});
        });
    },

};

export default function reducer(state = initialState, action = {}) {
    const fn = actionsMap[action.type];
    return fn ? fn(state, action) : state;
}
