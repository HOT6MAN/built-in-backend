package com.example.hotsix.repository.apply;

import com.example.hotsix.model.id.ApplyId;

public interface ApplyRepositoryCustom {
    void approve(ApplyId id);
    void reject(ApplyId id);
    void delete(ApplyId id);
}
