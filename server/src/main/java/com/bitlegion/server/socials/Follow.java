package com.bitlegion.server.socials;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.bitlegion.server.accounts.Account;

@Entity
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "follower_id", referencedColumnName = "id")
    private Account follower;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "following_id", referencedColumnName = "id")
    private Account following;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Account getFollower() {
        return this.follower;
    }

    public void setFollower(Account follower) {
        this.follower = follower;
    }

    public Account getFollowing() {
        return this.following;
    }

    public void setFollowing(Account following) {
        this.following = following;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", follower='" + getFollower() + "'" +
            ", following='" + getFollowing() + "'" +
            "}";
    }
    
}
