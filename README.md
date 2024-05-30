# LinCalendar

[![](https://jitpack.io/v/dyguests/LinCalendar.svg)](https://jitpack.io/#dyguests/LinCalendar)

A composable Calendar.

# Dependency

1. Add it in your root build.gradle at the end of repositories:

    ```gradle
        dependencyResolutionManagement {
            repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
            repositories {
                mavenCentral()
                maven { url 'https://jitpack.io' }
            }
        }
    ```

2. Add the dependency

    ```gradle
        dependencies {
            implementation 'com.github.dyguests:LinCalendar:0.1.4'
        }
    ```

# Structure

- `LinCalendar`
    - `HorizontalPager` cannot be customized.
        - `monthFiled`
            - `weekHeaderField`
                - `dayHeaderField`
            - `weekField`
                - `dayField`

# Usage

```kotlin
val state = rememberLinCalendarState()

LinCalendar(
    state = state,
)
```

# Reference

- TODO

# License

[MIT LICENSE](LICENSE)