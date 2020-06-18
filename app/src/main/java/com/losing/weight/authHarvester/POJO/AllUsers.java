
package com.losing.weight.authHarvester.POJO;

import java.util.List;
import com.squareup.moshi.Json;

public class AllUsers {

    @Json(name = "users")
    private List<User> users = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public AllUsers() {
    }

    /**
     * 
     * @param users
     */
    public AllUsers(List<User> users) {
        super();
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
