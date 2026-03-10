---
name: security-check
description: PR前にJavaプロジェクトのセキュリティチェックを行う。OWASP Top 10に基づくコード確認、依存関係の脆弱性、シークレット混入、設定の確認を行う。トリガー例：PR前のチェック、セキュリティチェック、security check、脆弱性確認。
---

# セキュリティチェック

PR作成前に、コード・依存関係・シークレット・設定をOWASPを軸に確認する。

## 使用タイミング

- プルリクエストを作成する前にセキュリティ観点で確認したいとき
- ユーザーが「セキュリティチェック」「PR前チェック」「脆弱性確認」と依頼したとき

## 実行順序

次の順で実施する。問題があれば該当箇所と修正案を提示し、修正後に該当ステップから再開する。

1. 依存関係の脆弱性
2. シークレット・認証情報の混入
3. コード（OWASP Top 10）
4. 設定・インフラ

---

## 1. 依存関係の脆弱性

### 実行

Gradle で OWASP Dependency Check を使う場合の例（プラグイン導入済みであること）。

```bash
./gradlew dependencyCheckAnalyze
```

未導入の場合は、変更差分に含まれる `build.gradle` / `build.gradle.kts` や `gradle.lockfile` を確認し、既知の脆弱性があるライブラリが追加されていないかコードベースを検索して確認する。

### 確認ポイント

- 新規追加された依存の CVE 有無（Web検索または NVD で確認）
- バージョンが古いままの依存（特にログ・シリアライズ・HTTPクライアント系）

---

## 2. シークレット・認証情報の混入

### 確認対象

- パスワード、APIキー、トークン、秘密鍵がソース・設定ファイルにハードコードされていないか
- `application.properties` / `application.yml` に平文の機密情報が無いか（本番用は環境変数やシークレット管理を推奨）

### 検索例

```bash
# ハードコードされたパスワード・キー・トークンの疑いがあるパターン
rg -i -n "password\s*=\s*[\"'][^\"']+[\"']" --type java .
rg -i -n "apikey|api_key|secret\s*=\s*[\"'][^\"']+[\"']" --type java .
rg -i -n "Bearer\s+[a-zA-Z0-9_-]+\.[a-zA-Z0-9_-]+\.[a-zA-Z0-9_-]+" --type java .
```

設定ファイルも同様に確認する。

---

## 3. コード（OWASP Top 10）

Java コードを OWASP Top 10:2021 に沿って確認する。差分がある場合は変更箇所を優先し、必要に応じて関連する既存コードも見る。

### A01 – Broken Access Control（アクセス制御の不備）

- 認可チェックが抜けていないか（Spring の `@PreAuthorize` / `@Secured` や明示的な権限判定）
- ユーザーが他人のリソースに直接アクセスできるパス・パラメータが無いか
- CSRF 対策が有効か（フォーム・状態変更を行うAPI）

### A02 – Cryptographic Failures（暗号の失敗）

- パスワードのハッシュに強力なアルゴリズム（BCrypt、Argon2 等）を使っているか
- 機密データの保存・送信に適切な暗号化と鍵管理をしているか
- TLS 利用の前提が崩れていないか

### A03 – Injection（インジェクション）

- SQL:  PreparedStatement / 名前付きパラメータや JPA のクエリで文字列連結していないか
- OS コマンド・スクリプト実行: 外部入力のサニタイズや許可リストがあるか
- XSS: 出力のエスケープや CSP の考慮があるか（Web の場合）

### A04 – Insecure Design（不安全な設計）

- 変更が脅威モデルや想定攻撃面を変えていないか（新規エンドポイント・新規権限・新規外部連携）

### A05 – Security Misconfiguration（セキュリティ設定の不備）

- 不要なデバッグ・管理エンドポイントが本番で有効になっていないか
- デフォルト認証情報の変更、エラーメッセージの過度な詳細表示の有無

### A06 – Vulnerable and Outdated Components

- 上記「1. 依存関係の脆弱性」で対応

### A07 – Identification and Authentication Failures（認証の失敗）

- 認証バイパスや弱いパスワードポリシー、セッション固定・予測可能なセッションID の有無
- 多要素認証が必要な箇所でスキップされていないか

### A08 – Software and Data Integrity Failures（整合性の失敗）

- 依存の取得元・整合性（署名、ハッシュ）、デシリアライズの信頼範囲

### A09 – Security Logging and Monitoring Failures（ログ・監視の失敗）

- 認証失敗・認可拒否・重要な操作がログに記録されているか
- 機密情報（パスワード、トークン）がログに出力されていないか

### A10 – Server-Side Request Forgery (SSRF)

- サーバー側で外部URLへリクエストする場合、入力URLの検証・許可リスト・スキーム制限があるか

---

## 4. 設定・インフラ

### 確認ポイント

- CORS: 必要以上に広い `*` オリジンが本番で指定されていないか
- HTTPS: 本番では HTTPS 前提の設定になっているか
- ヘッダー: セキュリティヘッダー（X-Frame-Options、CSP 等）の設定方針が崩れていないか
- 機密設定: `application.properties` / `application.yml` の本番用機密がリポジトリに平文でコミットされていないか

---

## 結果報告

完了後、次の形式で報告する。

```markdown
## セキュリティチェック 結果

| 項目           | 結果 |
|----------------|------|
| 依存関係       | OK / NG |
| シークレット   | OK / NG |
| コード(OWASP)  | OK / NG |
| 設定・インフラ | OK / NG |

（NG の場合は該当箇所・理由・修正案を記載）
```

---

## 参考

- [OWASP Top 10:2021](https://owasp.org/Top10/2021/)
- [OWASP Cheat Sheet Series](https://cheatsheetseries.owasp.org/)
- 詳細なチェックリストは [reference.md](reference.md) を参照
