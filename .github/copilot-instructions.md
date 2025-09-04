# DroidKaigi 2025 - Comprehensive Code Review Instructions

This document provides comprehensive code review guidelines for the DroidKaigi 2025 conference application, a sophisticated Kotlin Multiplatform (KMP) project using advanced architectural patterns.

## Project Overview

- **Architecture**: Kotlin Multiplatform supporting Android, iOS, and Desktop
- **Key Technologies**: Metro DI, Soil data fetching, Compose Multiplatform, Navigation3
- **Testing**: BDD-style multiplatform UI testing with Robot pattern and Roborazzi

---

## Architecture Review Guidelines

### 1. Metro Dependency Injection

**✅ Ensure proper Metro DI patterns:**
- Screen contexts must use `@ContributesGraphExtension` annotation
- All dependencies should be resolved at compile time
- Screen contexts must extend `ScreenContext` interface
- Factory interfaces must be properly scoped with `@ContributesGraphExtension.Factory`

**Example pattern to validate:**
```kotlin
@ContributesGraphExtension(TimetableScope::class)
interface TimetableScreenContext : ScreenContext {
    val timetableQueryKey: TimetableQueryKey
    
    @ContributesGraphExtension.Factory(AppScope::class)
    fun interface Factory {
        fun createTimetableScreenContext(): TimetableScreenContext
    }
}
```

**❌ Red flags:**
- Missing `@ContributesGraphExtension` annotations
- Direct usage of composition locals for dependencies
- Runtime dependency resolution instead of compile-time

### 2. Context Parameters Pattern

**✅ Verify context parameter usage:**
- All screen composables must use context parameters for dependency injection
- Context parameters provide semantic meaning and scope restriction
- Proper context receiver syntax: `context(screenContext: XXXScreenContext)`

**Example pattern:**
```kotlin
context(screenContext: TimetableScreenContext)
@Composable
fun TimetableScreenRoot(
    onSearchClick: () -> Unit,
    onTimetableItemClick: (TimetableItemId) -> Unit,
) {
    // Implementation
}
```

**❌ Red flags:**
- Missing context parameters in screen entry points
- Incorrect context parameter syntax
- Bypassing context parameters for dependency access

### 3. Soil Data Management

**✅ Validate data fetching patterns:**
- Use `SoilDataBoundary` to separate data fetching from UI logic
- Implement proper `QueryKey`, `SubscriptionKey`, and `MutationKey` patterns
- Data should be guaranteed available within `SoilDataBoundary` content lambda
- Proper error handling and loading states

**Example pattern:**
```kotlin
SoilDataBoundary(
    state1 = rememberQuery(screenContext.timetableQueryKey),
    state2 = rememberSubscription(screenContext.favoriteTimetableIdsSubscriptionKey),
    fallback = SoilFallbackDefaults.appBar(...),
) { timetable, favoriteTimetableItemIds ->
    // UI with guaranteed data availability
}
```

**❌ Red flags:**
- Direct repository usage instead of Soil patterns
- Missing `SoilDataBoundary` for data-dependent UI
- Improper error handling in data layer

---

## Screen Implementation Review

### 1. File Naming Conventions

**✅ Verify naming patterns:**
- Screen entry points: `XXXScreenRoot.kt`
- Screen contexts: `XXXScreenContext.kt`
- Presenters: `xxxScreenPresenter()` function
- Test robots: `XXXScreenRobot.kt`

### 2. Screen Structure Pattern

**✅ Validate screen structure:**
1. **Entry Point**: `XXXScreenRoot` with context parameters
2. **Data Boundary**: `SoilDataBoundary` for data fetching
3. **Event Handling**: `rememberEventFlow<XXXScreenEvent>()`
4. **Presenter**: Composable presenter function for UI state construction
5. **UI Layer**: Pure UI composables receiving state and events

**Example structure:**
```kotlin
context(screenContext: TimetableScreenContext)
@Composable
fun TimetableScreenRoot(...) {
    SoilDataBoundary(...) { data ->
        val eventFlow = rememberEventFlow<TimetableScreenEvent>()
        val uiState = timetableScreenPresenter(eventFlow, data)
        TimetableScreen(uiState, eventFlow)
    }
}
```

### 3. Presenter Pattern

**✅ Validate presenter implementation:**
- Presenters should be Composable functions
- Handle events via `EventEffect`
- Construct UI state based on data and user interactions
- Use proper mutation keys for state updates

**❌ Red flags:**
- Non-composable presenters
- Direct state mutation without proper keys
- Missing event handling

---

## Testing Review Guidelines

### 1. BDD-Style Testing

**✅ Ensure proper BDD structure:**
- Tests use `describe`/`doIt`/`itShould` pattern
- Clear behavioral descriptions
- Proper test organization with nested describes

