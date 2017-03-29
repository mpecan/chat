import {User} from './user';
import {Map} from 'immutable'
import moment from 'moment';
export class ChatRoom {
    constructor(response) {
        this.id = response.id;
        this.initiator = new User(response.initiator);
        this.target = new User(response.target);
        this.messages = Map({}).merge(response.messages
            .map((message) => {
                message.created = moment(message.created);
                return message;
            })
            .reduce((agg, val) => {
                agg[val.id] = val;
                return agg
            }, {}));
        this.created = response.created;
    }
}