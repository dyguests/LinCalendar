# LinCalendar

[![](https://jitpack.io/v/dyguests/LinCalendar.svg)](https://jitpack.io/#dyguests/LinCalendar)

A composable Calendar.

<img src="https://github.com/dyguests/LinCalendar/assets/7933189/3c78bbbd-7283-4066-8206-6c494a87f716" alt="default" width="360"/>
<img src="https://github.com/dyguests/LinCalendar/assets/7933189/f96b275b-2ba1-40b3-895e-0026c5267460" alt="default" width="360"/>
<img src="https://github.com/dyguests/LinCalendar/assets/7933189/c3f2d0ab-ed8a-429c-82ba-d672e9d65324" alt="default" width="360"/>

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
            implementation 'com.github.dyguests:LinCalendar:0.3.4'
        }
    ```

# Structure

- `LinCalendar`
    - `monthsField`
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

# Todo List

- [ ] 隐藏无关细节。比如monthFiled中的计算逻辑

# Reference

- [kizitonwose/Calendar](https://github.com/kizitonwose/Calendar)

# License

[MIT LICENSE](LICENSE)
