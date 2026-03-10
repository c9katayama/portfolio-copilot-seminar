---
name: commit-and-push
description: 変更内容を確認し、人が実行するコミット・push手順とメッセージ案を整理する。作業をリモートへ反映する前の準備で使う。
disable-model-invocation: true
---

変更内容を確認し、人が実行するコミット・push手順を整理する。

## 原則

- AIは `git add`、`git commit`、`git push` を実行しない
- コミットとpushは、内容を確認した人が実行する
- デフォルトブランチへの直接pushは推奨しない

## 手順

1. `git rev-parse --show-toplevel` でリポジトリルートを確認
2. `git status` で変更ファイルを確認
3. `git branch --show-current` で現在のブランチを確認
   - **デフォルトブランチ（main/master）の場合**: 「デフォルトブランチへの直接pushは推奨されません。作業用ブランチの作成を推奨します。続行しますか？」と確認する
   - ユーザーが中止を選んだら処理を停止する
   - 続行を選んだ場合のみ次へ進む
4. `git diff --stat` で変更概要を確認
5. 変更内容に応じて、適切なコミットメッセージ案を作成
6. 人が実行するためのコマンド例を提示する
7. 必要なら push 前に `verify-before-push` の実行を案内する
8. 実行はユーザー自身が行うよう案内して終了する

## コミットメッセージ

- 形式: `<type>: <description>`
- type 例: feat, fix, docs, chore, refactor, test, style

## 出力内容

次をまとめて出力する:

1. 変更概要
2. 推奨コミットメッセージ
3. 人が実行するコマンド例

```bash
git add <対象ファイル>
git commit -m "<type>: <description>"
git push origin <current-branch>
```
