# セキュリティチェック 詳細参照

SKILL.md の補足。OWASP と Java に特化したチェック項目の詳細。

## OWASP Top 10:2021 と Java での確認例

| カテゴリ | 確認例（Java/Spring） |
|----------|------------------------|
| A01 アクセス制御 | `@PreAuthorize` の欠落、IDOR の可能性（パスに userId 等）、CSRF トークン |
| A02 暗号 | `MessageDigest.getInstance("MD5")` 等の弱いアルゴリズム、平文パスワード保存 |
| A03 インジェクション | `String.format` / 連結による SQL、`Runtime.exec()` への未検証入力、`ScriptEngine` |
| A04 設計 | 新規 API の認可・レート制限・入力検証の有無 |
| A05 設定 | `management.endpoints.web.exposure.include=*`、デフォルトパスワード |
| A07 認証 | ブルートフォース対策、セッションタイムアウト、パスワードポリシー |
| A08 整合性 | 信頼できない入力のデシリアライズ、依存の署名検証 |
| A09 ログ | 認証失敗・認可拒否のログ、機密情報のマスキング |
| A10 SSRF | `RestTemplate` / `WebClient` に渡す URL の検証・スキーム制限 |

## 依存関係チェック（Gradle）

OWASP Dependency Check プラグインの例（`build.gradle`）：

```groovy
plugins {
    id 'org.owasp.dependencycheck' version '11.0.0'  // バージョンは要確認
}
dependencyCheck {
    failBuildOnCVSS = 7
    suppressionFile = 'dependency-check-suppressions.xml'
}
```

実行: `./gradlew dependencyCheckAnalyze`

## シークレット検索の追加パターン

- `private.key` / `-----BEGIN.*KEY-----`
- `aws_access_key` / `AWS_SECRET`
- `password\s*:\s*[\"'][^\"']+[\"']`（YAML/JSON 風）
- `.env` ファイルがリポジトリに含まれていないか

## 用語の統一

- 認可: 権限の有無のチェック（Authorization）
- 認証: 本人確認（Authentication）
- 機密情報: パスワード、APIキー、トークン、秘密鍵など
