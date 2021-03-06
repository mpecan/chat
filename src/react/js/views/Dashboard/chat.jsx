import React, {Component, PropTypes} from 'react';
import {ChatRoom} from '../../api/chat_room';
import Message from './message';
import {sendMessage, subscribeToRoom} from '../../actions/app'
import {connect} from 'react-redux';
import _ from 'lodash';
import moment from 'moment';

@connect(state => ({
    users: state.app.get('users'),
}))
export default class Chat extends Component {
    static propTypes = {
        item: PropTypes.objectOf(ChatRoom),
        user: PropTypes.string,
        dispatch: PropTypes.func,
        users: PropTypes.object,
    };

    constructor(props) {
        super(props);

        this.onClick = this.onClick.bind(this);
        this.messageChange = this.messageChange.bind(this);
        this.handleKeyPress = this.handleKeyPress.bind(this);
    }

    componentWillMount() {
        this.state = {message: ""};
        const {item, dispatch} = this.props;
        dispatch(subscribeToRoom(item));
    }
    componentDidMount() {
        const component = this;
        if(this.timer){
            clearInterval(this.timer);
        }
        this.timer = setInterval(function() {
            component.forceUpdate();
        }, 1000);
    }

    componentWillUnmount(){
        if(this.timer){
            clearInterval(this.timer);
            this.timer = null;
        }
    }

    messageChange(event) {
        this.setState({message: event.target.value});
    }

    onClick() {
        const {dispatch, item, user} = this.props;
        const chatId = item.get('id');
        dispatch(sendMessage(chatId, JSON.stringify({username: user, content: this.state.message, chat_id: chatId})));
        this.setState({message: ""});
    }

    handleKeyPress(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            this.onClick();
        }
    };


    scrollToBottom() {
        const scrollHeight = this.messageList.scrollHeight;
        const height = this.messageList.clientHeight;
        const maxScrollTop = scrollHeight - height;
        this.messageList.scrollTop = maxScrollTop > 0 ? maxScrollTop : 0;
    }

    componentDidUpdate() {
        this.scrollToBottom();
        this.textBox.focus();
    }

    render() {
        const {
            item,
            user,
            users,
        } = this.props;

        const other = users.get([item.get('initiator'), item.get('target')].find((other) => user !== other.username).id);

        const sorted = _.chain(item.get('messages').toJS()).map((value, key) => value).sort((one, other) => one.created.diff(other.created, "seconds")).map((message) =>
            <Message key={message.id} item={message} currentUser={user !== message.poster}/>).value();

        return (
            <div className="ChatContainer">
                {other && <div className="Title">{other.username} ({Math.abs(other.lastActive.diff(moment(), "seconds")) < 180 ? "Active" : "Inactive"})</div> }
                <div className="Chat">
                    <div className="Messages" ref={(ref) => this.messageList = ref}>
                        {sorted}
                    </div>
                    <label>Message:
                        <textarea value={this.state.message} onChange={this.messageChange}
                                  onKeyPress={this.handleKeyPress} ref={(ref) => this.textBox = ref}/>
                        <button onClick={this.onClick}>Send</button>
                    </label>
                </div>
            </div>

        );
    }
}