**Example pattern:**
```kotlin
val describedBehaviors = describeBehaviors<TimetableScreenRobot>("TimetableScreen") {
    describe("when server is operational") {
        doIt {
            setupTimetableServer(ServerStatus.Operational)
            setupTimetableScreenContent()
        }
        itShould("show loading indicator") {
            captureScreenWithChecks {
                checkLoadingIndicatorDisplayed()
            }
        }
    }
}
```

### 2. Robot Pattern

**✅ Validate robot implementation:**
- Test robots use dependency injection (`@Inject`)
- Robots implement interfaces for composability
- Context parameters for UI test interactions
- Screenshot testing with Roborazzi integration

**❌ Red flags:**
- Direct UI testing without robots
- Missing dependency injection in test setup
- Incomplete test coverage for error states

### 3. Multiplatform Testing

**✅ Verify multiplatform test setup:**
- Tests run on Android, JVM, and iOS using expect/actual
- Test dependency graphs created with Metro
- Proper platform-specific test runners

---

## Code Quality Guidelines

### 1. Kotlin Multiplatform Patterns

**✅ Review KMP implementation:**
- Proper expect/actual declarations
- Platform-specific implementations where needed
- Shared business logic in commonMain
- Platform-specific UI adaptations when necessary

### 2. Compose Multiplatform

**✅ Validate Compose usage:**
- Proper resource handling with Compose Resources
- Platform-specific UI adaptations
- Consistent design system usage
- Accessibility considerations

### 3. Error Handling

**✅ Ensure robust error handling:**
- Proper error boundaries in data layer
- User-friendly error states in UI
- Graceful degradation for network issues
- Proper loading states

---

## Data Layer Review

### 1. API Integration

**✅ Validate API patterns:**
- Use `@ContributesBinding` for implementations
- Proper QueryKey implementations with caching
- Network error handling
- Data transformation patterns

### 2. Caching Strategy

**✅ Review caching implementation:**
- Proper use of Soil's `SwrClient` for runtime caching
- Preload data implementation where appropriate
- Cache invalidation strategies

---

## Performance Considerations

### 1. Compose Performance

**✅ Check Compose optimizations:**
- Proper state management to avoid unnecessary recompositions
- Use of `remember` and `derivedStateOf` appropriately
- Lazy loading for large lists
- Image loading optimizations

### 2. Memory Management

**✅ Review memory usage:**
- Proper lifecycle management
- Resource cleanup in effects
- Avoiding memory leaks in long-running operations

---

## Security and Best Practices

### 1. Data Security

**✅ Ensure secure data handling:**
- No sensitive data in logs
- Proper API key management
- Secure network communications

### 2. Code Organization

**✅ Validate code structure:**
- Proper module organization following feature-based architecture
- Clear separation of concerns
- Consistent code style and formatting

---

## Common Anti-Patterns to Watch For

**❌ Avoid these patterns:**
1. **Repository Pattern**: Use Soil data fetching instead
2. **Composition Locals**: Use context parameters for DI
3. **Direct API Calls**: Use QueryKeys for data fetching
4. **Manual Caching**: Rely on Soil's automatic caching
5. **Imperative Navigation**: Use proper Navigation3 patterns
6. **Mixed UI/Business Logic**: Maintain clear separation with presenters

---

## Review Checklist

### Before Approving Changes:

- [ ] **Architecture**: Follows Metro DI and Soil patterns correctly
- [ ] **Testing**: Comprehensive BDD tests with Robot pattern
- [ ] **Performance**: No unnecessary recompositions or memory leaks
- [ ] **Multiplatform**: Proper KMP patterns and platform adaptations
- [ ] **Code Quality**: Consistent with project conventions
- [ ] **Documentation**: Code is self-documenting with clear intent
- [ ] **Error Handling**: Robust error states and user feedback
- [ ] **Security**: No exposed sensitive information

### Quality Gates:

1. **All tests passing** across all platforms
2. **No compiler warnings** or linting errors
3. **Screenshots tests verified** with Roborazzi
4. **Performance regression** checks completed
5. **Accessibility** considerations addressed

---

## Questions to Ask During Review

1. **Architecture**: Does this follow our Metro DI and Soil data patterns?
2. **Testing**: Are there comprehensive tests covering happy path and error states?
3. **Performance**: Could this cause performance issues or memory leaks?
4. **Maintainability**: Is the code easy to understand and modify?
5. **Multiplatform**: Does this work correctly across all target platforms?
6. **User Experience**: Does this provide a good user experience with proper loading/error states?

---

This comprehensive review guide ensures consistent quality across the sophisticated DroidKaigi 2025 codebase while maintaining the architectural patterns that make this project unique and maintainable.