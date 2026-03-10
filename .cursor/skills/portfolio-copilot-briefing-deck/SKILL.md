---
name: portfolio-copilot-briefing-deck
description: Create a finance-oriented explanation deck under docs for the Portfolio Copilot application. Use when the user explicitly asks to generate presentation materials or slides about this app's user-facing features, operation flow, architecture, technology stack, or required environment/specs.
---

# Portfolio Copilot Briefing Deck

## 目的
`docs/` 配下に、このアプリケーションを説明する `.pptx` 資料を作成する。対象は社外のお客様で、金融機関向けに信頼感のある落ち着いた印象を優先する。

## まずやること
1. `date` コマンドで現在日時を確認する。
2. 既存の `pptx` スキルを読む。
3. 次の情報源を読む。
   - `README.md`
   - `docs/specs/portfolio-copilot/requirements.md`
   - `docs/specs/portfolio-copilot/design.md`
   - `docs/specs/portfolio-copilot/tasks.md`
4. 必要なら実装コードや設定も追加で確認し、資料内容を事実ベースで固める。

## 出力先
- 既定の出力先は `docs/slides/portfolio-copilot-briefing.pptx`
- 補助テキストを残す場合は `docs/slides/portfolio-copilot-briefing.md`
- 新しく `docs/` 配下に資料を追加したら、必要に応じて `README.md` のドキュメント一覧も更新する

## 資料に必ず含める内容
1. 機能
   - 主にユーザー目線で、何ができるかを簡潔に説明する
   - ポートフォリオ閲覧、AI対話、提案書ドラフト生成を中心に整理する
2. 操作方法
   - 初見の利用者が追える順序で説明する
   - 画面遷移や操作ステップは 1, 2, 3... の番号で示す
3. アーキテクチャと技術スタック
   - UI、Web、サービス、リポジトリ、SQLite、AI API 連携の関係が分かるようにする
   - 図を使う場合はすべてのノードに番号を振る
4. 必要な環境とスペック
   - 起動に必要な前提、利用する API キー、ローカル実行条件を整理する
   - デモ向けの前提と制約があれば明記する

## 進め方
1. まず 6-8 枚程度のスライド構成を決める。
2. 構成は [deck-guide.md](deck-guide.md) の章立てを基本にする。
3. ユーザーが速度重視を求める場合や、章ごとの独立性が高い場合は `.cursor/agents/portfolio-copilot-parallel-briefing-deck.md` の進め方で本文ドラフトを並列化してよい。
4. 並列化する前に、章立て、スライド番号、担当範囲を確定する。
5. 並列担当は次のいずれかに分ける。
   - A: 機能と利用者メリット
   - B: 操作方法と画面遷移
   - C: アーキテクチャと技術スタック
   - D: 必要環境、実行条件、デモ前提
6. 各担当には、対象スライド番号、タイトル案、本文案、図や表の要否、根拠ファイル、未確定事項を返させる。
7. 回収したドラフトは、親タスク側で統合してから `.pptx` に反映する。
8. 最終的な `.pptx` の生成、レイアウト調整、QA は 1 つのフローで実施する。同じ `.pptx` を複数担当が同時編集しない。
9. スライド作成時は `pptx` スキルの作成手順と QA 手順に従う。
10. `.pptx` を作成したら、テキスト抽出と画像確認の両方で見直す。
11. 問題を見つけたら修正し、少なくとも 1 回は修正後の再確認を行う。

## 並列化の判断基準
- 並列化するのは、主に調査、本文ドラフト、図のラフ案までに留める。
- 小規模な修正や 1-2 枚の資料では、並列化しない方が速いことが多い。
- 情報が衝突した場合は、README、仕様書、実装、設定を一次情報として再確認する。
- 並列化後は、全スライドで用語、文体、粒度、メッセージの重複を必ず整える。

## 表現ルール
- 日本語で書く。
- 誇張表現は避け、事実と利点を淡々と説明する。
- `「」` は使わない。
- 金融機関向けを意識し、安定感・可読性・整然さを優先する。
- 断定できないことは書かない。仕様や実装で確認できる内容だけを載せる。

## デザイン指針
- ダークネイビー、スレート、ホワイトを基調に、彩度を抑えたアクセントを使う。
- 派手な演出や過度な装飾は避ける。
- タイトル、図、表、アイコンの役割を明確に分ける。
- 箇条書きを詰め込みすぎず、1 枚 1 メッセージを守る。

## 完了条件
- `.pptx` が `docs/` 配下に出力されている。
- 必須 4 テーマが含まれている。
- `pptx` スキルの QA を通している。
- 追加した資料への参照が必要なら `README.md` を更新している。
