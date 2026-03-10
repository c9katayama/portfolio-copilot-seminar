# Portfolio Copilot Seminar

生成AI開発デモ用リポジトリです。システム開発の文脈を前提に、仕様駆動開発、Rules、Commands、Skills、MCP、Hooks、マルチエージェント活用をまとめて説明できる構成にしています。

## 含まれるもの
- Spring Boot + SQLite のローカル実行アプリ
- コードフォーマット（Spotless / Google Java Format）。`./gradlew spotlessApply` で適用、`./gradlew spotlessCheck` で検証
- ポートフォリオ管理、保有比率の比較表示、AI対話、提案書ドラフト生成
- セミナー用仕様書、運用ガイド、デモスクリプト
- Cursor / Claude Code の活用例
- financial-crawler サブエージェント（複数ソースから金融ニュースを収集し、市場影響度トップ10を `financial-report/` に出力）

## ドキュメント
- `docs/seminar-outline.md`
- `docs/demo-script.md`
- `docs/operation-guide.md`
- `docs/slides/portfolio-copilot-briefing.md`
- `docs/slides/portfolio-copilot-briefing.pptx`
- `docs/specs/portfolio-copilot/requirements.md`
- `docs/specs/portfolio-copilot/design.md`
- `docs/specs/portfolio-copilot/tasks.md`

## ローカル実行
1. AI対話・提案書ドラフト用に `OPENAI_API_KEY` を環境変数に設定する（未設定時はAI機能利用時にエラーになります）
2. `./gradlew bootRun` を実行する
3. [http://localhost:8080/portfolios](http://localhost:8080/portfolios) を開く

Anthropic API を使う場合は `app.ai.provider=anthropic` と `ANTHROPIC_API_KEY` を設定する。

## スライド生成
1. ワークスペース直下の `pptx-template.pptx` を用意する
2. `python3 scripts/generate_seminar_deck.py` を実行する
3. 生成物は `slides/cursor-claude-code-seminar.pptx` に出力される

## アプリ説明資料の生成
1. `python3 scripts/generate_portfolio_copilot_briefing.py` を実行する
2. 生成物は `docs/slides/portfolio-copilot-briefing.pptx` に出力される
3. スライド構成と参照元は `docs/slides/portfolio-copilot-briefing.md` に整理している

