package com.padel.draft.repositories;

import com.padel.draft.domain.bid.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findByDraftSessionIdOrderByCreatedAtDesc(Long draftSessionId);

    List<Bid> findByDraftSessionIdAndPlayerIdOrderByCreatedAtDesc(Long draftSessionId, Long playerId);
}
