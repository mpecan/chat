import React, {Component, PropTypes} from 'react';
import {connect} from 'react-redux';
import {
    testAction,
    testAsync,
    setUsername,
    client,
    getChatRoom,
    getUsers,
    connectToWs
} from 'actions/app';
import bookImg from '../../../assets/img/book2.jpg';
import {User} from '../../api/user'
import UserItem from "./user";
import Chat from './chat';

@connect(state => ({
    asyncData: state.app.get('asyncData'),
    asyncError: state.app.get('asyncError'),
    asyncLoading: state.app.get('asyncLoading'),
    counter: state.app.get('counter'),
    messages: state.app.get('messages'),
    user: state.app.get('user'),
    chatRooms: state.app.get('chatRooms'),
    users: state.app.get('users'),
    currentChat: state.app.get('currentChat'),
}))
export default class Dashboard extends Component {
    static propTypes = {
        asyncError: PropTypes.object,
        asyncLoading: PropTypes.bool,
        counter: PropTypes.number,
        messages: PropTypes.array,
        user: PropTypes.objectOf(User),
        // from react-redux connect
        dispatch: PropTypes.func,
        chatRooms: PropTypes.object,
        users: PropTypes.object,
        currentChat: PropTypes.string
    };

    constructor(props) {
        super(props);

        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handleUsernameSet = this.handleUsernameSet.bind(this);
        this.handleTargetChange = this.handleTargetChange.bind(this);
        this.handleJoinRoom = this.handleJoinRoom.bind(this);
        this.joinRoom = this.joinRoom.bind(this);
        this.handleKeyPress = this.handleKeyPress.bind(this);
    }

    componentWillMount() {
        this.state = {usernameField: "", targetUser: ""};
    }

    componentDidMount() {
        const {dispatch} = this.props;
        if (window.sessionStorage) {
            let username = window.sessionStorage.getItem("username");
            if (username) {
                dispatch(setUsername(username));
            }
        }
    }

    handleUsernameChange(event) {
        this.setState({usernameField: event.target.value});
    }

    handleUsernameSet() {
        const {dispatch} = this.props;
        dispatch(setUsername(this.state.usernameField));
    }

    handleKeyPress(event) {
        if (event.key === 'Enter') {
            this.handleUsernameSet();
        }
    };

    handleTargetChange(event) {
        this.setState({targetUser: event.target.value});
    }

    handleJoinRoom() {
        this.joinRoom(this.state.targetUser);
    }

    joinRoom(target) {
        const {dispatch, user, chatRooms} = this.props;
        if (!chatRooms.find((chat) => [chat.get('initiator'), chat.get('target')].some((user) => user.username === this.state.targetUser))) {
            dispatch(getChatRoom(user.username, target));
        }
    }


    render() {
        const {
            asyncLoading,
            user,
            users,
            chatRooms,
            currentChat
        } = this.props;
        const room = chatRooms.get(currentChat);
        return (
            <div>{ user ? <div className='Dashboard'>
                <div className="Users">
                    Hello { user && <span> {user.username }</span> }. Choose a user below:
                    <ul>
                        {users.filter((current) => current.username !== user.username).map((current) => {
                            const chatRoom = chatRooms.find(
                                (room) => {
                                    return [room.get('initiator'), room.get('target')].map((user) => user.username).includes(current.username);
                                });

                            return <UserItem isCurrent={chatRoom && chatRoom.get('id') === currentChat }
                                             _onClick={this.joinRoom} item={current} chatRoom={chatRoom}/>;
                        })}
                    </ul>
                </div>
                {room ? <Chat item={room} user={user.username}/> :
                    <div className="ChatContainer"><div>Select a user to chat with on the left</div></div> }

            </div> : <div>
                <label className="UserEnter">
                    Username:
                    <input type="text" onChange={this.handleUsernameChange} value={this.state.usernameField}
                           onKeyPress={this.handleKeyPress}/>
                    <button
                        disabled={asyncLoading}
                        onClick={this.handleUsernameSet}
                    >Set username
                    </button>
                </label>
            </div>
            }</div>
        );
    }
}
