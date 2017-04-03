import React, {Component, PropTypes} from 'react';
import _ from 'lodash';
import {User} from '../../api/user'
export default class UserItem extends Component {
    static propTypes = {
        item: PropTypes.object,
        chatRoom: PropTypes.object,
        _onClick: PropTypes.func,
        isCurrent: PropTypes.bool,
    };

    constructor(props) {
        super(props);

        this.onClick = this.onClick.bind(this);
    }

    onClick() {
        const {item, _onClick} = this.props;
        _onClick(item.username);
    }

    render() {
        const {
            item,
            chatRoom,
            isCurrent
        } = this.props;

        let classes = [];
        if(isCurrent){
            classes.push("active");
        }
        if(chatRoom) {
            classes.push("hasRoom");
        }

        const sortedMessages = chatRoom && chatRoom.get("messages") &&
            _.chain(chatRoom.get('messages').toJS())
                .map((value, key) => value)
                .sort((one, other) => one.created.diff(other.created, "seconds")).value();

        const lastMessage = sortedMessages && sortedMessages.length > 0
            && _.last(sortedMessages);

        return (
            <li className={classes} onClick={this.onClick}>
                <div>
                    <div className="Username">{item.username}</div>
                    {lastMessage && <div className="Time">{lastMessage.created.fromNow()}</div>}
                </div>
                <div>
                {lastMessage &&<div className="Message">{lastMessage.content}</div> }
                </div>
            </li>
        );
    }
}