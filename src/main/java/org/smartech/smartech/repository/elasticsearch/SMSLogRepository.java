package org.smartech.smartech.repository.elasticsearch;

import org.smartech.smartech.model.elasticsearch.SMSLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SMSLogRepository extends ElasticsearchRepository<SMSLog, String> {
}
