import React, {Component, PropTypes} from 'react';
import {User} from '../../api/user'
export default class UserItem extends Component {
    static propTypes = {
        item: PropTypes.objectOf(User),
        chatroom: PropTypes.object,
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
            chatroom,
            isCurrent
        } = this.props;

        return (
            <li className={isCurrent ? "active" : null} onClick={this.onClick}>{item.username} {chatroom &&
            <span>✔</span>}</li>
        );
    }
}