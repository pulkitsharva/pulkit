package com.revolut.assignement.pulkit.repository;

import com.revolut.assignement.pulkit.dao.User;

public interface UserRepository {

  void create(final User user);

  User getUserByUserId(final Long userId);
}
