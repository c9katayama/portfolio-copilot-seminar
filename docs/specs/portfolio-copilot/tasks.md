# Implementation Plan

- [x] 1. Repository Foundation: Create seminar-facing repository structure and base documentation
  - Add README, seminar outline, demo script, and operation guide.
  - Add sample Cursor rules, commands, Claude skills, and local hooks for spec-driven development.
  - _Requirements: 1, 5_

- [x] 2. Backend Foundation: Configure application, persistence, and seed data
  - Add SQLite configuration, schema initialization, seed portfolios, and domain/repository models.
  - Ensure the application can boot locally and render sample data.
  - _Requirements: 2_

- [x] 3. Portfolio UI: Implement portfolio list and detail screens
  - Build Thymeleaf pages and controller flows for portfolio summary, holdings, discussion thread, and latest proposal.
  - Add styling and page structure suitable for a live seminar demo.
  - _Requirements: 1, 2, 4_

- [x] 4. AI Discussion: Implement Anthropic-backed portfolio discussion flow
  - Add AI configuration properties, API client, prompt building, service logic, and discussion endpoint.
  - Persist user and assistant messages with timestamps and explicit error handling.
  - _Requirements: 3_

- [x] 5. Proposal Generation: Implement proposal draft generation from portfolio context
  - Add proposal prompt generation, persistence, and UI/API integration for the latest draft.
  - Ensure proposal content is clearly marked as draft support material.
  - _Requirements: 4_

- [x] 6. Quality and Consistency: Add automated tests and repository maintenance guidance
  - Add unit and integration tests covering prompt construction, repository-backed portfolio rendering, and proposal flow behavior.
  - Add documentation for future feature updates and spec alignment.
  - _Requirements: 5_

- [x] 7. Presentation Assets: Build the seminar PowerPoint from the provided template
  - Create a Japanese internal seminar deck focused on Cursor-first productivity patterns and the sample application workflow.
  - Include sections on rules, skills, MCP, commands/hooks, multi-agent usage, and operational consistency.
  - _Requirements: 1_
