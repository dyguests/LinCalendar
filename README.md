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

# Usage

```kotlin
val state = rememberLinCalendarState()

LinCalendar(
    state = state,
)
```

# Properties

```kotlin
fun rememberLinCalendarState(
    initialDate: LocalDate = LocalDate.now(),
    startDate: LocalDate = LocalDate.of(1900, 1, 1),
    endDate: LocalDate = LocalDate.of(2099, 12, 31),
    initialDisplayMode: LinCalendar.DisplayMode = LinCalendar.DisplayMode.MONTHLY,
    option: LinCalendar.Option = LinCalendarDefaults.option(),
): LinCalendarState
```

```kotlin
data class Option(
    val headerHeight: Dp = 32.dp,
    val rowHeight: Dp = 36.dp,
    val firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val weekDisplayMode: WeekDisplayMode = WeekDisplayMode.FIXED_HEIGHT,
    val locale: Locale = Locale.getDefault(),
)
```

# Custom composable

Each part of LinCalendar is composable, so you can customize each part yourself.

###### Structure

- `LinCalendar`
    - `monthsField`
        - `monthFiled`
            - `weekHeaderField`
                - `dayHeaderField`
            - `weekField`
                - `dayField`

###### Custom Example

Add a highlighted background to today's day of the calendar.

**Step 1**

Add LinCalendar

```kotlin
val state = rememberLinCalendarState()

LinCalendar(
    state = state,
)
```

**Step 2**

If you look at the source code, you can see that the above code is equivalent to:



# Todo List

- [ ] 隐藏无关细节。比如monthFiled中的计算逻辑

# Reference

- [kizitonwose/Calendar](https://github.com/kizitonwose/Calendar)

# License

[MIT LICENSE](LICENSE)
