---
name: sync-default-branch
description: デフォルトブランチをリモートの最新と同期する。リモートの変更を取り込みたいときに呼び出す。
disable-model-invocation: true
---

## 手順

1. `git status --porcelain` で未コミットの変更を確認
2. 未コミットの変更がある場合は「未コミットの変更があります。先にコミットまたはstashしてください」と伝え、**処理を停止**
3. 変更がない場合、以下を順に実行する:
   - `git fetch origin`
   - デフォルトブランチへチェックアウト（例: `git checkout main`）
   - `git merge origin/main --ff-only`（ブランチ名は環境に合わせる）
4. 完了後、現在のコミットハッシュと直近のコミットメッセージを表示して報告する
