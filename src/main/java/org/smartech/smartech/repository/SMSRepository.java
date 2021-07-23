package org.smartech.smartech.repository;

import org.smartech.smartech.model.redis.SMS;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SMSRepository extends CrudRepository<SMS, String> {
}
