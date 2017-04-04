
import moment from 'moment';
export class User{
    constructor(response){
        this.username = response.username;
        this.id = response.id;
        this.lastActive = moment(response.last_active);
    }
}