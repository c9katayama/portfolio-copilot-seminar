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

## Agents（サブエージェント）

`.cursor/agents/` に、専門タスク用のサブエージェントを配置している。システムプロンプトに従い、特定のワークフローを実行する。

| 名前 | 説明 |
|------|------|
| financial-crawler | 複数の金融情報ソースから Web でニュースを収集し、市場影響度でランク付けしてトップ10を選定。結果は `financial-report/top10-YYYY-MM-DD.md` に出力。複数体で担当を分けた場合は `financial-report/weekly-report-YYYY-MM-DD.md` に統合することもある。 |

利用例: 「financial-crawler サブエージェントで今日の金融レポートを作成して」「financial-crawler サブエージェント4体で今週の金融レポートを作成して」。

詳細は [.cursor/README.md](.cursor/README.md) を参照。

## Skills（スキル）

`.cursor/skills/<name>/SKILL.md` に、ワークフローやレビュー基準を定義している。AI がタスクに応じて該当スキルを読み、手順に従って実行する。主なスキル一覧は [.cursor/README.md](.cursor/README.md) を参照。

### portfolio-copilot-briefing-deck（説明資料作成）

Portfolio Copilot アプリの説明用スライドを `docs/` 配下に `.pptx` で作成するスキル。社外・金融機関向けに、機能・操作方法・アーキテクチャ・必要環境を整理した資料を作る用途で使う。

**いつ使うか**
- 説明資料やスライドの作成・更新を明示的に依頼したとき
- アプリの機能、操作フロー、アーキテクチャ、技術スタック、必要環境・スペックについて資料が欲しいとき

**使い方**
1. チャットで「Portfolio Copilot の説明資料（スライド）を作成して」「説明用の .pptx を生成して」などと依頼する。
2. エージェントが `portfolio-copilot-briefing-deck` スキルを読み、`pptx` スキルと仕様書（`docs/specs/portfolio-copilot/`）を参照して作業する。
3. 出力は既定で `docs/slides/portfolio-copilot-briefing.pptx`。補助テキストは `docs/slides/portfolio-copilot-briefing.md` に残す場合がある。

**スキル内で行うこと**
- 現在日時の確認（`date`）、既存 `pptx` スキルの参照、README と `requirements.md` / `design.md` / `tasks.md` の読解
- 6〜8枚程度のスライド構成の決定（`.cursor/skills/portfolio-copilot-briefing-deck/deck-guide.md` の章立てを基本に利用）
- 必須4テーマの反映: 機能・操作方法・アーキテクチャと技術スタック・必要環境と実行前提
- 表現ルール: 日本語、誇張なし、金融向けの落ち着いたトーン、図のノードには番号を振る
- デザイン: ダークネイビー・スレート・ホワイト基調、彩度を抑えたアクセント（deck-guide の Primary/Secondary/Accent を参照）

**並列化**
- 章ごとに分担してドラフトを並列生成する場合は、スキル内の「並列化の判断基準」と担当分け（A: 機能と利用者メリット、B: 操作方法、C: アーキテクチャ・技術スタック、D: 必要環境）に従う。統合と `.pptx` への反映・QA は1つのフローで行う。

**参照ファイル**
- スキル本体: [.cursor/skills/portfolio-copilot-briefing-deck/SKILL.md](.cursor/skills/portfolio-copilot-briefing-deck/SKILL.md)
- 推奨構成・配色・QA: [.cursor/skills/portfolio-copilot-briefing-deck/deck-guide.md](.cursor/skills/portfolio-copilot-briefing-deck/deck-guide.md)

## Hooks（フック）

`.cursor/hooks.json` で、Cursor Agent のライフサイクルにスクリプトを紐付けている。実体は `.cursor/hooks/` にあり、次のように動作する。

| イベント | スクリプト | 説明 |
|----------|------------|------|
| beforeShellExecution（`git commit` / `git push` に一致） | `hooks/pre-commit-check.sh` | テスト実行。失敗時は exit 2 でコマンドをブロックする。 |
| afterFileEdit | `hooks/docs-sync-check.sh` | `docs/specs` の見直しリマインドを表示する。 |
| afterFileEdit | `hooks/lint-test-on-change.sh` | 変更後に `./gradlew spotlessApply` を実行する。 |

設定の詳細や手動テスト方法は [.cursor/README.md](.cursor/README.md) を参照。

## ローカル実行
1. AI対話・提案書ドラフト用に `OPENAI_API_KEY` を環境変数に設定する（未設定時はAI機能利用時にエラーになります）
2. `./gradlew bootRun` を実行する
3. [http://localhost:8080/portfolios](http://localhost:8080/portfolios) を開く

Anthropic API を使う場合は `app.ai.provider=anthropic` と `ANTHROPIC_API_KEY` を設定する。

