# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this
repository.

## Project

Native Android app (Jetpack Compose) consuming the TMDB API. Multi-module clean architecture:
`:app` → `:domain` → `:data`. Build config is centralized in the `gradle/libs.versions.toml` version
catalog; all versions/plugins are referenced as `libs.*`.

> The `data/api/` directory is a scaffolded module **not** wired into `settings.gradle.kts` (only
`:app`, `:data`, `:domain` are included). Ignore it unless explicitly adding it.

## Common commands

Build uses product flavors (`dev`, `prod`) × build types (`debug`, `release`), producing variants
like `devDebug`, `prodRelease`.

```bash
./gradlew assembleDevDebug              # build the dev/debug APK
./gradlew installDevDebug               # build + install on a device/emulator
./gradlew testDevDebugUnitTest          # run all JVM unit tests (app module variant)
./gradlew :data:testDebugUnitTest       # run unit tests for a single module
./gradlew :data:testDebugUnitTest --tests "com.alican.data.service.ApiServiceTest"   # single test class
./gradlew connectedDevDebugAndroidTest  # instrumented tests (needs device/emulator)
./gradlew lint                          # Android lint
```

`:data` and `:domain` are Android library modules (no flavors), so their unit-test tasks are
`testDebugUnitTest` / `testReleaseUnitTest`.

## Architecture

**Layering (strict dependency direction):**

- `:domain` — pure Kotlin business layer. Holds `interactors/` (use cases), `repository/`
  interfaces, `models/`, `mappers/` (response → UI model), and `ui_models/` (per-screen `UiState`
  types). Depends on nothing app/data-specific.
- `:data` — implements domain repository interfaces. Ktor (OkHttp engine) remote (`ApiService`/
  `ApiServiceImpl`), Room local (`AppDatabase`, DAOs, entities), `response/` DTOs, `mappers/`.
  Network base URL, API token, Room DB name, and certificate pin are injected via `BuildConfig`
  fields.
- `:app` — Compose UI (`ui/<feature>/`), ViewModels, DI wiring, navigation, and `helpers/` (theme,
  datastore, security, notification, navigation3).

