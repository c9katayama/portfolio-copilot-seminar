---
name: pull-request-review
description: プルリクエストのレビューを行うときに使う。差分とコメントを確認し、人が投稿するレビューコメントや返信文案を作る場合に呼び出す。
disable-model-invocation: true
---

## 概要

GitHub CLI（`gh`）とAPIを使ってプルリクエストの情報を取得し、レビュー内容を整理する。AIはコメント返信やレビュー結果を直接投稿せず、人が投稿するための文案を作る。

## 使用タイミング

- PRの差分を確認し、指摘事項を整理したいとき
- PRのコメントを取得し、返信文案を作りたいとき
- 承認、コメント、変更依頼のレビュー文案を作りたいとき

## 原則

- AIは `gh api` や `gh pr review` を使ってレビューコメントや返信を直接投稿しない
- 投稿は、内容を確認した人がGitHub上で行う
- AIは取得、要約、修正文案の作成までを担当する

## 行番号付き差分の取得

インラインコメントを正確な行に付けるため、行番号付きで差分を取得する。`L` は変更前（削除行）、`R` は変更後（追加行）の行番号。

> **Note**: macOSのBSD awkはGNU awkの拡張（`match()` の第3引数）に対応していないため、perlを使用する。

```bash
gh pr diff <PR番号> | perl -ne '
if (/^diff --git/) { print; next }
if (/^@@.*-(\d+).*\+(\d+)/) { $old=$1; $new=$2; print; next }
if (/^-/) { printf "L%-4d      | %s", $old++, $_; next }
if (/^\+/) { printf "      R%-4d| %s", $new++, $_; next }
if (/^ /) { printf "L%-4d R%-4d| %s", $old++, $new++, $_; next }
print
'
```

特定ファイルのみ取得する場合:

```bash
gh pr diff <PR番号> -- <ファイルパス> | perl -ne '...'
```

## インラインコメント案の作成

取得した差分をもとに、次を出力する:

1. 対象ファイル
2. 対象行（`R` または `L`）
3. 指摘内容の要約
4. GitHubに貼り付けるコメント文案

## コメントの取得

```bash
# PRの会話コメントを取得
gh api repos/<OWNER>/<REPO>/issues/<PR番号>/comments

# インライン（レビュー）コメントを取得
gh api repos/<OWNER>/<REPO>/pulls/<PR番号>/comments
```

## コメントへの返信

レビューコメントを取得したら、次を出力する:

1. 指摘内容の要約
2. 必要ならコード修正案
3. GitHubに貼り付ける返信文案

## レビュー結果の整理

レビュー全体を確認したら、次のいずれかの文案を作る:

- 承認コメント案
- 変更依頼コメント案
- コメントのみのレビュー案

## PR情報の取得

```bash
gh pr view <PR番号>
gh pr diff <PR番号> --name-only
gh pr checks <PR番号>
```

## 注意

- インラインコメントでは、差分の `R` 行番号（`side=RIGHT`）または `L` 行番号（`side=LEFT`）を使う
- `<OWNER>/<REPO>` はリポジトリに合わせて置き換える（`gh repo view --json nameWithOwner -q .nameWithOwner` で取得可能）
- GitHubへの投稿はユーザーが内容を確認・編集したうえで行う
