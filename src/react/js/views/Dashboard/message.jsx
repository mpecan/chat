import React, {Component, PropTypes} from 'react';

export default class Message extends Component {
    static propTypes = {
        item: PropTypes.object,
        currentUser: PropTypes.bool,
    };

    constructor(props) {
        super(props);
    }


    render() {
        const {
            item,
            currentUser,
        } = this.props;

        return (
            <div className={currentUser ? "Message Current" : "Message"}>
                <div className="Bubble">
                    <div className="Date">{item.created.format('MMMM Do YYYY, h:mm:ss a')}</div>
                    <div className="Text">{item.content}</div>
                </div>
                <div className="User">{item.poster}</div>
            </div>
        );
    }
}