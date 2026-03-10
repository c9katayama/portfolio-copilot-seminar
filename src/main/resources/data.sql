delete from proposal_drafts;
delete from conversation_messages;
delete from holdings;
delete from portfolios;

insert into portfolios (id, name, client_segment, objective, risk_profile, invested_amount, updated_at) values
    (1, 'Core Growth 2030', 'Mass Affluent', '中長期の成長を狙いつつ、下落局面の耐性も確保する', 'Moderate', 18000000, '2026-03-01T10:00:00Z'),
    (2, 'Income Balance Plus', 'Retirement Advisory', '安定収益を重視しながらインフレ耐性を持たせる', 'Conservative', 26000000, '2026-03-02T09:30:00Z'),
    (3, 'Executive Reserve', 'Corporate Executive', '短中期の流動性を確保しつつ、余剰資金を効率運用する', 'Balanced', 32000000, '2026-03-04T08:45:00Z');

insert into holdings (id, portfolio_id, asset_class, ticker, instrument_name, allocation_percent, market_value) values
    (101, 1, 'Equity ETF', 'VT', 'Vanguard Total World Stock ETF', 42.0, 7560000),
    (102, 1, 'Bond ETF', 'BND', 'Vanguard Total Bond Market ETF', 24.0, 4320000),
    (103, 1, 'Japan Equity', '1306', 'TOPIX ETF', 14.0, 2520000),
    (104, 1, 'Cash Equivalent', 'JPY-CASH', 'MMF / Cash', 20.0, 3600000),
    (201, 2, 'Bond ETF', 'AGG', 'iShares Core U.S. Aggregate Bond ETF', 36.0, 9360000),
    (202, 2, 'Dividend Equity', 'VYM', 'Vanguard High Dividend Yield ETF', 24.0, 6240000),
    (203, 2, 'REIT', 'VNQ', 'Vanguard Real Estate ETF', 16.0, 4160000),
    (204, 2, 'Cash Equivalent', 'JPY-CASH', 'MMF / Cash', 24.0, 6240000),
    (301, 3, 'Short Duration Bond', 'SHY', 'iShares 1-3 Year Treasury Bond ETF', 30.0, 9600000),
    (302, 3, 'Global Equity', 'ACWI', 'iShares MSCI ACWI ETF', 30.0, 9600000),
    (303, 3, 'Gold', 'GLDM', 'SPDR Gold MiniShares Trust', 10.0, 3200000),
    (304, 3, 'Cash Equivalent', 'JPY-CASH', 'MMF / Cash', 30.0, 9600000);

insert into conversation_messages (portfolio_id, role, content, created_at) values
    (1, 'USER', '現状の配分でボラティリティを抑えながら成長余地を残す方向を議論したいです。', '2026-03-05T09:00:00Z'),
    (1, 'ASSISTANT', '内部検討のたたき台としては、キャッシュ比率と債券比率の役割を整理した上で、株式部分の地域分散を再確認するのが有効です。', '2026-03-05T09:00:30Z');

insert into proposal_drafts (portfolio_id, title, content, created_at) values
    (1, 'Core Growth 2030 提案ドラフト 2026-03-05', '提案概要\n- 成長性を維持しつつ下落耐性を補強するため、キャッシュと債券の役割を再整理する。\n\n現状認識\n- 世界株式比率は確保できている一方、守りの資産の目的がやや曖昧。\n\n見直しの方向性\n- ボラティリティ許容度に応じて、債券とキャッシュの配分方針を明文化する。', '2026-03-05T11:00:00Z');
