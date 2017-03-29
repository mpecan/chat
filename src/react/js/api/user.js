export class User{
    constructor(response){
        this.username = response.username;
        this.id = response.id;
        this.lastActive = response.last_active;
    }
}