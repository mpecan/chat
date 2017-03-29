import React, {Component, PropTypes} from 'react';
import {ChatRoom} from '../../api/chat_room';
import Message from './message';
import {sendMessage,subscribeToRoom} from '../../actions/app'
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
    }

    componentWillMount(){
        this.state = {message: ""};
        const{item, dispatch} = this.props;
        dispatch(subscribeToRoom(item));
    }

    messageChange(event){
        this.setState({message: event.target.value});
    }

    onClick() {
        const {dispatch, item, user} = this.props;
        const chatId = item.get('id');
        dispatch(sendMessage(chatId,JSON.stringify({username: user, content: this.state.message, chat_id: chatId})));
    }

    render() {
        const {
            item,
            user,
        } = this.props;

        const sorted = _.chain(item.get('messages').toJS()).map((value, key) => value).sort((one, other) => one.created.diff(other.created, "seconds")).map((message) => <Message key={message.id} item={message} currentUser={user === message.posted_by}/>).value();

        return (
            <div className="Chat">
                {sorted}
                <label>Write message:
                <input value={this.state.message} onChange={this.messageChange} type="textarea" />
                    <button onClick={this.onClick}>Send</button>
                </label>
            </div>
        );
    }
}