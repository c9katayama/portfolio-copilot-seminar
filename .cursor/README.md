# .cursor 構成

## スキル運用（コマンド移行済み）

ワークフローはすべて `.cursor/skills/<name>/SKILL.md` に集約している。`/migrate-to-skills` により従来の `.cursor/commands/*.md` はスキルに統合済みで、`/` からはスキル名で呼び出す。

### 主なスキル

| スキル名 | 用途 |
|----------|------|
| baseline-ui | UIベースライン（コンポーネント・アニメーション・タイポグラフィ・レイアウト・グラフィカル要素）の適用・レビュー。React/Tailwind 向け。金融・業務系では比較しやすいグラフの追加基準も含む |
| commit-and-push | 人が実行するコミット・pushの手順とメッセージ案を整理 |
| draft-pull-request | 下書き PR の作成 |
| generate-design | 要件書を元に設計書（design.md）の生成・更新 |
| implement-feature | 承認済みタスクを1つずつ実装し、アプリコード・テスト・ドキュメントの整合を保つ |
| portfolio-copilot-briefing-deck | Portfolio Copilot アプリの説明資料を `docs/` 配下に `.pptx` で作成する。機能、操作方法、アーキテクチャ、技術スタック、必要環境を金融機関向けの落ち着いたトーンで整理し、必要に応じて章ごとの本文ドラフトを並列化する |
| pull-request-review | PR差分とコメントを確認し、人が投稿するレビュー文案を作成 |
| pptx | .pptx の作成・読み取り・編集・要約。スライドデッキ、テンプレート、レイアウト、スピーカーノートなどを扱う |
| review-update-impact | 機能更新の影響範囲レビュー |
| security-check | PR前のセキュリティチェック（OWASP ベースのコード確認、依存関係の脆弱性、シークレット混入、設定の確認） |
| sync-default-branch | デフォルトブランチの同期 |
| verify-before-push | push 前のビルド・テスト実行 |

手順の追加・変更は該当する `SKILL.md` を編集する。 

### 参照

- Cursor: [Skills](https://cursor.com/docs/skills.md)（frontmatter と配置）

## Hooks（Agent 用）

`.cursor/hooks.json` で Cursor Agent のライフサイクルにスクリプトを紐付けている。実体は `.cursor/hooks/` 配下に置いている。

| イベント | スクリプト | 説明 |
|----------|------------|------|
| beforeShellExecution（`git commit` / `git push` に一致時） | `.cursor/hooks/pre-commit-check.sh` | `./gradlew spotlessApply` の後に `./gradlew test` を実行。どちらかが失敗した場合は exit 2 でコマンドをブロック。 |
| stop | `.cursor/hooks/docs-sync-check.sh` | Agent の作業完了時に、docs/specs と `README.md` の更新要否を 1 回だけ再確認する follow-up を自動投入する。`loop_count > 0` では再投入しない。 |

設定の詳細は [Cursor Docs - Hooks](https://cursor.com/docs/agent/hooks) を参照。

### フックの手動テスト

プロジェクトルートで以下を実行すると、各スクリプトを直接試せる。

```bash
# 1) docs 更新要否の follow-up 生成
printf '{"status":"completed","loop_count":0}\n' | bash .cursor/hooks/docs-sync-check.sh

# 2) コミット/プッシュ前チェック（gradlew spotlessApply と gradlew test、失敗時は exit 2）
bash .cursor/hooks/pre-commit-check.sh
```

`hooks.json` の検証（JSON とスキーマ）は次で確認できる。

```bash
python3 -c "
import json
with open('.cursor/hooks.json') as f:
    d = json.load(f)
assert d.get('version') == 1 and 'hooks' in d
print('OK: hooks.json is valid')
"
```

## Agents（サブエージェント）

`.cursor/agents/` にプロジェクト用のサブエージェントを配置している。専門タスク用のシステムプロンプトで動作する。

| 名前 | 説明 |
|------|------|
| financial-crawler | 複数の金融情報を Web から収集し、株式市場に影響がありそうなニュースのトップ10を選定。結果は `financial-report/top10-YYYY-MM-DD.md` に出力する。複数体で担当を分けて実行した場合は、統合レポートを `financial-report/weekly-report-YYYY-MM-DD.md` にまとめることもある。 |
| portfolio-copilot-parallel-briefing-deck | `portfolio-copilot-briefing-deck` スキルを土台に、Portfolio Copilot の説明資料を章ごとに分担して並列生成する。機能、操作方法、アーキテクチャ、必要環境を個別に下調べ・ドラフトし、最後に統合して `docs/slides/portfolio-copilot-briefing.pptx` を作る用途向け。 |

利用例: 「financial-crawler サブエージェントで今日の金融レポートを作成して」「financial-crawler サブエージェント4体で今週の金融レポートを作成して」「portfolio-copilot-parallel-briefing-deck サブエージェントで説明資料を並列生成して」などと依頼する。
