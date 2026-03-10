# Requirements Document

## Introduction
This project is a seminar-ready local full-stack application and repository template for demonstrating how to improve team productivity with Cursor as the primary tool and Claude Code as a compatible companion workflow. The demo application targets a financial solutions company and provides portfolio management, AI-assisted discussion, and proposal draft generation for internal demonstrations.

## Requirements

### 1. Seminar Demonstration Readiness
**User Story:** As an internal seminar presenter, I want a repository that visibly includes specs, rules, commands, skills, hooks, and a working application so that participants can understand how Cursor-centric workflows improve delivery quality and speed.

#### Acceptance Criteria
1. WHEN a presenter opens the repository THEN the repository SHALL include requirements, design, and task documents for the demo application.
2. WHEN a presenter explains Cursor-centric development THEN the repository SHALL include sample rules, commands, skills, and hooks that match the implementation workflow.
3. WHEN a presenter runs the application locally THEN the application SHALL start without requiring cloud deployment.

### 2. Portfolio Management
**User Story:** As a seminar participant, I want to view portfolio summaries and their underlying holdings so that I can understand the business context before using AI-assisted functions.

#### Acceptance Criteria
1. WHEN the application is opened THEN the user SHALL see a list of sample portfolios relevant to wealth management or advisory scenarios.
2. WHEN the user selects a portfolio THEN the application SHALL display objective, risk profile, advisor notes, and holding breakdown.
3. WHEN the sample data is initialized THEN the application SHALL persist portfolio and holding data in SQLite.

### 3. AI Discussion Support
**User Story:** As an advisor or analyst, I want to ask questions about a portfolio and receive AI-generated guidance so that I can discuss client options more productively.

#### Acceptance Criteria
1. WHEN the user submits a question for a portfolio THEN the application SHALL send portfolio context and discussion history to an external AI API.
2. WHEN the AI API responds successfully THEN the application SHALL store both user and assistant messages in SQLite and display them in the portfolio detail view.
3. WHEN AI configuration is missing or invalid THEN the application SHALL return an explicit error and log the failure without silently degrading behavior.

### 4. Proposal Draft Generation
**User Story:** As an advisor, I want to generate a proposal draft from a portfolio and its AI discussion so that I can accelerate client-facing document preparation.

#### Acceptance Criteria
1. WHEN the user requests proposal generation THEN the application SHALL send portfolio context and relevant discussion content to the AI API.
2. WHEN proposal generation succeeds THEN the application SHALL store the generated draft with creation time and display it in the UI.
3. WHEN multiple proposals exist for the same portfolio THEN the application SHALL show the latest generated proposal in the main detail view.

### 5. Operational Consistency
**User Story:** As a team member maintaining the repository, I want implementation and documentation changes to stay aligned so that future feature updates remain understandable and reproducible.

#### Acceptance Criteria
1. WHEN implementation rules are described in the repository THEN there SHALL be guidance for keeping specs and operational documentation in sync.
2. WHEN contributors use the repository as a template THEN there SHALL be examples of how to use multi-agent exploration, implementation, and review workflows.
3. WHEN business logic is added or changed THEN there SHALL be automated tests covering core portfolio and proposal-related behavior.
