create table if not exists portfolios (
    id integer primary key,
    name text not null,
    client_segment text not null,
    objective text not null,
    risk_profile text not null,
    invested_amount numeric not null,
    updated_at text not null
);

create table if not exists holdings (
    id integer primary key,
    portfolio_id integer not null,
    asset_class text not null,
    ticker text not null,
    instrument_name text not null,
    allocation_percent numeric not null,
    market_value numeric not null,
    foreign key (portfolio_id) references portfolios(id)
);

create table if not exists conversation_messages (
    id integer primary key autoincrement,
    portfolio_id integer not null,
    role text not null,
    content text not null,
    created_at text not null,
    foreign key (portfolio_id) references portfolios(id)
);

create table if not exists proposal_drafts (
    id integer primary key autoincrement,
    portfolio_id integer not null,
    title text not null,
    content text not null,
    created_at text not null,
    foreign key (portfolio_id) references portfolios(id)
);
