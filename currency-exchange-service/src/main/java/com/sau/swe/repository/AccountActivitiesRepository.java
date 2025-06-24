package com.sau.swe.repository;

import com.sau.swe.entity.AccountActivities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountActivitiesRepository extends JpaRepository<AccountActivities, Long> {
}
