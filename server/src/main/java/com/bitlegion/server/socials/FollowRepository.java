package com.bitlegion.server.socials;

import java.util.Optional;

import com.bitlegion.server.accounts.Account;

import org.springframework.data.repository.CrudRepository;

public interface FollowRepository extends CrudRepository<Follow, Integer> {

    public Optional<Follow> findByFollowerAndFollowing(Account follower, Account following);

    public Iterable<Follow> findAllByFollowing(Account following);

    public Integer countByFollowing(Account following);

    public Iterable<Follow> findAllByFollower(Account follower);

    public Integer countByFollower(Account follower);

}
