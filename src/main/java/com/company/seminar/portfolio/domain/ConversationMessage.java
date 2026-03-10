package com.company.seminar.portfolio.domain;

import java.time.Instant;

public record ConversationMessage(
    Long id, Long portfolioId, MessageRole role, String content, Instant createdAt) {}
