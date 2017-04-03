import React, {Component, PropTypes} from 'react';
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

        return (
            <li className={classes} onClick={this.onClick}>{item.username} {chatRoom && chatRoom.get("messages").size > 0 &&
            <span>⏱</span>}</li>
        );
    }
}