**Result wrappers (three distinct types — don't conflate them):**

- `data/utils/ResultWrapper` + `safeCall(client) { ... }` — wraps raw Ktor calls in
  `ApiServiceImpl`.
- `domain/utils/Resource<T>` (`Success`/`Error`) — what repository methods return.
- `domain/ui_models/BaseUIModel<T>` (`Success`/`Error`/`Loading`/`Empty`) — what interactors expose
  to the UI.

**MVI via `BaseViewModel`** (`app/.../utils/BaseViewModel.kt`): generic over
`<UiState, UiEvent, UiEffect>`. State is a `StateFlow` built with Kotlin **explicit backing fields
** (the module enables `-Xexplicit-backing-fields`). One-time effects go through a `Channel`.
Subclasses implement `initialState(savedStateHandle)` and `handleEvent(event)`; update state with
`updateState { copy(...) }` and emit effects with `sendEffect(...)`. Each screen typically has
matching `XUIEvents` / `XUIState` files.

**Pagination:** `domain/interactors/BasePaginatedInteractor<T, R>` + `PaginationStateManager` drive
paged lists (`loadFirstPage`/`loadNextPage`/`retry`/`reset`), exposing a `PaginationUIModel` flow.

**Navigation (Navigation 3):** custom abstraction under `app/.../helpers/navigation3/`. Inject
`AppRouter` (bound to `AppRouterImpl`) for `navigateTo(NavKey)` / `navigateBack()` /
`navigateToBottomBarTab(...)`. Routes are `NavKey`s; bottom-bar tabs are `BottomNavRoutes`.
`NavigationStateProvider` must be `initialize`d once in `MainActivity` before routing works.

**DI (Hilt, all `SingletonComponent`):** `NetworkModule` (`:data`, provides `HttpClient`, Room,
`ApiService`, `FirebaseAuth`), `DomainModule` (app-wide `CoroutineScope(SupervisorJob())`), and in
`:app`: `AppModule`, `RepositoryModule` (binds repo impls), `NavigationModule` (binds `AppRouter`).
`App` is `@HiltAndroidApp`; `MainActivity` is `@AndroidEntryPoint`.

**Firebase:** Auth, Messaging (`NotificationMessagingService`), Crashlytics, Analytics. Requires a
`google-services.json` per flavor.

## Conventions

- Kotlin version is pinned to match KSP (`kspVersion`) and Hilt's supported metadata — bumping
  `kotlin` in the catalog can break Hilt KSP with a "metadata version" error; keep them aligned.
- `:data` compiles at JVM 11 (Ktor requirement); `:app` at JVM 1.8. Match the target of the module
  you edit.
- New remote endpoint: add to `ApiService` interface + `ApiServiceImpl` (via `safeCall`) →
  repository method returning `Resource` → interactor exposing `BaseUIModel`/pagination →
  ViewModel → Compose screen.

---

# context-mode — MANDATORY routing rules

You have context-mode MCP tools available. These rules are NOT optional — they protect your context
window from flooding. A single unrouted command can dump 56 KB into context and waste the entire
session.

## BLOCKED commands — do NOT attempt these

### curl / wget — BLOCKED

Any Bash command containing `curl` or `wget` is intercepted and replaced with an error message. Do
NOT retry.
Instead use:

- `ctx_fetch_and_index(url, source)` to fetch and index web pages
- `ctx_execute(language: "javascript", code: "const r = await fetch(...)")` to run HTTP calls in
  sandbox

### Inline HTTP — BLOCKED

Any Bash command containing `fetch('http`, `requests.get(`, `requests.post(`, `http.get(`, or
`http.request(` is intercepted and replaced with an error message. Do NOT retry with Bash.
Instead use:

- `ctx_execute(language, code)` to run HTTP calls in sandbox — only stdout enters context

### WebFetch — BLOCKED

WebFetch calls are denied entirely. The URL is extracted and you are told to use
`ctx_fetch_and_index` instead.
Instead use:

- `ctx_fetch_and_index(url, source)` then `ctx_search(queries)` to query the indexed content

## REDIRECTED tools — use sandbox equivalents

### Bash (>20 lines output)

Bash is ONLY for: `git`, `mkdir`, `rm`, `mv`, `cd`, `ls`, `npm install`, `pip install`, and other
short-output commands.
For everything else, use:

- `ctx_batch_execute(commands, queries)` — run multiple commands + search in ONE call
- `ctx_execute(language: "shell", code: "...")` — run in sandbox, only stdout enters context

### Read (for analysis)

If you are reading a file to **Edit** it → Read is correct (Edit needs content in context).
If you are reading to **analyze, explore, or summarize** → use
`ctx_execute_file(path, language, code)` instead. Only your printed summary enters context. The raw
file content stays in the sandbox.

### Grep (large results)

Grep results can flood context. Use `ctx_execute(language: "shell", code: "grep ...")` to run
searches in sandbox. Only your printed summary enters context.

## Tool selection hierarchy

1. **GATHER**: `ctx_batch_execute(commands, queries)` — Primary tool. Runs all commands,
   auto-indexes output, returns search results. ONE call replaces 30+ individual calls.
2. **FOLLOW-UP**: `ctx_search(queries: ["q1", "q2", ...])` — Query indexed content. Pass ALL
   questions as array in ONE call.
3. **PROCESSING**: `ctx_execute(language, code)` | `ctx_execute_file(path, language, code)` —
   Sandbox execution. Only stdout enters context.
4. **WEB**: `ctx_fetch_and_index(url, source)` then `ctx_search(queries)` — Fetch, chunk, index,
   query. Raw HTML never enters context.
5. **INDEX**: `ctx_index(content, source)` — Store content in FTS5 knowledge base for later search.

## Subagent routing

When spawning subagents (Agent/Task tool), the routing block is automatically injected into their
prompt. Bash-type subagents are upgraded to general-purpose so they have access to MCP tools. You do
NOT need to manually instruct subagents about context-mode.

## Output constraints

- Keep responses under 500 words.
- Write artifacts (code, configs, PRDs) to FILES — never return them as inline text. Return only:
  file path + 1-line description.
- When indexing content, use descriptive source labels so others can `ctx_search(source: "label")`
  later.

## ctx commands

| Command       | Action                                                                                |
|---------------|---------------------------------------------------------------------------------------|
| `ctx stats`   | Call the `ctx_stats` MCP tool and display the full output verbatim                    |
| `ctx doctor`  | Call the `ctx_doctor` MCP tool, run the returned shell command, display as checklist  |
| `ctx upgrade` | Call the `ctx_upgrade` MCP tool, run the returned shell command, display as checklist |
