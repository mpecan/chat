import React, {Component, PropTypes} from 'react';
import {ChatRoom} from '../../api/chat_room';
import Message from './message';
import {sendMessage, subscribeToRoom} from '../../actions/app'
import {connect} from 'react-redux';
import _ from 'lodash';

@connect(null)
export default class Chat extends Component {
    static propTypes = {
        item: PropTypes.objectOf(ChatRoom),
        user: PropTypes.string,
        dispatch: PropTypes.func,

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
        } = this.props;

        const other = [item.get('initiator'), item.get('target')].map((user) => user.username).find((username) => user.username !== username);

        const sorted = _.chain(item.get('messages').toJS()).map((value, key) => value).sort((one, other) => one.created.diff(other.created, "seconds")).map((message) =>
            <Message key={message.id} item={message} currentUser={user !== message.poster}/>).value();

        return (
            <div className="ChatContainer">
                <div className="Title">{other}</div>
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